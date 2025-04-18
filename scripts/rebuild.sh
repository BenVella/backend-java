#!/bin/bash
./gradlew clean build && docker compose down && docker-compose up --build app