package com.damian34.gitreader.infrastructure.service.repository;

import com.damian34.gitreader.GitData;
import com.damian34.gitreader.TestContainerInitializer;
import com.damian34.gitreader.exception.GitRepositoryException;
import com.damian34.gitreader.model.queue.GitConnectionCredentials;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(initializers = TestContainerInitializer.class)
class JGitRepositoryLoaderTest {

    @Autowired
    private JGitRepositoryLoader jGitRepositoryLoader;

    @Test
    @SneakyThrows
    void shouldLoadRepositoryWhenValidCredentials() {
        // given
        GitConnectionCredentials credentials = new GitConnectionCredentials(
                GitData.GIT_REPOSITORY_READER_URL,
                null,
                null,
                null
        );

        // when & then
        JGitRepository repository = jGitRepositoryLoader.loadRepository(credentials);
        Assertions.assertNotNull(repository);
        Assertions.assertNotNull(repository.getGit());
        Assertions.assertNotNull(repository.getTempRepositoryDir());
    }

    @Test
    void shouldThrowExceptionWhenNullUrl() {
        // given
        GitConnectionCredentials credentials = new GitConnectionCredentials(
                null,
                null,
                null,
                null
        );

        // when & then
        Assertions.assertThrows(GitRepositoryException.class, 
                () -> jGitRepositoryLoader.loadRepository(credentials));
    }

    @ParameterizedTest
    @ValueSource(strings = {GitData.GOOGLE_URL, GitData.INVALID_URL})
    void shouldThrowExceptionWhenInvalidOrNonGitUrl(String url) {
        // given
        GitConnectionCredentials credentials = new GitConnectionCredentials(
                url,
                null,
                null,
                null
        );

        // when & then
        Assertions.assertThrows(GitRepositoryException.class, 
                () -> jGitRepositoryLoader.loadRepository(credentials));
    }
} 