FROM openjdk:21-jdk-slim

WORKDIR /app

RUN apt-get update && apt-get install -y git

COPY ./target/git-repository-reader-engine-exec.jar git-repository-reader-engine.jar
EXPOSE 8081

ENTRYPOINT ["java","-jar", "git-repository-reader-engine.jar"]

