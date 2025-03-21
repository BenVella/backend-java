# Order Taking and Fulfilment API

## Introduction

This project is an Order Taking and Fulfilment API implemented using Spring Boot. It provides functionalities for accepting, validating, and processing orders, as well as order approval and fulfilment.

## Requirements

- Java 11 or higher
- Gradle
- Docker (for running RabbitMQ/Kafka if needed)
- An SMTP server for sending emails

## Getting Started

1. Clone the repository:
    ```sh
    git clone <repository-url>
    cd <repository-directory>
    ```
2. Build and run the project:
    ```sh
    ./scripts/run.sh
    ```

3. To run the tests:
    ```sh
    ./scripts/test.sh
    ```

4. For integration tests, use the provided Postman collection in the `scripts` folder.

Refer to the `HELP.md` file for detailed instructions on setting up and running the project.