# Order Taking and Fulfilment API

## Introduction

This project is an Order Taking and Fulfilment API implemented using Spring Boot. It provides functionalities for accepting, validating, and processing orders, as well as order approval and fulfilment.

It's more of a prototype / proof of concept / spring boot playground.  Don't bet your life on it, unless it ain't worth much :)

## Features and notes

- Using [BezKoder Spring Security with PSQL](https://github.com/bezkoder/spring-boot-security-postgresql) authentication
  - Sign-in was modified from original to use header based, basic auth
- Gradle build with docker
- Docker compose 
  - To include a postgres container for development
- orders api requests to rabbitMq or kafka (todo)

## Requirements

- Java 21 or higher
  - Suggested to install via sdkman
- Docker Desktop
- **_Google API Credentials supporting backend for localhost_**
  - Navigate to [Google Auth Clients](https://console.cloud.google.com/auth/clients)
  - Create a client -> Web Application
  - Add a redirect URI: `http://localhost:8080/login/oauth2/code/google`
  - Create a `src/main/resources/application-secrets.yml` file with the below contents (replace as necessary)

```yml
secret:
  oauth:
    google:
      client-id: copied-id
      client-secret: copied-secret
    github: # optional
      client-id: id
      client-secret: secret
  datasource:
    url: jdbc:postgresql://localhost:5432/orderdb
    username: <USERNAME>
    password: <PASSWORD>
  rabbitmq:
    username: <USERNAME>
    password: <PASSWORD>
```

## Getting Started

Clone the repository and navigate to its dir.

### Local Development
The easiest and fastest way to run for development is to make use of `org.springframework.boot:spring-boot-docker-compose`.

- With IntelliJ, you can wait until the build is resolved, or create one for the local machine.
- Simply run it and the spring boot docker compose will auto compose the additional containers

### Alternative via manual docker

- Build docker image (compose up includes local postgres for development)
    ```sh
    docker-compose up --build
    ```

- Note that the `./gradlew` wrapper will pull the distro each time, which results in minutes long builds.
- It also hasn't been tested very much after swapping to the integrated docker-compose implementation and is likely to
require amendments

# Deployment Notes

- Security details and credentials should all be migrated to a secret manager (such as [Google Secret Manager](https://cloud.google.com/secret-manager/docs/configuring-secret-manager)) and retrieved that way
- A dedicated and better configured authentication to fit available resources should be used instead.

### Direct Docker build (No compose)

This is still partially todo, this project isn't really intended for production use and use at your own discretion.

For production, you don't want to use the included postgres / rabbitMq deployments but manage them separately and bind to them

(Some additional work and reconfiguration would be required for this)

- Run the container
    ```sh
    docker run -d -p 8080:8080 orders-app:1.0
    ```