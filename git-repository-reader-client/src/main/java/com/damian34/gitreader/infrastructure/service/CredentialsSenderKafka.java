package com.damian34.gitreader.infrastructure.service;

import com.damian34.gitreader.domain.CredentialsSender;
import com.damian34.gitreader.queue.GitConnectionCredentials;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CredentialsSenderKafka implements CredentialsSender {

    @Value("${spring.kafka.topics.git-credentials}")
    private String topic;

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper mapper;

    @SneakyThrows
    public void send(GitConnectionCredentials credentials) {
        var data = mapper.writeValueAsString(credentials);
        kafkaTemplate.send(topic, credentials.url(), data)
                .whenComplete((result, e) -> {
                    if (e != null) {
                        log.error("Failed to send message to Kafka with url: {}", credentials.url(), e);
                    } else {
                        log.info("Message sent successfully to Kafka with url: {}", credentials.url());
                    }
                });
    }

}
