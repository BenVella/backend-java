# Game Backend Foundation

Lean Spring Boot baseline for game-oriented backend services.

This repository stays intentionally small and favors platform basics over premature feature work:
- deterministic Maven builds
- PR-based CI validation
- stateless OAuth2 resource-server security with Keycloak
- role-based access control backed by JWT claims
- local operability with a minimal auth stack
- health probes and JSON auth failure responses

## Technology Stack

- Java 21
- Spring Boot 3.4
- Maven 3.9.14
- Spring Security OAuth2 Resource Server
- Keycloak
- Springdoc / OpenAPI runtime docs
- Docker Compose

## Current Scope

Implemented today:
- public and role-protected access probe endpoints
- local Keycloak realm import for repeatable token testing
- JWT validation with issuer, audience, and authorized-party checks
- Maven wrapper build and PR workflow validation

Not yet implemented:
- game-domain modules
- persistence and migration tooling
- realtime transport systems
- deployment automation

## Running the Project

### Prerequisites

- Java 21
- Docker Desktop

### Build

```shell
./scripts/build.sh
```

### Test

```shell
./scripts/test.sh
```

GitHub Actions runs the Maven build and test workflow for pull requests.

### Local Infrastructure

```shell
docker compose up -d --build
```

The local stack includes:
- the API on `http://localhost:8080`
- Keycloak on `http://localhost:8090`

## API and Operational Endpoints

Public endpoints:
- `GET /api/ping`
- `GET /api/access/public`
- `GET /actuator/health`
- `GET /actuator/health/liveness`
- `GET /actuator/health/readiness`
- `GET /v3/api-docs`
- `GET /swagger-ui/index.html`

Role-protected endpoints:
- `GET /api/access/user`
- `GET /api/access/admin`

## Documentation Map

- [docs/wiki/auth.md](docs/wiki/auth.md): application security contract
- [docs/wiki/auth-keycloak.md](docs/wiki/auth-keycloak.md): local Keycloak flow
- [docs/plan.md](docs/plan.md): delivery roadmap
- [docs/TODO.md](docs/TODO.md): implementation backlog
- [docs/CHANGELIST.md](docs/CHANGELIST.md): engineering milestones
- [docs/PROTOTYPE_REVIEW.md](docs/PROTOTYPE_REVIEW.md): current implementation assessment
