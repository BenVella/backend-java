#!/usr/bin/env bash
set -euo pipefail

KEYCLOAK_DISCOVERY_URL="http://localhost:8090/realms/backend-java/.well-known/openid-configuration"

use_java21_if_available() {
  for candidate in \
    "$HOME/.local/share/mise/installs/java/21.0.2" \
    "$HOME/.local/share/mise/installs/java/21"; do
    if [[ -x "$candidate/bin/java" ]]; then
      export JAVA_HOME="$candidate"
      export PATH="$JAVA_HOME/bin:$PATH"
      return 0
    fi
  done
  return 1
}

current_java_version="$(java -version 2>&1 | head -n1 || true)"
if [[ "$current_java_version" != *'"21.'* ]] && [[ "$current_java_version" != *'"21"'* ]]; then
  use_java21_if_available || true
fi

docker compose up -d postgres keycloak

oidc_ready() {
  if command -v curl >/dev/null 2>&1; then
    curl -fsS "$KEYCLOAK_DISCOVERY_URL" >/dev/null
    return $?
  fi
  if command -v wget >/dev/null 2>&1; then
    wget -qO- "$KEYCLOAK_DISCOVERY_URL" >/dev/null
    return $?
  fi
  echo "Neither curl nor wget is available to probe Keycloak readiness." >&2
  return 1
}

for _ in $(seq 1 60); do
  if oidc_ready; then
    exec ./mvnw spring-boot:run \
      -Dspring-boot.run.arguments="--spring.main.banner-mode=off --logging.level.root=warn --logging.level.com.backend=info --logging.level.org.springframework=warn --logging.level.org.flywaydb=info"
  fi
  sleep 2
done

echo "Keycloak discovery endpoint not ready: $KEYCLOAK_DISCOVERY_URL" >&2
exit 1
