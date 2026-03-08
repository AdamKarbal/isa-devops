#!/usr/bin/env bash

echo "Compiling the TCF Spring display service within a multi-stage docker build"

docker build --build-arg JAR_FILE=display-0.0.1-SNAPSHOT.jar -t skimaster-display:latest .
