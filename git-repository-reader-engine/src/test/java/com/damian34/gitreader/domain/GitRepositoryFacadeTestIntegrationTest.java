package com.damian34.gitreader.domain;

import com.damian34.gitreader.GitData;
import com.damian34.gitreader.TestContainerInitializer;
import com.damian34.gitreader.domain.service.GitRepositoryFacade;
import com.damian34.gitreader.exception.NotFoundGitReaderException;
import com.damian34.gitreader.infrastructure.db.GitRepositoryDocumentRepository;
import com.damian34.gitreader.infrastructure.db.GitStatusRepository;
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

    @Autowired
    private GitStatusRepository gitStatusRepository;

    @BeforeEach
    void setUp() {
        gitRepositoryDocumentRepository.deleteAll();
        gitStatusRepository.deleteAll();
    }

    @Test
    void shouldFailAndStoreExceptionWhenUrlIsNotSupported() {
        // given
        String url = "https://www.google.pl/";
        var credentials = new GitConnectionCredentials(url,null,null,null);

        // when
        gitRepositoryFacade.processRepositoryData(credentials);

        // then
        var repositoryDocument = gitRepositoryDocumentRepository.findAll();
        Assertions.assertTrue(repositoryDocument.isEmpty(), "GitRepositoryDocument should not exists.");

        var statusDocument = gitStatusRepository.findAll();
        Assertions.assertFalse(statusDocument.isEmpty(), "GitStatusDocument should exists.");
        var status = statusDocument.getFirst();
        Assertions.assertEquals(ProcessStatus.FAILED, status.getStatus(), "GitStatusDocument should be FAILED");
        Assertions.assertEquals(status.getException().getType(), NotFoundGitReaderException.class.getSimpleName());
    }

    @ParameterizedTest
    @ValueSource(strings = { GitData.GITHUB_SECURITY_JWT_URL, GitData.GITHUB_SECURITY_JWT_URL_DOMAIN})
    void shouldProcessGithubRepositories(String url) {
        // given
        var credentials = new GitConnectionCredentials(url,null,null,null);

        // when
        gitRepositoryFacade.processRepositoryData(credentials);

        // then
        var repositoryDocument = gitRepositoryDocumentRepository.findAll();
        Assertions.assertFalse(repositoryDocument.isEmpty(), "GitRepositoryDocument should exists.");
        repositoryDocument.getFirst().getBranches().forEach(branch ->
                Assertions.assertFalse(branch.getCommits().isEmpty(), "Commits for branch " + branch.getName() + " should not be empty.")
        );
        var statusDocument = gitStatusRepository.findAll();
        Assertions.assertFalse(statusDocument.isEmpty(), "GitStatusDocument should exists.");
        var status = statusDocument.getFirst();
        Assertions.assertEquals(ProcessStatus.COMPLETED, status.getStatus(), "GitStatusDocument should be COMPLETED");
    }
}
