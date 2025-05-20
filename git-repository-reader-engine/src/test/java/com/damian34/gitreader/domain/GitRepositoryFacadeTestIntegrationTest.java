package com.damian34.gitreader.domain;

import com.damian34.gitreader.GitData;
import com.damian34.gitreader.TestContainerInitializer;
import com.damian34.gitreader.domain.service.GitRepositoryFacade;
import com.damian34.gitreader.exception.NotFoundGitReaderException;
import com.damian34.gitreader.infrastructure.db.GitRepositoryDocumentRepository;
import com.damian34.gitreader.model.ProcessStatus;
import com.damian34.gitreader.model.queue.GitConnectionCredentials;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(initializers = TestContainerInitializer.class)
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
    @ValueSource(strings = { GitData.GITHUB_SECURITY_JWT_URL, GitData.GITHUB_SECURITY_JWT_URL_DOMAIN})
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
