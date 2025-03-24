# Order Taking and Fulfilment API

## Introduction

This project is an Order Taking and Fulfilment API implemented using Spring Boot. It provides functionalities for accepting, validating, and processing orders, as well as order approval and fulfilment.

## Features and notes

- Using [BezKoder Spring Security with PSQL](https://github.com/bezkoder/spring-boot-security-postgresql) authentication
  - Sign-in was modified from original to use header based, basic auth
- Gradle build with docker
- Docker compose 
  - To include a postgres container for development
- orders api requests to rabbitMq or kafka (todo)

## Requirements

- Java 17 or higher
- Docker

## Getting Started

Clone the repository and navigate to its dir.

- Make sure to build  the component first
  ```sh
  docker build -t orders-app:1.0 .
  ```
  
### Local development (with local postgres and rabbitMq)

- Build docker image (compose up includes local postgres for development)
    ```sh
    docker-compose up
    ```
    
# Deployment Notes

- Consider building without the integrated resources (postgres and rabbitMq)
- Security details and credentials should all be migrated to a secret manager (such as [Google Secret Manager](https://cloud.google.com/secret-manager/docs/configuring-secret-manager))
- Authentication should also be ported out of this application for larger implementation scopes.

### Multistage docker build (no local postgres or rabbitMq)

For production, you don't want to use the included postgres / rabbitMq deployments but manage them separately and bind to them

(Some additional work and reconfiguration would be required for this)

- Run the container
    ```sh
    docker run -d -p 8080:8080 orders-app:1.0
    ```