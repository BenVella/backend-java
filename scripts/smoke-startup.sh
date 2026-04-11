#!/usr/bin/env bash
set -euo pipefail

APP_HEALTH_URL="${APP_HEALTH_URL:-http://localhost:8080/actuator/health}"
APP_STARTUP_LOG="${APP_STARTUP_LOG:-target/app-startup.log}"

mkdir -p "$(dirname "$APP_STARTUP_LOG")"
: > "$APP_STARTUP_LOG"

./scripts/run.sh >"$APP_STARTUP_LOG" 2>&1 &
app_pid=$!

cleanup() {
  if [[ -n "${app_pid:-}" ]] && kill -0 "$app_pid" 2>/dev/null; then
    kill "$app_pid" 2>/dev/null || true
    wait "$app_pid" || true
  fi
}

trap cleanup EXIT

for _ in $(seq 1 90); do
  if curl -fsS "$APP_HEALTH_URL" | grep -q '"status":"UP"'; then
    echo "Startup smoke passed: $APP_HEALTH_URL"
    exit 0
  fi

  if ! kill -0 "$app_pid" 2>/dev/null; then
    echo "Application exited before becoming healthy." >&2
    cat "$APP_STARTUP_LOG" >&2
    exit 1
  fi

  sleep 2
done

echo "Timed out waiting for healthy startup: $APP_HEALTH_URL" >&2
cat "$APP_STARTUP_LOG" >&2
exit 1
