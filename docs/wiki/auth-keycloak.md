# Keycloak Setup and Operations Guide

## Purpose

This document describes the Keycloak configuration expected by the application, both for local development and for the production-style security posture implemented in the service.

The local stack is intentionally designed to make auth behavior repeatable: realm import, test users, expected claims, and client structure are all defined up front.

## Security Expectations

The API operates as a stateless OAuth2 resource server.

Required token characteristics:
- valid Keycloak issuer
- audience including `order-taking-api`
- authorized party (`azp`) matching an allowed client
- realm and client role claims used for RBAC

Default application values:
- API client id: `order-taking-api`
- allowed authorized party: `order-taking-api-cli`

## Local Stack

`docker-compose.yml` imports the local realm from `docker/keycloak/realm-import/backend-java-realm.json`.

Local endpoints:
- API: `http://localhost:8080`
- Keycloak: `http://localhost:8090`
- RabbitMQ UI: `http://localhost:15672`

Bootstrap admin credentials:
- username: `admin`
- password: `admin`

These admin credentials are for local development only.

## Imported Realm

Realm name:
- `backend-java`

Imported users:
- `api-user` / `password`
- `api-admin` / `password`

Imported clients:
- `order-taking-api`
  - represents the protected resource
  - expected audience for accepted access tokens
  - configured as bearer-only
- `order-taking-api-cli`
  - local development client for acquiring test tokens
  - enabled for direct access grants to simplify manual verification

## Obtaining a Token

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

Expected claims in the resulting access token:
- `aud` includes `order-taking-api`
- `azp` equals `order-taking-api-cli`

## Using the Token

```bash
curl -H "Authorization: Bearer <ACCESS_TOKEN>" http://localhost:8080/helloUser
curl -H "Authorization: Bearer <ACCESS_TOKEN>" http://localhost:8080/helloAdmin
```

## Production Guidance

For production-style deployment:
- do not run Keycloak with `start-dev`
- use HTTPS and a stable external hostname
- externalize secrets and bootstrap credentials
- keep audience validation enabled
- keep authorized-party validation enabled and scoped to approved clients
- separate interactive clients from protected-resource clients
- disable direct access grants unless there is a specific operational need

The local realm is deliberately simple. The production posture should keep the same validation principles while moving credentials, TLS, and operational settings into deployment-specific infrastructure.
