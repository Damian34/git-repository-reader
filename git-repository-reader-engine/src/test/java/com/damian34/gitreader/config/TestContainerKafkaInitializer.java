package com.damian34.gitreader.config;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

public class TestContainerKafkaInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    static final KafkaContainer kafka =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.9.2"))
                    .withExposedPorts(9092, 9093)
                    .withEnv("KAFKA_AUTO_CREATE_TOPICS_ENABLE", "true")
                    .withEnv("KAFKA_GROUP_INITIAL_REBALANCE_DELAY_MS", "0")
                    .waitingFor(Wait.forListeningPort());

    static {
        kafka.start();
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        TestPropertyValues.of(
                "spring.kafka.bootstrap-servers=" + kafka.getBootstrapServers(),
                "spring.kafka.consumer.auto-offset-reset=earliest",
                "spring.kafka.consumer.group-id=test-consumer-group",
                "spring.kafka.admin.fail-fast=false",
                "spring.kafka.properties.allow.auto.create.topics=true"
        ).applyTo(applicationContext.getEnvironment());
    }
}
