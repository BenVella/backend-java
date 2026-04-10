# Order Taking API

Production-oriented Spring Boot service baseline for an order intake backend.

This repository focuses on the engineering foundations that matter early in a backend service lifecycle:
- deterministic Maven builds
- stateless OAuth2 resource-server security with Keycloak
- role-based access control backed by JWT claims
- local infrastructure wiring for application, identity, and messaging dependencies
- operational basics such as health probes and structured auth failure responses

The order domain itself is still intentionally lightweight. The current value of the repository is the platform baseline: build reproducibility, security posture, local operability, and a service structure that can be expanded into a fuller order workflow.

## Technology Stack

- Java 21
- Spring Boot 3.4
- Maven 3.9.14
- Spring Security OAuth2 Resource Server
- Keycloak
- RabbitMQ
- Springdoc / OpenAPI runtime docs
- Docker Compose

## Engineering Highlights

- Maven Wrapper based build and packaging flow
- Stateless JWT validation with issuer, audience, and authorized-party (`azp`) enforcement
- Keycloak role mapping from both realm and client claims
- JSON 401 and 403 responses for predictable API failure handling
- Local Compose topology for application, Keycloak, PostgreSQL, and RabbitMQ
- Actuator health endpoints enabled for liveness and readiness probing

## Current Scope

Implemented today:
- public and secured example endpoints for authentication and authorization verification
- order API scaffolding under `com.backend.order`
- local Keycloak realm import for repeatable token-based testing
- messaging wiring for order-event experimentation via RabbitMQ

Not yet complete:
- persistent order storage
- mature order lifecycle and business rules
- contract-first OpenAPI generation pipeline
- deployment automation

That boundary is intentional. The repository already demonstrates a production-ready security and build baseline, while leaving room to extend the business domain in subsequent iterations.

## Security Model

Authentication is standardized on Keycloak-backed bearer tokens.

Every accepted token must satisfy:
- valid signature
- valid issuer
- valid timestamps
- expected audience for this API
- allowed authorized party (`azp`)

Authorization is enforced through Spring Security roles derived from:
- `realm_access.roles`
- `resource_access.{client-id}.roles`

See [auth.md](docs/wiki/auth.md) for the application-level security contract and [auth-keycloak.md](docs/wiki/auth-keycloak.md) for Keycloak setup and token flow.

## Running the Project

### Prerequisites

- Java 21
- Docker Desktop

### Build

Package build:

```shell
./scripts/build.sh
```

### Test

Test run:

```shell
./scripts/test.sh
```

GitHub Actions also runs the Maven build and test workflow automatically on every push and pull request.

### Local Infrastructure

Bring up the local stack:

```shell
docker compose up -d --build
```

The local stack includes:
- the API on `http://localhost:8080`
- Keycloak on `http://localhost:8090`
- RabbitMQ Management UI on `http://localhost:15672`

Note: Docker Desktop must be running before `docker compose up`.

## API and Operational Endpoints

Public endpoints:
- `GET /api/ping`
- `GET /helloGuest`
- `GET /actuator/health`
- `GET /actuator/health/liveness`
- `GET /actuator/health/readiness`
- `GET /v3/api-docs`
- `GET /swagger-ui/index.html`

Secured endpoints:
- `GET /helloUser`
- `GET /helloAdmin`
- `POST /api/orders`
- `GET /api/orders/{orderId}`

## Documentation Map

- [docs/wiki/auth.md](docs/wiki/auth.md): application security contract
- [docs/wiki/auth-keycloak.md](docs/wiki/auth-keycloak.md): Keycloak realm, clients, and token flow
- [docs/plan.md](docs/plan.md): delivery roadmap
- [docs/TODO.md](docs/TODO.md): implementation backlog
- [docs/CHANGELIST.md](docs/CHANGELIST.md): engineering milestones
- [docs/PROTOTYPE_REVIEW.md](docs/PROTOTYPE_REVIEW.md): current implementation assessment

## Portfolio Positioning

This repository is strongest as a demonstration of backend platform engineering judgment:
- secure-by-default API integration with an external identity provider
- deterministic local build execution
- practical local infrastructure orchestration
- clear separation between production-oriented foundations and incomplete business-domain work

That distinction matters. Mature engineering is not just about shipping features; it is also about establishing build, security, and operational constraints that let a service scale safely.
