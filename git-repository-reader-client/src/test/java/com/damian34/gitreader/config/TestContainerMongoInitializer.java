package com.damian34.gitreader.config;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.wait.strategy.Wait;

public class TestContainerMongoInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    static final MongoDBContainer mongoContainer = new MongoDBContainer("mongo:8.0.10")
            .withEnv("MONGO_INITDB_DATABASE", "test_db")
            .waitingFor(Wait.forListeningPort());

    static {
        mongoContainer.start();
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        TestPropertyValues.of(
                "spring.data.mongodb.uri=" + mongoContainer.getReplicaSetUrl()
        ).applyTo(applicationContext.getEnvironment());
    }
}
