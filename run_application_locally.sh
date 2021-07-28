#!/bin/sh
./gradlew clean assemble
docker build --tag github:latest .
docker-compose up
