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

mapfile -t demo_files < <(find ./cli/demos -maxdepth 1 -type f -name "*.txt" | sort)
if [[ "${#demo_files[@]}" -eq 0 ]]; then
  echo "No demo files found in ./cli/demos."
  exit 1
fi

echo "Running all demo scripts in /opt/app/demos through socat..."
{
  for demo_file in "${demo_files[@]}"; do
    demo_name="$(basename "${demo_file}")"
    echo "echo ===_RUNNING_SCRIPT_${demo_name}_==="
    echo "script /opt/app/demos/${demo_name}"
  done
  echo "exit"
} | timeout 300s socat - "EXEC:docker attach cli,pty,rawer,echo=0,setsid,sigint,sane" | tee "${LOG_FILE}"

echo "Validating all demo scripts were invoked..."
for demo_file in "${demo_files[@]}"; do
  demo_name="$(basename "${demo_file}")"
  grep -Fq "===_RUNNING_SCRIPT_${demo_name}_===" "${LOG_FILE}"
done
echo "CLI demo completed successfully."
