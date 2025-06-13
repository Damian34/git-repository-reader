package com.damian34.gitreader.infrastructure.config.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;

import java.time.Duration;
import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class ReactiveKafkaConfig {

    private final ReactiveKafkaProperties kafkaProperties;
    
    @Bean
    public KafkaReceiver<String, String> gitCredentialsReceiver() {
        ReceiverOptions<String, String> options = ReceiverOptions
            .<String, String>create(kafkaProperties.createConsumerProps())
            .pollTimeout(Duration.ofMillis(kafkaProperties.getReactor().getPollTimeout()))
            .subscription(Collections.singleton(kafkaProperties.getTopics().getGitCredentials()));
        return KafkaReceiver.create(options);
    }
} 