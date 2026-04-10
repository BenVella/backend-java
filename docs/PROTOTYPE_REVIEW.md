# Prototype Review: Scope, Purpose, and Notable Shortcomings

## Overall Scope and Purpose

This codebase is a **Spring Boot prototype** for an order-taking backend that explores three main concerns:

1. **Order API shape and package structure** (`com.backend.order`) for request intake and future orchestration.
2. **Auth strategy experimentation** (legacy JWT, OAuth2, and current Keycloak-focused resource-server setup).
3. **Asynchronous messaging integration** via RabbitMQ as a stand-in for eventual fulfillment/event-driven flows.

The project appears intentionally framed as an exploratory playground rather than production-ready software.

## What Exists Today (Prototype Baseline)

- Basic Spring Boot app skeleton and Gradle setup (Java 21, Spring Boot 3.4.x).
- Starter security wiring with role-based route guards and a custom JWT auth converter.
- Initial order controller/service/request models with placeholder business logic.
- RabbitMQ config and test-like message path for verifying queue publish/listen behavior.
- Docker Compose stack including app + RabbitMQ + Keycloak + Postgres for local experiments.
- Minimal test scaffolding (`@SpringBootTest` context test only).

## Notable Prototype-Phase Shortcomings

### 1) Domain and persistence are largely stubs
- `Order` is a plain model with string fields and no persistence annotations/repository.
- `OrderDao` exists only as an empty placeholder.
- `getOrderById` returns a new empty object instead of fetching data.

### 2) Controller contract and validation are incomplete
- `createOrder` accepts request body but does not use `@Valid` despite validation annotations in payloads.
- Response is a plain success string rather than a structured response with order id/status/errors.
- Type mismatch risk: controller path variable is `Long`, while model id is `String`.

### 3) Business logic is not implemented yet
- Service method builds an empty `Order` without mapping request fields.
- No order lifecycle/status tracking, deduplication/idempotency, or conflict handling.
- No explicit error model (4xx/5xx mapping, validation problem details, etc.).

### 4) Messaging flow is proof-of-concept only
- `CountDownLatch` in singleton receiver is not reset safely for repeated/parallel requests.
- Service waits synchronously on message acknowledgment, which undermines async design.
- Message payload typing/schema/versioning and dead-letter/retry behavior are not defined.

### 5) Security integration needs hardening and cleanup
- Security config class contains `@GetMapping` endpoints (mixes configuration and controller concerns).
- Every route currently requires auth; no explicit public health/info endpoints for ops.
- Debug/security logging is enabled in config, which is unsafe/noisy for non-dev environments.
- Token/issuer setup appears split between client/resource-server experiments and may be partially stale.

### 6) Environment and operability gaps
- Compose/config ports appear inconsistent for Keycloak references vs exposed ports.
- Secrets strategy is local-file based; no profile-based production secret handling is implemented.
- Dockerfile assumes pre-built JAR path/name and includes compose file in runtime image unnecessarily.

### 7) Test coverage is near-zero
- No unit tests for mapping/validation/business rules.
- No controller tests for request/response and status codes.
- No integration tests for auth behavior or RabbitMQ publishing path.

### 8) API maturity/documentation gaps
- No OpenAPI/Swagger contract generated, despite TODO intent.
- No API versioning strategy, pagination/filtering patterns, or standardized error envelope.

## Prototype-Appropriate Next Priorities

1. Add persistence baseline (JPA entity/repository + migration tooling + seeded local DB).
2. Complete API contract (`@Valid`, DTO mapping, typed responses, consistent ids, RFC7807 errors).
3. Separate synchronous order creation from async fulfillment events (outbox or durable publish strategy).
4. Refactor security boundaries (config vs controller separation; add health/actuator policy).
5. Introduce practical test pyramid (unit + web slice + integration with Testcontainers).
6. Publish OpenAPI spec and align request/response/error contracts to it.

## Bottom Line

As a prototype, this repository successfully demonstrates architectural direction and technology evaluation. The main limitation is that most core capabilities (persistence, business rules, robust API contracts, reliable messaging, and test coverage) are still scaffolding-level and need implementation before any production-style usage.
