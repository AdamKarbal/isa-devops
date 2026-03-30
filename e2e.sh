#!/bin/bash
set -euo pipefail

BACKEND_HEALTH_URL="http://localhost:8080/actuator/health"
CLI_CONTAINER="cli"
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

wait_for_container_health() {
  local container="$1"
  local retries=60

  until [ "$(docker inspect --format='{{.State.Health.Status}}' "$container" 2>/dev/null || true)" = "healthy" ]; do
    retries=$((retries - 1))
    if [ "$retries" -le 0 ]; then
      echo "Container ${container} is not healthy in time"
      return 1
    fi
    echo "En attente du container ${container}..."
    sleep 5
  done
}

wait_for_health "$BACKEND_HEALTH_URL" "backend"
wait_for_container_health "$CLI_CONTAINER"

marker="E2E_MARKER_$(date +%s)"

{
  echo "echo ${marker}"
  cat cli/demo.txt
} | docker exec -i "$CLI_CONTAINER" socat -u STDIN /proc/1/fd/0

start_time=$(date +%s)

while true; do
  if docker logs "$CLI_CONTAINER" 2>&1 | awk -v marker="$marker" 'found{print} $0 ~ marker{found=1}' | grep -q 'END OF SCENARIO'; then
    break
  fi

  now=$(date +%s)
  if [ $((now - start_time)) -ge "$TIMEOUT_SECONDS" ]; then
    echo "Erreur dans le scénario E2E (timeout)"
    docker logs "$CLI_CONTAINER" 2>&1 | awk -v marker="$marker" 'found{print} $0 ~ marker{found=1}' > "$OUTPUT_FILE"
    cat "$OUTPUT_FILE"
    exit 1
  fi

  sleep 2
done

docker logs "$CLI_CONTAINER" 2>&1 | awk -v marker="$marker" 'found{print} $0 ~ marker{found=1}' > "$OUTPUT_FILE"

if grep -q 'END OF SCENARIO' "$OUTPUT_FILE"; then
  echo "Tests E2E réussis"
  exit 0
else
  echo "Erreur dans le scénario E2E"
  cat "$OUTPUT_FILE"
  exit 1
fi
