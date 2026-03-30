#!/bin/bash
set -euo pipefail

BACKEND_HEALTH_URL="http://localhost:8080/actuator/health"
CLI_SERVICE="tcf-cli"
CLI_CONTAINER_FALLBACK="cli"
OUTPUT_FILE="output.log"
TIMEOUT_SECONDS=180

wait_for_health() {
  local url="$1"
  local name="$2"
  local retries=60

  until curl -fsS "$url" | grep -q '"status":"UP"'; do
    retries=$((retries - 1))
    if [ "$retries" -le 0 ]; then
      echo "${name} is not healthy in time"
      return 1
    fi
    echo "En attente de ${name}..."
    sleep 5
  done
}

get_cli_container_id() {
  local cid
  cid="$(docker compose ps -q "$CLI_SERVICE" 2>/dev/null || true)"
  if [ -n "$cid" ]; then
    echo "$cid"
    return 0
  fi

  cid="$(docker ps -q --filter "name=^/${CLI_CONTAINER_FALLBACK}$" 2>/dev/null || true)"
  if [ -n "$cid" ]; then
    echo "$cid"
    return 0
  fi

  return 1
}

wait_for_container_ready() {
  local retries=60

  while true; do
    local cid
    cid="$(get_cli_container_id || true)"

    if [ -n "$cid" ]; then
      local state health
      state="$(docker inspect --format='{{.State.Status}}' "$cid" 2>/dev/null || true)"
      health="$(docker inspect --format='{{if .State.Health}}{{.State.Health.Status}}{{else}}none{{end}}' "$cid" 2>/dev/null || true)"

      if [ "$state" = "running" ] && { [ "$health" = "healthy" ] || [ "$health" = "none" ] || [ "$health" = "unhealthy" ]; }; then
        if [ "$health" = "unhealthy" ]; then
          echo "Container CLI running mais healthcheck KO, on continue quand même..."
        fi
        echo "$cid"
        return 0
      fi
    fi

    retries=$((retries - 1))
    if [ "$retries" -le 0 ]; then
      echo "Container CLI non prêt à temps"
      return 1
    fi
    echo "En attente du container cli..."
    sleep 5
  done
}

wait_for_health "$BACKEND_HEALTH_URL" "backend"
CLI_CONTAINER_ID="$(wait_for_container_ready)"

marker="E2E_MARKER_$(date +%s)"

{
  echo "echo ${marker}"
  cat cli/demo.txt
} | docker exec -i "$CLI_CONTAINER_ID" socat -u STDIN /proc/1/fd/0

start_time=$(date +%s)

while true; do
  if docker logs "$CLI_CONTAINER_ID" 2>&1 | awk -v marker="$marker" 'found{print} $0 ~ marker{found=1}' | grep -q 'END OF SCENARIO'; then
    break
  fi

  now=$(date +%s)
  if [ $((now - start_time)) -ge "$TIMEOUT_SECONDS" ]; then
    echo "Erreur dans le scénario E2E (timeout)"
    docker logs "$CLI_CONTAINER_ID" 2>&1 | awk -v marker="$marker" 'found{print} $0 ~ marker{found=1}' > "$OUTPUT_FILE"
    cat "$OUTPUT_FILE"
    exit 1
  fi

  sleep 2
done

docker logs "$CLI_CONTAINER_ID" 2>&1 | awk -v marker="$marker" 'found{print} $0 ~ marker{found=1}' > "$OUTPUT_FILE"

if grep -q 'END OF SCENARIO' "$OUTPUT_FILE"; then
  echo "Tests E2E réussis"
  exit 0
else
  echo "Erreur dans le scénario E2E"
  cat "$OUTPUT_FILE"
  exit 1
fi
