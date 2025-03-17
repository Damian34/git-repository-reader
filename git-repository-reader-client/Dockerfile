FROM openjdk:21-jdk-slim

WORKDIR /app

COPY ./target/git-repository-reader-client-exec.jar git-repository-reader-client.jar
EXPOSE 8082

ENTRYPOINT ["java","-jar", "git-repository-reader-client.jar"]

