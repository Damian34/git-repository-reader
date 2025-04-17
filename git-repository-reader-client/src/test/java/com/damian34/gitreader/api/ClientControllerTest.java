package com.damian34.gitreader.api;

import com.damian34.gitreader.TestContainerInitializer;
import com.damian34.gitreader.api.protocol.reqeust.GitCredentials;
import com.damian34.gitreader.api.protocol.reqeust.GitCredentialsRequest;
import com.damian34.gitreader.api.protocol.reqeust.GitUrlRequest;
import com.damian34.gitreader.infrastructure.db.GitRepositoryDocumentRepository;
import com.damian34.gitreader.infrastructure.db.GitStatusRepository;
import com.damian34.gitreader.infrastructure.service.kafka.sender.KafkaCredentialsMessageSender;
import com.damian34.gitreader.model.ProcessStatus;
import com.damian34.gitreader.model.document.GitRepositoryDocument;
import com.damian34.gitreader.model.document.GitStatusDocument;
import com.damian34.gitreader.model.repository.Branch;
import com.damian34.gitreader.model.repository.Commit;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ContextConfiguration(initializers = TestContainerInitializer.class)
class ClientControllerTest {

    private static final String REPOSITORY_URL = "https://github.com/Damian34/spring-security-jwt-auth";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GitStatusRepository gitStatusRepository;

    @Autowired
    private GitRepositoryDocumentRepository gitRepositoryDocumentRepository;

    @MockitoBean
    private KafkaCredentialsMessageSender kafkaCredentialsMessageSender;

    @BeforeEach
    void setUp() {
        gitStatusRepository.deleteAll();
    }

    @SneakyThrows
    @Test
    void shouldSendGitCredentialsToKafkaWhenValidData() {
        // given
        GitCredentials credentials = new GitCredentials(REPOSITORY_URL, null, null, null);
        GitCredentialsRequest request = new GitCredentialsRequest(List.of(credentials));

        // when
        mockMvc.perform(post("/api/git/repository/load")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // then
        Mockito.verify(kafkaCredentialsMessageSender, Mockito.times(1)).sendMessage(Mockito.any());
        var statusDocument = gitStatusRepository.findAll();
        Assertions.assertFalse(statusDocument.isEmpty(), "GitStatusDocument should exists.");
        var status = statusDocument.getFirst();
        Assertions.assertEquals(ProcessStatus.WAITING, status.getStatus(), "GitStatusDocument should be WAITING");
    }

    @SneakyThrows
    @Test
    void shouldFailSendGitCredentialsWhenUrlBlank() {
        // given
        GitCredentials credentials = new GitCredentials(null, "username", "password", "token");
        GitCredentialsRequest request = new GitCredentialsRequest(List.of(credentials));

        // when and then
        mockMvc.perform(post("/api/git/repository/load")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void shouldFailSendGitCredentialsWhenListIsEmpty() {
        GitCredentialsRequest request = new GitCredentialsRequest(new ArrayList<>());

        // when and then
        mockMvc.perform(post("/api/git/repository/load")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void shouldGetGitStatusWhenExists() {
        // given
        GitUrlRequest request = new GitUrlRequest(REPOSITORY_URL);
        GitStatusDocument statusInDb = createStatus(request.getUrl());

        // when and then
        mockMvc.perform(get("/api/git/repository/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value(statusInDb.getUrl()))
                .andExpect(jsonPath("$.status").value(statusInDb.getStatus().toString()));
    }

    @SneakyThrows
    @Test
    void shouldFailGetGitStatusWhenNotExists() {
        // given
        GitUrlRequest request = new GitUrlRequest(REPOSITORY_URL);

        // when and then
        mockMvc.perform(get("/api/git/repository/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void shouldGetGitRepositoryWhenExists() {
        // given
        GitUrlRequest request = new GitUrlRequest(REPOSITORY_URL);
        GitStatusDocument statusInDb = createStatus(request.getUrl());
        GitRepositoryDocument repositoryInDb = createRepository(request.getUrl());

        // when and then
        mockMvc.perform(get("/api/git/repository")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value(repositoryInDb.getUrl()))
                .andExpect(jsonPath("$.status").value(statusInDb.getStatus().toString()));
    }

    @SneakyThrows
    @Test
    void shouldFailGetGitRepositoryWhenNotExists() {
        // given
        GitUrlRequest request = new GitUrlRequest(REPOSITORY_URL);

        // when and then
        mockMvc.perform(get("/api/git/repository")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }


    @SneakyThrows
    @Test
    void shouldGetGitRepositoriesEmptyWhenNotExistsAny() {
        // given
        GitUrlRequest request = new GitUrlRequest(REPOSITORY_URL);

        // when and then
        mockMvc.perform(get("/api/git/repository/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @SneakyThrows
    @Test
    void shouldGetGitRepositoriesWhenExistsAny() {
        // given
        GitUrlRequest request = new GitUrlRequest(REPOSITORY_URL);
        createStatus(request.getUrl());
        createRepository(request.getUrl());

        // when and then
        mockMvc.perform(get("/api/git/repository/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    private GitStatusDocument createStatus(String url) {
        GitStatusDocument statusInDb = new GitStatusDocument(url, null, ProcessStatus.FAILED, null);
        return gitStatusRepository.save(statusInDb);
    }

    private GitRepositoryDocument createRepository(String url) {
        GitRepositoryDocument repositoryInDb = new GitRepositoryDocument(url, null,
                List.of(new Branch("branchId", "name",
                        List.of(new Commit("commitId", "name", "author", "date")))));
        return gitRepositoryDocumentRepository.save(repositoryInDb);
    }
}
