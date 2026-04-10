# Keycloak Setup and Operations Guide

This guide documents the Keycloak shape expected by the application after the Maven migration and security hardening pass.

## Security Model

- The API is a stateless OAuth2 resource server.
- Token signature and issuer are validated by Spring Security.
- Token audience is also validated, so the access token must explicitly target this API.
- Token authorized party (`azp`) is also validated, so only approved OAuth clients can call this API.
- Authorities are derived from:
  - `realm_access.roles`
  - `resource_access.{client-id}.roles`

The API client id defaults to `order-taking-api`.
The allowed authorized party defaults to `order-taking-api-cli`.

## Local Stack

The local `docker-compose.yml` imports a ready-to-use realm from `docker/keycloak/realm-import/backend-java-realm.json`.

Local endpoints:
- API: `http://localhost:8080`
- Keycloak: `http://localhost:8090`
- RabbitMQ UI: `http://localhost:15672`

Bootstrap admin credentials:
- username: `admin`
- password: `admin`

Imported test users:
- `api-user` / `password`
- `api-admin` / `password`

These credentials are for local development only.

## Imported Realm

The imported realm is named `backend-java`.

Imported clients:
- `order-taking-api`
  - represents the protected API
  - expected audience for access tokens
- `order-taking-api-cli`
  - local-only client used to obtain test tokens
  - configured with direct access grants for easy local verification

## Obtaining a Test Token

User token:

```bash
curl -s \
  -X POST "http://localhost:8090/realms/backend-java/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=order-taking-api-cli" \
  -d "grant_type=password" \
  -d "username=api-user" \
  -d "password=password"
```

Admin token:

```bash
curl -s \
  -X POST "http://localhost:8090/realms/backend-java/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=order-taking-api-cli" \
  -d "grant_type=password" \
  -d "username=api-admin" \
  -d "password=password"
```

The resulting access token should contain `aud: ["order-taking-api", ...]`.
The resulting access token should also contain `azp: "order-taking-api-cli"`.

## Using the Token

```bash
curl -H "Authorization: Bearer <ACCESS_TOKEN>" http://localhost:8080/helloUser
curl -H "Authorization: Bearer <ACCESS_TOKEN>" http://localhost:8080/helloAdmin
```

## Production Guidance

- Do not run Keycloak with `start-dev` in production.
- Use HTTPS, a stable hostname, and externalized secrets.
- Keep audience validation enabled.
- Keep authorized-party validation enabled and scoped to the real interactive or automation clients that should reach the API.
- Disable direct access grants for real clients unless there is a specific operational reason.
- Prefer a dedicated interactive client for human login flows and a separate API client for the protected resource.
