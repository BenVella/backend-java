# Order Taking and Fulfilment API

## Introduction

This project is an Order Taking and Fulfilment API implemented using Spring Boot. It's final version is targeting to provide logic for accepting, validating, and processing orders, as well as order approval and fulfilment secured behind an authentication system.

It's more of a prototype / proof of concept / spring boot playground.  Don't bet your life on it, unless it ain't worth much :)

**_Remember to check the docs folder for additional documentation with useful info._**

## Features and notes

- Gradle build with docker-compose
- Integrating locally hosted dockerized Keycloak instance for Token Endpoint authentication
- Docker compose
  - rabbitmq, for eventual integration of an amqp
  - keycloak for the targeted auth system once setup
  - postgresdb for keycloak above
- orders api requests to rabbitMq or kafka (todo)
  - Some structure is present under `com.backend.order`, but requires proper hookup
  - The structure should demonstrate the general flow of code, which should evolve as more functionality would be brought in

## Execution Requirements

- Docker Desktop (needed for our docker compose setup)
- Java 21 or higher, suggested install via Sdkman:
  - `sdk selfupdate force` - Might help on Windows since it's not too robust
  - `sdk install java 21-tem`
  - `sdk default java 21-tem`
  - For windows add JAVA_HOME to system vars: `JAVA_HOME = C:\Users\<USERNAME>\.sdkman\candidates\java\current`
  - And include JAVA_HOME/bin to path: `%JAVA_HOME%\bin`
- Create the `src/main/resources/application-secrets.yml` file locally.
  - `.gitignore` will not include this file in version control.

  ```yml
  secret:
    rabbitmq:
      username: <USERNAME>
      password: <PASSWORD>
  ```
  
Since these are naively setup in `docker-compose.yml` currently, you can check there to find out the details

# Getting Started

This only covers local development commands.  It's an unfinished project acting as a preview.

- Note that I elected to move away from multistage docker builds since the convenience was just costing a lot of time in containerisation.
- Proper devops would typically require the build to happen outside of docker, and docker would only be used to execute the built JAR

## Suggested

Simply running spring boot should see you sorted, so long as you have a Docker Engine running.  This should provide with better coloring and a generally convenient way to run and re-run your apps.


## Alternative Approach

If you encounter issues, don't have IntelliJ at hand and can't quite setup another IDE to this for you, the manual approach is as follows.

To avoid multistage docker files and only build when necessary, you must build before composing.

  ```shell
  ./gradlew clean build
  docker-compose up
  ``` 

### Alternative

If you want to stick to the terminal, or hate colors in IntelliJ you could:

- `./gradlew clean build` normally
- From `build.gradle.kts`, strip out the implementation `spring-boot-docker-compose` entry (this will conflict otherwise)
- Use `docker-compose up` to run it through compose.

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
  
# Authentication

Still a WIP.  It seemed like a worthwhile challenge to focus on

- Tried BezKoder's JWT (which worked but was severely outdated)
- OAuth Github and Google but ditched for excessive complexity and heavy limitations
- Keycloak - still to fully implement but ran out of time

# Ordering API

Also a WIP.  Placed some skeleton structure.

- I suppose the devil is in the details for APIs, it's easy to over or under engineer them and make a mess.
- Best to evolve the code alongside the necessary demands.
- Generally speaking however, API work is quite straightforward and "boring"
- Would absolutely use an Open API Spec 3.0 for documenting (even if just for internal use only)