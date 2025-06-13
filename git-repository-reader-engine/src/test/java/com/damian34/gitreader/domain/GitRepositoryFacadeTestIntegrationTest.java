package com.damian34.gitreader.domain;

import com.damian34.gitreader.GitRepositoryUrls;
import com.damian34.gitreader.config.TestContainerKafkaInitializer;
import com.damian34.gitreader.config.TestContainerMongoInitializer;
import com.damian34.gitreader.infrastructure.exception.NotFoundGitReaderException;
import com.damian34.gitreader.infrastructure.repository.GitRepositoryDocumentRepository;
import com.damian34.gitreader.ProcessStatus;
import com.damian34.gitreader.queue.GitConnectionCredentials;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(initializers = {
        TestContainerMongoInitializer.class,
        TestContainerKafkaInitializer.class
})
class GitRepositoryFacadeTestIntegrationTest {

    @Autowired
    private GitRepositoryFacade gitRepositoryFacade;

    @Autowired
    private GitRepositoryDocumentRepository gitRepositoryDocumentRepository;

    @BeforeEach
    void setUp() {
        gitRepositoryDocumentRepository.deleteAll().block();
    }

    @Test
    void shouldFailAndStoreExceptionWhenUrlIsNotSupported() {
        // given
        String url = "https://www.google.pl/";
        var credentials = new GitConnectionCredentials(url, null, null, null);

        // when
        gitRepositoryFacade.processRepositoryData(credentials).block();

        // then
        var repositoryDocuments = gitRepositoryDocumentRepository.findAll().collectList().block();
        Assertions.assertFalse(repositoryDocuments.isEmpty(), "GitRepositoryDocument should exist.");
        var document = repositoryDocuments.getFirst();
        Assertions.assertEquals(ProcessStatus.FAILED, document.getStatus(), "GitRepositoryDocument status should be FAILED");
        Assertions.assertEquals(document.getException().getType(), NotFoundGitReaderException.class.getSimpleName());
    }

    @ParameterizedTest
    @ValueSource(strings = { GitRepositoryUrls.GITHUB_SECURITY_JWT_URL, GitRepositoryUrls.GITHUB_SECURITY_JWT_URL_DOMAIN})
    void shouldProcessGithubRepositories(String url) {
        // given
        var credentials = new GitConnectionCredentials(url, null, null, null);

        // when
        gitRepositoryFacade.processRepositoryData(credentials).block();

        // then
        var repositoryDocuments = gitRepositoryDocumentRepository.findAll().collectList().block();
        Assertions.assertFalse(repositoryDocuments.isEmpty(), "GitRepositoryDocument should exist.");
        var document = repositoryDocuments.getFirst();
        Assertions.assertEquals(ProcessStatus.COMPLETED, document.getStatus(), "GitRepositoryDocument status should be COMPLETED");
        document.getBranches().forEach(branch ->
                Assertions.assertFalse(branch.getCommits().isEmpty(), "Commits for branch " + branch.getName() + " should not be empty.")
        );
    }
}
