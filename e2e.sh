#!/bin/bash

until curl -s http://localhost:8080/actuator/health | grep "UP"; do
  echo "En attente du backend..."
  sleep 5
done

> output.log

cat cli/demo.txt | docker exec -i cli socat - TCP:localhost:1234 >> output.log

if grep -q "END OF SCENARIO" output.log; then
  echo "Tests E2E réussis"
  exit 0
else
  echo "Erreur dans le scénario E2E"
  cat output.log
  exit 1
fi
