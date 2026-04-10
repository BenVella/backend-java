#!/usr/bin/env bash
set -euo pipefail

# Prefetches dependencies and plugins into the project-local Maven repository.

./mvnw -U -DskipTests dependency:go-offline
