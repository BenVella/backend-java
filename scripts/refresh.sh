#!/bin/bash
./gradlew clean build && docker compose stop app && docker compose rm -f app && docker-compose up --build app