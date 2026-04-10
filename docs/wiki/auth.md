# Authentication and Authorization

## Current Standard (Keycloak + JWT Resource Server)

This service uses **Keycloak as the Identity Provider** and validates bearer JWTs with Spring Security's OAuth2 Resource Server support.

### Token validation
- JWT issuer is configured by `KEYCLOAK_ISSUER_URI`.
- Default local issuer: `http://localhost:8090/realms/myrealm`.
- In docker-compose, app uses `http://keycloak:8090/realms/myrealm`.

### Authorization model
- Role-based access control (RBAC) is enforced from JWT claims.
- Roles are extracted from:
  - `realm_access.roles`
  - `resource_access.demo.roles`
- Roles are normalized to Spring authorities (`ROLE_*`).

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

## Brief historical notes
- **Custom/legacy JWT approach**: dropped due to maintainability and modernization concerns.
- **GitHub OAuth attempt**: not suitable as a clean issuer-based resource-server fit for this API model.
- **Google OAuth attempt**: introduced complexity and API-calling overhead for token handling in this backend context.

The project now standardizes on Keycloak + JWT resource-server validation.
