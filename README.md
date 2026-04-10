# Order Taking and Fulfilment API

## Introduction

This project is an Order Taking and Fulfilment API implemented using Spring Boot. The current priority is a stable Maven build and a production-grade Keycloak-backed stateless auth baseline.

**_Remember to check the docs folder for additional documentation with useful info._**

## Features and notes

- Maven build with Maven Wrapper support
- Authentication uses Keycloak + JWT Resource Server validation (see `docs/wiki/auth.md`)
- Audience validation is enforced for API tokens
- Authorized-party validation is enforced so only approved OAuth clients can call the API
- Docker compose
  - rabbitmq, for eventual integration of amqp
  - keycloak + postgres for local auth verification
- orders api requests to rabbitMq or kafka (todo)
  - Some structure is present under `com.backend.order`, but requires proper hookup
  - The structure should demonstrate the general flow of code, which should evolve as more functionality would be brought in

## Execution Requirements

- Java 21
- Maven 3.9+ or the checked-in Maven Wrapper
- Docker Desktop

# Getting Started

## Suggested

Build the application jar and bring up the local stack:

  ```shell
  ./scripts/build.sh
  docker-compose up
  ```

Run the test suite:

```shell
./scripts/test.sh
```

# Deployment Notes

- Security details and credentials should all be migrated to a secret manager (such as [Google Secret Manager](https://cloud.google.com/secret-manager/docs/configuring-secret-manager)) and retrieved that way
- Keycloak should run with production TLS, hostname, and secret-management settings outside `start-dev`.

### Direct Docker build (No compose)

This is still partially todo, this project isn't really intended for production use and use at your own discretion.

For production, you don't want to use the included postgres / rabbitMq deployments but manage them separately and bind to them

(Some additional work and reconfiguration would be required for this)

- Run the container
    ```sh
    docker run -d -p 8080:8080 orders-app:1.0
    ```
  
# Authentication

Authentication is standardized on **Keycloak + JWT Resource Server**.

- Public endpoints: `/api/ping`, `/helloGuest`, and actuator health probes.
- Secured endpoints: `/helloUser` and `/helloAdmin` with role-based access control.
- Access tokens must contain the configured audience for this API.
- Access tokens must come from an allowed OAuth client (`azp` validation).
- API returns JSON 401/403 responses for auth failures.

See `docs/wiki/auth.md` for the application contract and `docs/wiki/auth-keycloak.md` for the Keycloak setup and token flow.

# Ordering API

Also a WIP.  Placed some skeleton structure.

- I suppose the devil is in the details for APIs, it's easy to over or under engineer them and make a mess.
- Best to evolve the code alongside the necessary demands.
- Generally speaking however, API work is quite straightforward and "boring"
- Would absolutely use an Open API Spec 3.0 for documenting (even if just for internal use only)

# Build Notes

The previous Gradle/OpenAPI generation path has been stepped over in favor of the Maven and security migration.

- Runtime OpenAPI docs remain available through Springdoc:
  - Swagger UI: `/swagger-ui/index.html`
  - Raw docs endpoint: `/v3/api-docs`

The static OpenAPI contract can be revisited after the Maven/auth baseline is stable.
