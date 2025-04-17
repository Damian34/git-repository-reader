package com.damian34.gitreader;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

public class TestContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    static final MongoDBContainer mongoContainer = new MongoDBContainer("mongo:8.0.6")
            .withEnv("MONGO_INITDB_DATABASE", "test_db")
            .waitingFor(Wait.forListeningPort());

    static final KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.9.0"))
            .waitingFor(Wait.forListeningPort());

    static {
        mongoContainer.start();
        kafkaContainer.start();
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        TestPropertyValues.of(
                "spring.data.mongodb.uri=" + mongoContainer.getReplicaSetUrl(),
                "spring.kafka.bootstrap-servers=" + kafkaContainer.getBootstrapServers()
        ).applyTo(applicationContext.getEnvironment());
    }
}
