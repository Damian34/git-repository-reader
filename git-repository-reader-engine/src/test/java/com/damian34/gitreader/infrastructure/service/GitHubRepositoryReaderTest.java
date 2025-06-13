package com.damian34.gitreader.infrastructure.service;

import com.damian34.gitreader.GitRepositoryUrls;
import com.damian34.gitreader.config.TestContainerKafkaInitializer;
import com.damian34.gitreader.config.TestContainerMongoInitializer;
import com.damian34.gitreader.infrastructure.exception.GitRepositoryException;
import org.junit.jupiter.api.Assertions;
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
class GitHubRepositoryReaderTest {

    @Autowired
    private GitHubRepositoryReader gitHubRepositoryReader;

    @ParameterizedTest
    @ValueSource(strings = {
            GitRepositoryUrls.GITHUB_USER_REPO_HTTPS,
            GitRepositoryUrls.GITHUB_USER_REPO_HTTP,
            GitRepositoryUrls.GITHUB_USER_REPO_DOMAIN,
            GitRepositoryUrls.GITHUB_USER_REPO_WWW,
            GitRepositoryUrls.GITHUB_USER_REPO_TREE
    })
    void shouldReturnTrueWhenGitHubUrls(String url) {
        // when & then
        Assertions.assertTrue(gitHubRepositoryReader.isSupported(url));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            GitRepositoryUrls.GITLAB_URL,
            GitRepositoryUrls.BITBUCKET_URL,
            "invalid-url"
    })
    void shouldReturnFalseWhenNotGitHubUrls(String url) {
        // when & then
        Assertions.assertFalse(gitHubRepositoryReader.isSupported(url));
    }
    
    @Test
    void shouldReturnProperGitUrl() {
        // when & then
        Assertions.assertEquals(
                GitRepositoryUrls.GIT_REPOSITORY_READER_URL,
                gitHubRepositoryReader.buildGitCloneUrl(GitRepositoryUrls.GIT_REPOSITORY_READER_URL_NO_EXTENSION)
        );
        
        Assertions.assertEquals(
                GitRepositoryUrls.GIT_REPOSITORY_READER_URL,
                gitHubRepositoryReader.buildGitCloneUrl(GitRepositoryUrls.GIT_REPOSITORY_READER_URL_DOMAIN)
        );
        
        Assertions.assertEquals(
                GitRepositoryUrls.GIT_REPOSITORY_READER_URL,
                gitHubRepositoryReader.buildGitCloneUrl(GitRepositoryUrls.GIT_REPOSITORY_READER_URL)
        );
    }

    @Test
    void shouldThrowExceptionWhenInvalidFormat() {
        // given
        String url = GitRepositoryUrls.INVALID_URL;

        // when & then
        Assertions.assertThrows(GitRepositoryException.class, () -> gitHubRepositoryReader.buildGitCloneUrl(url));
    }

    @Test
    void shouldReturnCorrectOriginPath() {
        // when
        String originPath = gitHubRepositoryReader.getOriginPath();

        // then
        Assertions.assertEquals("refs/remotes/origin/", originPath);
    }
} 