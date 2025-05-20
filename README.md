# Git Repository Reader

The project is designed as an application based on two microservices. The architecture utilizes a message queue system (Kafka) to enable communication between services. The goal of the application is to retrieve commit data from specified Git repositories and store it in a MongoDB database. The project also features resilient communication through the use of Circuit Breaker and Retry patterns with Resilience4j, and utilizes Docker and Docker Compose for containerized environments.

### git-repository-reader-engine

This service retrieves access data from Kafka, connects to a Git repository, retrieves commits, and stores them in MongoDB.  
It uses a **reactive programming model** based on **Spring WebFlux** and a **Kafka consumer built with Reactor Kafka** to provide non-blocking data flow.  
JGit is used for interacting with Git repositories, and MongoDB is accessed in a reactive manner using `spring-boot-starter-data-mongodb-reactive`.

### git-repository-reader-client

This service exposes REST endpoints. One of them sends a message to Kafka to start the process of fetching data from the Git repository. Other endpoints allow fetching information about already retrieved repositories or their statuses.  
It is based on **Spring WebMVC** and provides **OpenAPI documentation** via Swagger.

#### Swagger

- **Swagger UI**: [http://localhost:8082/swagger-ui/index.html]
- **API Documentation (JSON)**: [http://localhost:8082/v3/api-docs]

## Technologies

- **Java 21** – the version of Java used in the project.
- **Maven** – a tool for managing dependencies and building the project.
- **Spring Boot** – a framework for building web applications.
- **Spring WebMVC** – used in the client module to expose classic REST endpoints.
- **Spring WebFlux** – used in the engine module to provide a fully reactive, non-blocking approach.
- **Kafka** – a message queue system used in the project for communication between services.
- **Reactor Kafka** – used in the engine module for reactive Kafka consumption.
- **MongoDB** – a NoSQL database used to store information about repositories.
- **Docker & Docker Compose** – for running containers with MongoDB, Kafka, and for testing.
- **Resilience4j** – for implementing **Circuit Breaker** and **Retry** patterns, ensuring application resilience in the face of external system failures.
- **Testcontainers** – for running MongoDB and Kafka instances in containers during tests.
- **Lombok** – A library used to reduce boilerplate code by automatically generating common methods.
- **JGit** – a library for working with Git repositories, used to retrieve commits and manage repositories.
- **DeepSource** – a static analysis tool integrated into the CI pipeline to automatically check code quality and security vulnerabilities.
- **Swagger/OpenAPI** – for documenting and visualizing the REST API endpoints.
- **MapStruct** – a code generator used to simplify the process of mapping between Java beans.

## Setup

### Prerequisites
- Java 21
- Docker (for running MongoDB and Kafka locally)
- Maven for building the project

### Installation Steps

- git clone https://github.com/Damian34/git-repository-reader.git
- cd git-repository-reader
- mvn clean install (or alternatively, to skip tests: mvn clean install -Dtest)
- docker-compose up
