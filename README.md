# Git Repository Reader

## Summary

I built this experimental project with **2 microservices** communicating through **Apache Kafka**, primarily to serve as an example of a distributed system.
Initially, I wanted a simple data source for the app. Something that didn’t require scraping tools like Selenium.  
I chose **Git repositories**. It seemed straightforward, but once I started analyzing Git’s internal structure, it turned out to be a surprisingly complex graph.
The project is intentionally simple and has some shortcomings, detailed in the points below.

### How a Git Repository Looks and Works?

Git data has a **graph** structure. I tried working with tools like **JGit** and **Git CLI**.  
A branch in Git is just a reference (a HEAD pointer) to a commit in this graph. You can navigate the repository history in any direction with checkouts.
Branches can have many merges from top (e.g. merging `dev` into `main`) and bottom (e.g. creating `dev` from `main`, pulling changes from other branches, etc.).

This means a branch can have multiple splits from top and bottom.
Git keeps track of the top (HEAD), but not the "bottom", so when you ask Git CLI for all commits in a branch, you get everything from HEAD down to the root of the entire commit history.

But what I needed was the exact point where a branch (e.g. `dev`) was created.

#### Possible solutions:

- Tools like `merge-base` only work based on commit history and easily get confused by squashes, rebases, and other rewriting.
- Another idea was to check the number of branch-outs per commit, but this also wasn’t reliable. 
  I couldn't tell whether a given commit was a creation point or a later merge from another branch.

For example, sure, I can get the HEAD, but how can I be sure that an earlier commit split wasn’t just a `main` → `dev` pull update?

Eventually, I dropped the idea of parsing the repository/branch structure in Git. That wasn’t my main goal anyway, I just needed a dataset for the app.
Besides, the Git graph structure was neither an issue for storing data in MongoDB nor for mapping with a standard ObjectMapper.

---

#### What went relatively well

- I split the app into two microservices: `engine` (processing) and `client` (presentation layer)
- I added Kafka, even though it’s just one communication channel between the services
- I extracted a shared library (though I’m aware that might not be a best practice in microservice architecture)
- I implemented the **Circuit Breaker** pattern
- I used MongoDB, which is now at least represented in one of my portfolio projects

---

#### What could have been done better

- Since I didn’t succeed in normalizing repository data, I ended up storing everything directly in MongoDB  
  and didn’t convert it into structured entities in a relational database like Postgres  
  (this could’ve been a good opportunity to create an extra microservice for normalization and event-based communication)
- Both the `client` and `engine` services share a single MongoDB instance, which is not ideal for a proper Kubernetes setup, although technically it could still work.
- I used DeepSource for pipeline reports because it seemed like the best free option, but later I realized it doesn't fully support Java in the free tier, so I partially abandoned it.
- I could have started the project with a dedicated `dev` branch from the beginning, instead of committing most of the work directly to `main` which is commonly used as production branch.

---

### git-repository-reader-engine

This service retrieves access data from Kafka, connects to a Git repository, retrieves commits, and stores them in MongoDB.  
It uses a **reactive programming model** based on **Spring WebFlux** and a **Kafka consumer built with Reactor Kafka** to provide non-blocking data flow.  
JGit is used for interacting with Git repositories, and MongoDB is accessed in a reactive manner using `spring-boot-starter-data-mongodb-reactive`.

### git-repository-reader-client

This service exposes REST endpoints. One of them sends a message to Kafka to start the process of fetching data from the Git repository. Other endpoints allow fetching information about already retrieved repositories or their statuses.  
It is based on **Spring WebMVC** and provides **OpenAPI documentation** via Swagger.

## Swagger

- **Swagger UI**: http://localhost:8082/swagger-ui/index.html
- **API Documentation (JSON)**: http://localhost:8082/v3/api-docs

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

Run the API using `docker-compose.yaml`, for example with Docker Desktop.
