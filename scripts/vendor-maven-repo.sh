#!/usr/bin/env bash
set -euo pipefail

# Prefetches all required plugins/dependencies into the project-local Maven repository.
# This is the one-time online step to support later offline builds/tests.

mvn -U -DskipTests dependency:go-offline
