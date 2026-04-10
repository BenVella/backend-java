# Authentication and Authorization

## Current Standard

This service uses **Keycloak as the Identity Provider** and validates bearer JWTs with Spring Security OAuth2 Resource Server support.

### Token validation

Every accepted token must satisfy all of the following:
- valid signature
- valid issuer
- valid timestamps
- configured audience for this API
- allowed authorized party (`azp`) for this API

The relevant configuration is:
- `KEYCLOAK_ISSUER_URI`
- `KEYCLOAK_CLIENT_ID`
- `KEYCLOAK_REQUIRED_AUDIENCE`
- `KEYCLOAK_ALLOWED_AUTHORIZED_PARTIES`

Default local values:
- issuer: `http://localhost:8090/realms/backend-java`
- client id: `order-taking-api`
- required audience: `order-taking-api`
- allowed authorized parties: `order-taking-api-cli`

### Authorization model

Role-based access control is enforced from JWT claims.

Authorities are mapped from:
- `realm_access.roles`
- `resource_access.{KEYCLOAK_CLIENT_ID}.roles`

All roles are normalized to Spring Security authorities using the `ROLE_` prefix.

### Endpoint access policy
Public (no auth required):
- `GET /api/ping`
- `GET /helloGuest`
- `GET /actuator/health`
- `GET /actuator/health/liveness`
- `GET /actuator/health/readiness`

Protected:
- `GET /helloUser` requires `ROLE_USER` or `ROLE_ADMIN`
- `GET /helloAdmin` requires `ROLE_ADMIN`
- everything else requires authentication

### Error handling
Security failures return JSON responses:
- **401 Unauthorized** when token is missing/invalid
- **403 Forbidden** when token is valid but lacks required role

## Health and readiness
The service exposes Actuator health probes:
- Liveness: `/actuator/health/liveness`
- Readiness: `/actuator/health/readiness`

This is the production-standard baseline and should be preferred over custom-only health routes.

## Why This Matters

Issuer-only validation is not enough for a production resource server in a shared identity domain.

Audience validation prevents the API from accepting tokens that were minted for a different client or downstream service under the same Keycloak realm.

Authorized-party validation lets the API restrict which OAuth clients are allowed to present those tokens, which is useful when a realm serves multiple frontends or automation clients.

## Keycloak Operations

See `docs/wiki/auth-keycloak.md` for:
- the imported local realm
- local test credentials
- token acquisition examples
- production deployment notes
