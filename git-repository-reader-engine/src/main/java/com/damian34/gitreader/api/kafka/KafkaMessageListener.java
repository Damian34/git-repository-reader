package com.damian34.gitreader.api.kafka;

import com.damian34.gitreader.domain.service.GitRepositoryFacade;
import com.damian34.gitreader.model.queue.GitConnectionCredentials;
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

    @SneakyThrows
    @KafkaListener(topics = "git-credentials")
    public void gitCredentialsListener(String message) {
        log.info("Received git-credentials message: {}", message);
        GitConnectionCredentials credentials = objectMapper.readValue(message, GitConnectionCredentials.class);
        gitRepositoryFacade.processRepositoryData(credentials);
    }
}
