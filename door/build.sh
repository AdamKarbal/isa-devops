#!/usr/bin/env bash

echo "Compiling the TCF Spring door service within a multi-stage docker build"

docker build --build-arg JAR_FILE=door-0.0.1-SNAPSHOT.jar -t team-k/skimaster-door:local .
