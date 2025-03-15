FROM openjdk:21-jdk-slim

WORKDIR /app

COPY ./target/git-repository-reader-engine-exec.jar git-repository-reader-engine.jar
EXPOSE 8080

ENTRYPOINT ["java","-jar", "git-repository-reader-engine.jar"]

