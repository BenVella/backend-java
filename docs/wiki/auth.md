# Authentication and Authorization

## Overview

This service is a stateless OAuth2 resource server backed by Keycloak-issued JWT access tokens.

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
- client id: `game-backend`
- required audience: `game-backend`
- allowed authorized parties: `game-backend-cli`
- principal claim: `preferred_username`

## Authorization Model

Spring Security authorities are derived from:
- `realm_access.roles`
- `resource_access.{KEYCLOAK_CLIENT_ID}.roles`

Roles are normalized to Spring Security authorities with the `ROLE_` prefix.

## Endpoint Policy

Public endpoints:
- `GET /api/ping`
- `GET /api/access/public`
- `GET /actuator/health`
- `GET /actuator/health/liveness`
- `GET /actuator/health/readiness`
- `GET /v3/api-docs`
- `GET /swagger-ui/index.html`

Role-protected endpoints:
- `GET /api/access/user` requires `ROLE_USER` or `ROLE_ADMIN`
- `GET /api/access/admin` requires `ROLE_ADMIN`

Authenticated endpoints:
- all remaining routes require a valid bearer token

## Failure Handling

Authentication and authorization failures return JSON responses:
- `401 Unauthorized`
- `403 Forbidden`

For realm shape, local users, and token acquisition examples, see [auth-keycloak.md](auth-keycloak.md).
