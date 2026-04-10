# Authentication and Authorization

## Overview

This service is a stateless OAuth2 resource server backed by Keycloak-issued JWT access tokens.

The security model is designed around production-style validation rules rather than simple bearer-token acceptance. A token is only accepted when it is cryptographically valid and clearly intended for this API.

## Token Validation Contract

Every accepted token must satisfy all of the following:
- valid signature
- valid issuer
- valid timestamps
- configured audience for this API
- allowed authorized party (`azp`) for this API

Relevant configuration:
- `KEYCLOAK_ISSUER_URI`
- `KEYCLOAK_CLIENT_ID`
- `KEYCLOAK_REQUIRED_AUDIENCE`
- `KEYCLOAK_ALLOWED_AUTHORIZED_PARTIES`
- `KEYCLOAK_PRINCIPAL_CLAIM`

Default local values:
- issuer: `http://localhost:8090/realms/backend-java`
- client id: `order-taking-api`
- required audience: `order-taking-api`
- allowed authorized parties: `order-taking-api-cli`
- principal claim: `preferred_username`

## Authorization Model

Spring Security authorities are derived from Keycloak claims in two places:
- `realm_access.roles`
- `resource_access.{KEYCLOAK_CLIENT_ID}.roles`

Roles are normalized to Spring Security authorities with the `ROLE_` prefix.

Examples:
- realm role `admin` becomes `ROLE_ADMIN`
- client role `user` becomes `ROLE_USER`

## Endpoint Policy

Public endpoints:
- `GET /api/ping`
- `GET /helloGuest`
- `GET /actuator/health`
- `GET /actuator/health/liveness`
- `GET /actuator/health/readiness`
- `GET /v3/api-docs`
- `GET /swagger-ui/index.html`

Role-protected endpoints:
- `GET /helloUser` requires `ROLE_USER` or `ROLE_ADMIN`
- `GET /helloAdmin` requires `ROLE_ADMIN`

Authenticated endpoints:
- all remaining routes require a valid bearer token

## Failure Handling

Authentication and authorization failures return JSON responses rather than framework-default HTML.

- `401 Unauthorized`: token missing, malformed, expired, or failing validation
- `403 Forbidden`: token valid, but lacking required authority

This keeps API behavior predictable for clients and is a cleaner operational default for non-browser consumers.

## Why Audience and Authorized-Party Validation Matter

Issuer validation alone is not enough in a shared identity domain.

Audience validation ensures the token was minted for this API rather than a different downstream service. Authorized-party validation adds another control by restricting which OAuth clients are allowed to present that token to this service.

Together, those checks reduce the risk of accepting tokens that are technically valid but operationally out of scope.

## Operational Notes

- The service is stateless. No server-side session is created.
- CSRF is disabled because the API is designed for bearer-token usage rather than browser session workflows.
- Health probes are exposed through Actuator and intended for platform readiness and liveness checks.

For realm shape, local users, and token acquisition examples, see [auth-keycloak.md](auth-keycloak.md).
