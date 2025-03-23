# Order Taking and Fulfilment API

## Introduction

This project is an Order Taking and Fulfilment API implemented using Spring Boot. It provides functionalities for accepting, validating, and processing orders, as well as order approval and fulfilment.

## Features and notes

- Using [BezKoder Spring Security with PSQL](https://github.com/bezkoder/spring-boot-security-postgresql) authentication
  - Sign-in was modified from original to use header based, basic auth
- Gradle build with docker
- Docker with compose up (todo)
  - To include a postgres container for development
- orders api requests to rabbitMq or kafka (todo)
- 

## Requirements

- Java 17 or higher
- Docker

## Getting Started

1. Clone the repository and navigate to its dir
2. Build docker image
    ```sh
    docker build -t orders-app:1.0 .
    ```

3. Run
    ```sh
   docker run -d -p 8080:8080 orders-app:1.0
    ```

4. Import provided postman collection to sign up, sign in with a 1 hr access token and make requests