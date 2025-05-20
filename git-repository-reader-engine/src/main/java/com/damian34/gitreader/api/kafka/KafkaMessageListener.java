package com.damian34.gitreader.api.kafka;

import com.damian34.gitreader.domain.service.GitRepositoryFacade;
import com.damian34.gitreader.model.queue.GitConnectionCredentials;
import com.damian34.gitreader.model.encryption.service.CredentialsEncryptionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverRecord;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaMessageListener {
    private final GitRepositoryFacade gitRepositoryFacade;
    private final ObjectMapper objectMapper;
    private final CredentialsEncryptionService credentialsEncryptionService;
    private final KafkaReceiver<String, String> gitCredentialsReceiver;

    @PostConstruct
    public void startListener() {
        gitCredentialsReceiver.receive()
                .flatMap(this::processRecord)
                .subscribe();
    }

    private Mono<Void> processRecord(ReceiverRecord<String, String> record) {
        log.info("Received message from topic: {}, key: {}", record.topic(), record.key());
        return deserializeAndDecrypt(record.value())
                .flatMap(gitRepositoryFacade::processRepositoryData)
                .doOnSuccess(v -> record.receiverOffset().acknowledge())
                .doOnError(e -> log.error("Error processing message: {}", e.getMessage(), e));
    }

    private Mono<GitConnectionCredentials> deserializeAndDecrypt(String message) {
        return Mono.fromCallable(() -> {
            try {
                GitConnectionCredentials encryptedCredentials = objectMapper.readValue(message, GitConnectionCredentials.class);
                log.info("Processing git-credentials with URL: {}", encryptedCredentials.url());
                return credentialsEncryptionService.decrypt(encryptedCredentials);
            } catch (Exception e) {
                log.error("Failed to deserialize or decrypt message: {}", e.getMessage());
                throw e;
            }
        });
    }
}
