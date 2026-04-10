# Repo Guide

## Purpose
- Lean Spring Boot baseline for game-oriented backend services.
- Favor stripping dead prototype code over preserving it.
- Keep implementation explicit, small, and easy to reason about.

## Stack
- Java 21
- Spring Boot 3.4
- Maven wrapper
- PostgreSQL
- Flyway SQL migrations
- Spring JDBC via `JdbcClient`
- OAuth2 resource server with Keycloak
- OpenAPI via springdoc
- Testcontainers for PostgreSQL-backed integration tests

## Current Shape
- Public and role-protected access probe endpoints only.
- First persistence slice is `player_profile`.
- No game-domain module is implemented yet.
- No deployment automation should be introduced unless asked.

## Important Paths
- `src/main/java/com/backend`: application code
- `src/main/resources/application.yml`: runtime config defaults
- `src/main/resources/db/migration`: Flyway migrations
- `src/test/java/com/backend/integration`: boot/integration tests
- `docker-compose.yml`: local Postgres + Keycloak + app stack
- `docs/TODO.md`: terse backlog
- `docs/plan.md`: roadmap

## Working Rules
- Prefer plain SQL repositories over ORM layers.
- Use Flyway for schema changes.
- Keep security/resource-server baseline intact.
- Do not reintroduce order/prototype scaffolding.
- Keep docs aligned with actual behavior.

## Local Run
- Infra only: `docker compose up -d postgres keycloak`
- App quiet mode: `scripts/run.cmd` or `./scripts/run.sh`
- App debug mode: `scripts/run-debug.cmd` or `./scripts/run-debug.sh`
- Full stack in containers: `docker compose up -d --build`

## Build And Test
- Build: `scripts/build.cmd` or `./scripts/build.sh`
- Test: `scripts/test.cmd` or `./scripts/test.sh`
- Full verify: `./mvnw clean verify` or `.\mvnw.cmd -B clean verify`
- Integration tests require Docker.

## Known Gotchas
- Testcontainers on this repo is pinned through Surefire system property `api.version=1.44` for Docker Desktop compatibility.
- Mockito emits a dynamic-agent warning during tests; currently non-blocking.
