package com.damian34.gitreader.api;

import com.damian34.gitreader.config.TestContainerKafkaInitializer;
import com.damian34.gitreader.config.TestContainerMongoInitializer;
import com.damian34.gitreader.api.protocol.reqeust.GitCredentials;
import com.damian34.gitreader.api.protocol.reqeust.GitCredentialsRequest;
import com.damian34.gitreader.api.protocol.reqeust.GitUrlRequest;
import com.damian34.gitreader.infrastructure.repository.GitRepositoryDocumentRepository;
import com.damian34.gitreader.infrastructure.service.CredentialsSenderKafka;
import com.damian34.gitreader.ProcessStatus;
import com.damian34.gitreader.document.GitRepositoryDocument;
import com.damian34.gitreader.repository.Branch;
import com.damian34.gitreader.repository.Commit;
import com.damian34.gitreader.api.protocol.mapper.GitProtocolMapper;
import com.damian34.gitreader.api.protocol.response.GitRepositoryResponse;
import com.damian34.gitreader.domain.GitRepositoryClientFacade;
import com.damian34.gitreader.domain.dto.GitRepositoryDto;
import com.damian34.gitreader.domain.exception.GitRepositoryDeleteException;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
@SpringBootTest
@ContextConfiguration(initializers = {
        TestContainerMongoInitializer.class,
        TestContainerKafkaInitializer.class
})
class ClientControllerTest {

    private static final String REPOSITORY_URL = "https://github.com/Damian34/spring-security-jwt-auth";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GitRepositoryDocumentRepository gitRepositoryDocumentRepository;

    @MockitoBean
    private CredentialsSenderKafka credentialsMessageProducer;

    @BeforeEach
    void setUp() {
        gitRepositoryDocumentRepository.deleteAll();
    }

    @SneakyThrows
    @Test
    void shouldSendGitCredentialsToKafkaWhenValidData() {
        // given
        GitCredentials credentials = new GitCredentials(REPOSITORY_URL, null, null, null);
        GitCredentialsRequest request = new GitCredentialsRequest(List.of(credentials));

        // when
        mockMvc.perform(post("/api/v1/git/repository/load")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // then
        Mockito.verify(credentialsMessageProducer, Mockito.times(1)).send(Mockito.any());

        var repositoryDocuments = gitRepositoryDocumentRepository.findAll();
        var document = repositoryDocuments.getFirst();
        Assertions.assertEquals(ProcessStatus.WAITING, document.getStatus(), "GitRepositoryDocument Status should be WAITING");
    }

    @SneakyThrows
    @Test
    void shouldFailSendGitCredentialsWhenUrlBlank() {
        // given
        GitCredentials credentials = new GitCredentials(null, "username", "password", "token");
        GitCredentialsRequest request = new GitCredentialsRequest(List.of(credentials));

        // when and then
        mockMvc.perform(post("/api/v1/git/repository/load")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void shouldFailSendGitCredentialsWhenListIsEmpty() {
        GitCredentialsRequest request = new GitCredentialsRequest(new ArrayList<>());

        // when and then
        mockMvc.perform(post("/api/v1/git/repository/load")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void shouldGetGitRepositoryWhenExists() {
        // given
        GitUrlRequest request = new GitUrlRequest(REPOSITORY_URL);
        GitRepositoryDocument repositoryInDb = createRepository(request.getUrl());

        // when and then
        mockMvc.perform(get("/api/v1/git/repository")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value(repositoryInDb.getUrl()))
                .andExpect(jsonPath("$.status").value(repositoryInDb.getStatus().toString()));
    }

    @SneakyThrows
    @Test
    void shouldFailGetGitRepositoryWhenNotExists() {
        // given
        GitUrlRequest request = new GitUrlRequest(REPOSITORY_URL);

        // when and then
        mockMvc.perform(get("/api/v1/git/repository")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void shouldGetGitRepositoriesEmptyWhenNotExistsAny() {
        // when and then
        mockMvc.perform(get("/api/v1/git/repository/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @SneakyThrows
    @Test
    void shouldGetGitRepositoriesWhenExistsAny() {
        // given
        createRepository(REPOSITORY_URL);

        // when and then
        mockMvc.perform(get("/api/v1/git/repository/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void shouldDeleteGitRepositoryWhenStatusIsCompleted() throws Exception {
        // given
        GitRepositoryDocument repository = createRepository(REPOSITORY_URL);
        repository.setStatus(ProcessStatus.COMPLETED);
        gitRepositoryDocumentRepository.save(repository);
        GitUrlRequest request = new GitUrlRequest(REPOSITORY_URL);

        // when & then
        mockMvc.perform(delete("/api/v1/git/repository")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // verify
        assertThat(gitRepositoryDocumentRepository.findById(REPOSITORY_URL)).isEmpty();
    }

    @Test
    void shouldThrowExceptionWhenDeletingRepositoryWithNonCompletedStatus() throws Exception {
        // given
        GitRepositoryDocument repository = createRepository(REPOSITORY_URL);
        repository.setStatus(ProcessStatus.WAITING);
        gitRepositoryDocumentRepository.save(repository);
        GitUrlRequest request = new GitUrlRequest(REPOSITORY_URL);

        // when & then
        mockMvc.perform(delete("/api/v1/git/repository")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());

        // verify
        assertThat(gitRepositoryDocumentRepository.findById(REPOSITORY_URL)).isPresent();
    }

    private GitRepositoryDocument createRepository(String url) {
        GitRepositoryDocument repositoryInDb = new GitRepositoryDocument(url, null, ProcessStatus.FAILED, null,
                List.of(new Branch("branchId", "name",
                        List.of(new Commit("commitId", "name", "author", "date")))));
        return gitRepositoryDocumentRepository.save(repositoryInDb);
    }
}
