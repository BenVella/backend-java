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

Clone the repository and navigate to its dir

### Local development (with local postgres)

Build docker image (compose up includes local postgres for development)

- 
    ```sh
    docker-compose up
    ```
    
### Multistage docker build (no local postgres)

If the local postgres is not required you can build the multistage docker file on its own (no local postgres)

- 
    ```sh
    docker build -t orders-app:1.0 .
    ```

Then run the container

- 
    ```sh
    docker run -d -p 8080:8080 orders-app:1.0
    ```