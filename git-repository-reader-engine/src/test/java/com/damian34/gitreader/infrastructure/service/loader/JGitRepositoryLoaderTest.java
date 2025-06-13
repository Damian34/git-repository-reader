package com.damian34.gitreader.infrastructure.service.loader;

import com.damian34.gitreader.GitRepositoryUrls;
import com.damian34.gitreader.config.TestContainerKafkaInitializer;
import com.damian34.gitreader.config.TestContainerMongoInitializer;
import com.damian34.gitreader.infrastructure.exception.GitRepositoryException;
import com.damian34.gitreader.queue.GitConnectionCredentials;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import reactor.test.StepVerifier;

@SpringBootTest
@ContextConfiguration(initializers = {
        TestContainerMongoInitializer.class,
        TestContainerKafkaInitializer.class
})
class JGitRepositoryLoaderTest {

    @Autowired
    private JGitRepositoryLoader jGitRepositoryLoader;

    @Test
    @SneakyThrows
    void shouldLoadRepositoryWhenValidCredentials() {
        // given
        GitConnectionCredentials credentials = new GitConnectionCredentials(
                GitRepositoryUrls.GIT_REPOSITORY_READER_URL,
                null,
                null,
                null
        );

        // when & then
        JGitRepository repository = jGitRepositoryLoader.loadRepository(credentials).block();
        Assertions.assertNotNull(repository);
        Assertions.assertNotNull(repository.getGit());
        Assertions.assertNotNull(repository.getTempRepositoryDir());
    }

    @Test
    void shouldThrowExceptionWhenInvalidUrl() {
        // given
        GitConnectionCredentials credentials = new GitConnectionCredentials(
                null,
                null,
                null,
                null
        );

        // when & then
        StepVerifier.create(jGitRepositoryLoader.loadRepository(credentials))
                .expectError(GitRepositoryException.class)
                .verify();
    }

    @ParameterizedTest
    @ValueSource(strings = {GitRepositoryUrls.GOOGLE_URL, GitRepositoryUrls.INVALID_URL})
    void shouldThrowExceptionWhenInvalidOrNonGitUrl(String url) {
        // given
        GitConnectionCredentials credentials = new GitConnectionCredentials(
                url,
                null,
                null,
                null
        );

        // when & then
        StepVerifier.create(jGitRepositoryLoader.loadRepository(credentials))
                .expectError(GitRepositoryException.class)
                .verify();
    }
} 