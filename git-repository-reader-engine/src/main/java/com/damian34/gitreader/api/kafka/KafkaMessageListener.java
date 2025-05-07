package com.damian34.gitreader.api.kafka;

import com.damian34.gitreader.domain.service.GitRepositoryFacade;
import com.damian34.gitreader.model.queue.GitConnectionCredentials;
import com.damian34.gitreader.model.encryption.service.CredentialsEncryptionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaMessageListener {
    private final GitRepositoryFacade gitRepositoryFacade;
    private final ObjectMapper objectMapper;
    private final CredentialsEncryptionService credentialsEncryptionService;

    @SneakyThrows
    @KafkaListener(topics = "${spring.kafka.topics.git-credentials}")
    public void credentialsListener(String message) {
        GitConnectionCredentials encryptedCredentials = objectMapper.readValue(message, GitConnectionCredentials.class);
        log.info("Received git-credentials message with URL: {}", encryptedCredentials.url());
        GitConnectionCredentials decryptedCredentials = credentialsEncryptionService.decrypt(encryptedCredentials);
        gitRepositoryFacade.processRepositoryData(decryptedCredentials);
    }
}
