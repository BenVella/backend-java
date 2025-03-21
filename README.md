# Melita Order Taking and Fulfilment API

## Overview

This project consists of two main tasks to develop an authenticated Order Taking API stack and extend it to include order approval and fulfilment functionalities. The project is implemented using Spring Boot and follows best practices for code design, error handling, and integration patterns.

## Task 1: Order Taking API

### Requirements
1. **Version Control**: Code needs to be version controlled in a Git repository.
2. **Configuration**: Configuration should be externalized to Spring Cloud Config.
3. **Code Quality**: Expect very good code design and robust error handling, design, and integration patterns employed.
4. **Testing**: JUnit tests and Postman collections for integration tests are necessary.

### Deliverables
1. An authenticated public REST-based Order Taking API stack.
2. Microservices implemented using Spring Boot.
3. Publish events to RabbitMQ or Kafka.

### Functionality
- Accepts orders with customer details, installation address, preferred installation date and time slot, required products, and packages.
- Validates order details and accepts the order.
- Publishes a messaging event to RabbitMQ or Kafka for Melita's Ordering Fulfilment system and Care systems.

## Task 2: Order Approval and Fulfilment

### Requirements
1. **Version Control**: Code needs to be version controlled in a Git repository.
2. **Configuration**: Configuration should be externalized to Spring Cloud Config.
3. **Code Quality**: Expect very good code design and robust error handling, design, and integration patterns employed.
4. **Testing**: JUnit tests and Postman collections for integration tests are necessary.

### Deliverables
1. An authenticated private REST-based Order Approval API stack.
2. Microservices implemented using Spring Boot.
3. Publish/Consume events with RabbitMQ or Kafka.
4. Emails to be sent via SMTP.
5. Save orders to a database via Spring Data JPA.

### Functionality
- Consumes messages from RabbitMQ or Kafka.
- Sends an email with the order information to an agent.
- Submits the order restfully to the Ordering Fulfilment system.
- Persists orders in a database.
- Provides APIs for agent approval of certain configurable products before submitting the order to the Order Fulfilment system.

## Dependencies

The project uses the following dependencies:
- `spring-boot-starter-data-jpa`: For database interactions using Spring Data JPA.
- `spring-boot-starter-web`: For building RESTful web services.
- `spring-boot-starter-security`: For securing the API.
- `spring-boot-starter-amqp`: For RabbitMQ integration.
- `spring-kafka`: For Kafka integration.
- `spring-cloud-starter-config`: For externalized configuration using Spring Cloud Config.
- `spring-boot-starter-mail`: For sending emails.
- `spring-boot-starter-validation`: For validating request data.

## Getting Started

Refer to the `HELP.md` file for detailed instructions on setting up and running the project.