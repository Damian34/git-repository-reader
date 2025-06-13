package com.damian34.gitreader.domain;

import com.damian34.gitreader.GitRepositoryUrls;
import com.damian34.gitreader.config.TestContainerKafkaInitializer;
import com.damian34.gitreader.config.TestContainerMongoInitializer;
import com.damian34.gitreader.infrastructure.exception.GitRepositoryException;
import com.damian34.gitreader.queue.GitConnectionCredentials;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(initializers = {
        TestContainerMongoInitializer.class,
        TestContainerKafkaInitializer.class
})
class GitRepositoryValidatorTest {

    @Autowired
    private GitRepositoryValidator validator;

    @Test
    void shouldNotThrowExceptionWhenValidUrl() {
        // given
        GitConnectionCredentials credentials = new GitConnectionCredentials(
                GitRepositoryUrls.GIT_REPOSITORY_READER_URL,
                null,
                null,
                null
        );

        // when
        Assertions.assertDoesNotThrow(() -> validator.validateCredentials(credentials));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n"})
    void shouldThrowExceptionWhenInvalidUrl(String url) {
        // given
        GitConnectionCredentials credentials = new GitConnectionCredentials(
                url,
                "username",
                "password",
                "token"
        );

        // when
        Assertions.assertThrows(GitRepositoryException.class, () -> validator.validateCredentials(credentials));
    }

    @Test
    void shouldPassValidWhenTokenAndCredentialsNotNUll() {
        // given
        GitConnectionCredentials credentials = new GitConnectionCredentials(
                GitRepositoryUrls.GIT_REPOSITORY_READER_URL,
                "username",
                "password",
                "token"
        );

        // when
        Assertions.assertDoesNotThrow(() -> validator.validateCredentials(credentials));
    }
} 