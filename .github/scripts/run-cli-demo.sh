#!/usr/bin/env bash
set -euo pipefail

LOG_FILE="${1:-/tmp/cli-demo.log}"

echo "Starting compose stack for CLI demo scenario..."
docker compose up -d --build

echo "Waiting for CLI container healthcheck..."
for i in {1..60}; do
  status="$(docker inspect --format='{{.State.Health.Status}}' cli 2>/dev/null || true)"
  if [[ "${status}" == "healthy" ]]; then
    echo "CLI container is healthy."
    break
  fi

  if [[ "${i}" -eq 60 ]]; then
    echo "CLI container did not become healthy in time."
    docker compose ps
    exit 1
  fi
  sleep 5
done

echo "Running script /opt/app/demo.txt in CLI through socat..."
cat <<'EOF' | timeout 300s socat - "EXEC:docker attach cli,pty,rawer,echo=0,setsid,sigint,sane" | tee "${LOG_FILE}"
script /opt/app/demo.txt
exit
EOF

echo "Validating demo output..."
grep -q "===_13._FINAL_VERIFICATION_===" "${LOG_FILE}"
echo "CLI demo completed successfully."
