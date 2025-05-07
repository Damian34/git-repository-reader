package com.damian34.gitreader.domain.service;

import com.damian34.gitreader.GitData;
import com.damian34.gitreader.TestContainerInitializer;
import com.damian34.gitreader.exception.GitRepositoryException;
import com.damian34.gitreader.model.queue.GitConnectionCredentials;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(initializers = TestContainerInitializer.class)
class GitRepositoryValidatorTest {

    @Autowired
    private GitRepositoryValidator validator;

    @Test
    void shouldNotThrowExceptionWhenValidUrl() {
        // given
        GitConnectionCredentials credentials = new GitConnectionCredentials(
                GitData.GIT_REPOSITORY_READER_URL,
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
                GitData.GIT_REPOSITORY_READER_URL,
                "username",
                "password",
                "token"
        );

        // when
        Assertions.assertDoesNotThrow(() -> validator.validateCredentials(credentials));
    }
} 