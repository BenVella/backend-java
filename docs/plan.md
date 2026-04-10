# Delivery Roadmap

## Objective

Establish this repository as a production-oriented backend service baseline, then expand the business domain on top of that foundation.

## Phase 1: Platform Baseline

Completed:
- Maven migration from Gradle
- Keycloak-backed stateless resource-server security
- audience and authorized-party validation
- local Docker Compose stack for auth and messaging dependencies

## Phase 2: API Contract and Domain Maturity

Next:
- tighten request validation on order endpoints
- introduce typed response models for order creation and retrieval
- define a durable order lifecycle and state transitions
- align the order API to a stable OpenAPI contract

## Phase 3: Persistence and Messaging Reliability

Planned:
- add a persistence layer for orders
- introduce migration tooling and local seeded data
- formalize event payloads and delivery guarantees
- clarify synchronous API behavior versus asynchronous fulfillment processing

## Phase 4: Delivery and Operations

Planned:
- CI validation for tests and packaging
- container publishing and deployment automation
- secret management integration
- stronger runtime observability and operational documentation

## Guiding Principle

The repository should continue to reflect disciplined engineering tradeoffs:
- secure defaults first
- deterministic builds first
- clear operational behavior first
- domain complexity layered in after the platform baseline is stable
