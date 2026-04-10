# Keycloak Setup and Operations Guide

## Purpose

This document describes the local Keycloak configuration expected by the application.

## Security Expectations

Required token characteristics:
- valid Keycloak issuer
- audience including `game-backend`
- authorized party (`azp`) matching an allowed client
- realm and client role claims used for RBAC

Default application values:
- API client id: `game-backend`
- allowed authorized party: `game-backend-cli`

## Local Stack

`docker-compose.yml` imports the local realm from `docker/keycloak/realm-import/backend-java-realm.json`.

Local endpoints:
- API: `http://localhost:8080`
- Keycloak: `http://localhost:8090`

Bootstrap admin credentials:
- username: `admin`
- password: `admin`

## Imported Realm

Realm name:
- `backend-java`

Imported users:
- `api-user` / `password`
- `api-admin` / `password`

Imported clients:
- `game-backend`
  - protected resource client
  - expected audience for accepted tokens
  - configured as bearer-only
- `game-backend-cli`
  - local development client for acquiring test tokens
  - enabled for direct access grants

## Obtaining a Token

User token:

```bash
curl -s \
  -X POST "http://localhost:8090/realms/backend-java/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=game-backend-cli" \
  -d "grant_type=password" \
  -d "username=api-user" \
  -d "password=password"
```

Admin token:

```bash
curl -s \
  -X POST "http://localhost:8090/realms/backend-java/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=game-backend-cli" \
  -d "grant_type=password" \
  -d "username=api-admin" \
  -d "password=password"
```

Expected claims:
- `aud` includes `game-backend`
- `azp` equals `game-backend-cli`

## Using the Token

```bash
curl -H "Authorization: Bearer <ACCESS_TOKEN>" http://localhost:8080/api/access/user
curl -H "Authorization: Bearer <ACCESS_TOKEN>" http://localhost:8080/api/access/admin
```
