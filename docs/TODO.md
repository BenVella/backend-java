# Implementation Backlog

## High Priority

- Replace placeholder order creation behavior with mapped domain data and typed responses
- Add validation enforcement to order creation requests
- Introduce persistent storage for orders
- Define a consistent error contract for business and validation failures

## Medium Priority

- Publish a contract-first OpenAPI specification and align controller responses to it
- Improve messaging reliability semantics around order submission and acknowledgment
- Add broader automated test coverage for controller, service, and messaging paths
- Introduce CI workflow coverage for build and test execution

## Low Priority

- Expand observability beyond basic health checks
- Add deployment manifests or infrastructure examples
- Introduce additional domain flows beyond the current order intake baseline

## Backlog Standard

Items in this file should remain implementation-oriented and action-driven.

Narrative notes, historical context, and design rationale belong in:
- `README.md`
- `docs/CHANGELIST.md`
- `docs/PROTOTYPE_REVIEW.md`
