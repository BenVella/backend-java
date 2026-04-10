#!/usr/bin/env bash
set -euo pipefail

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
exec ./mvnw spring-boot:run \
  -Dspring-boot.run.arguments="--spring.main.banner-mode=off --logging.level.root=warn --logging.level.com.backend=info --logging.level.org.springframework=warn --logging.level.org.flywaydb=info"
