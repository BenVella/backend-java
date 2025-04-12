# Order Taking and Fulfilment API

## Introduction

This started off as an interview task expecting a customer order management API.

- The plumbing was the ultimate focus for me instead so little was on with respect to orders
- Ordering was to be hollowed out and the backend was to shift towards a game development setup introducing a variety of bells and whistles

It's more of a prototype / proof of concept / spring boot playground.  Don't bet your life on it, unless it ain't worth much :)

**_Remember to check the docs folder for additional documentation with useful info._**

## Features and notes

- Gradle build with docker-compose
- Integrating locally hosted dockerized Keycloak instance for Token Endpoint authentication
- Docker compose:
  - rabbitmq, for eventual integration of an amqp
  - keycloak for bearer based authentication for rest api consumption
  - postgresdb for keycloak above
- RabbitMq
  - Setup in docker and config but not being used at all for the time being, would be a good way to buffer and de-synchronise workloads

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
- Proper devops would typically require the build to happen outside of docker, and docker would help compose the final environment for the stack to run

## Build and Run

All you need to get going is (as long as you meet requirements above):

  ```shell
  ./gradlew clean build && docker-compose up
  ``` 

The build will generate the `./build/` dir.
Docker compose will copy the `./build/libs/<JAR_FILE>` into its working dir and wait on all necessary dependents to start.

### IntelliJ

If on IntelliJ, you can turn this into a Run Configuration:

- Create a docker compose config
- Add a "Before launch" step:
  - `Run Gradle Task` -> `order-taking-api` -> Tasks: `build`

# Notes

## Deployment

- Security details and credentials should all be migrated to a secret manager (such as [Google Secret Manager](https://cloud.google.com/secret-manager/docs/configuring-secret-manager)) and retrieved that way
- Keycloak should be hosted independently, and allowed-origins update to reflect this.

## Authentication

- Keycloak - still to fully implement but ran out of time

## Rest API

Also a WIP.  Placed some skeleton structure.

- I suppose the devil is in the details for APIs, it's easy to over or under engineer them and make a mess.
- Best to evolve the code alongside the necessary demands.
- Generally speaking however, API work is generally straightforward and lower effort than the plumbing that supports it
- Would absolutely use an Open API Spec 3.0 for documenting the API (even if just for internal use only)