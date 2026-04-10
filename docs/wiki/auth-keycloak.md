# Keycloak Setup and Testing Guide (Human-Friendly)

This guide explains how to run Keycloak locally with this project, create roles/users, obtain a token, and test protected APIs.

## 1) Start the stack

From repo root:

```bash
docker-compose up --build
```

Key services:
- API: `http://localhost:8080`
- Keycloak: `http://localhost:8090`
- RabbitMQ UI: `http://localhost:15672`

## 2) Sign in to Keycloak Admin Console

Open:
- `http://localhost:8090`

Default admin credentials from `docker-compose.yml`:
- username: `admin`
- password: `admin`

## 3) Create or verify realm/client

The app expects issuer:
- `http://localhost:8090/realms/myrealm`

So ensure realm `myrealm` exists.

Create client:
- Client ID: `demo`
- Access type: confidential or public (for local testing, public is simpler)
- Enable Direct Access Grants (if you want username/password token calls)

## 4) Create roles used by API

Create realm roles:
- `USER`
- `ADMIN`

These match the API security rules:
- `/helloUser` requires `USER` or `ADMIN`
- `/helloAdmin` requires `ADMIN`

## 5) Create test users and assign roles

Example users:
- `test-user` with role `USER`
- `test-admin` with role `ADMIN`

Set passwords and disable temporary password requirement.

## 6) Get an access token

Use token endpoint (password grant for local testing):

```bash
curl -X POST "http://localhost:8090/realms/myrealm/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=demo" \
  -d "grant_type=password" \
  -d "username=test-user" \
  -d "password=<PASSWORD>"
```

Copy `access_token` from response.

## 7) Call API endpoints

Public endpoints (no token needed):

```bash
curl http://localhost:8080/api/ping
curl http://localhost:8080/helloGuest
curl http://localhost:8080/actuator/health/readiness
```

Protected endpoint with token:

```bash
TOKEN="<ACCESS_TOKEN>"
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/helloUser
```

Admin endpoint (requires ADMIN role):

```bash
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/helloAdmin
```

## 8) Expected auth behavior

- Missing/invalid token => `401 Unauthorized` JSON response.
- Valid token but missing required role => `403 Forbidden` JSON response.

## 9) Common troubleshooting

1. **401 on everything protected**
   - Check issuer URI in app config: `KEYCLOAK_ISSUER_URI`.
   - Ensure token is from `myrealm` and not expired.

2. **403 on `/helloAdmin`**
   - User token does not contain `ADMIN` role.

3. **Keycloak not reachable**
   - Confirm containers are running and port `8090` is free.

4. **Token call fails**
   - Verify `demo` client exists and allows direct access grants.

## 10) Recommended local testing flow

1. Start compose.
2. Create realm/client/roles/users once.
3. Obtain token for `test-user` and test `/helloUser`.
4. Obtain token for `test-admin` and test `/helloAdmin`.
5. Validate no-auth endpoints remain accessible.

---

For system-level auth design details, see: `docs/wiki/auth.md`.
