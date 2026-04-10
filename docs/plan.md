# Delivery Roadmap

## Objective

Keep this repository as a lean backend foundation, then add game-specific modules only when they have clear contracts.

## Phase 1: Platform Baseline

Completed:
- Maven migration from Gradle
- Keycloak-backed stateless resource-server security
- audience and authorized-party validation
- local Docker Compose auth stack
- PR CI validation for build and tests

## Phase 2: Foundation Cleanup

Completed:
- strip prototype-only code paths
- generalize project naming and configuration
- keep only neutral access probes and platform primitives
- publish the baseline OpenAPI contract through runtime docs

## Phase 3: Game Platform Modules

Next:
- add persistence and migration tooling
- define core game-service contracts
- introduce realtime transport boundaries
- add asynchronous flows only where justified

## Phase 4: Delivery and Operations

Planned:
- container publishing and deployment automation
- secret management integration
- stronger runtime observability and operational documentation
