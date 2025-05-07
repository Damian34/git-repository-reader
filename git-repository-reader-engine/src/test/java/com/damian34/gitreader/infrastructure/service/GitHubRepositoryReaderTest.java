package com.damian34.gitreader.infrastructure.service;

import com.damian34.gitreader.GitData;
import com.damian34.gitreader.TestContainerInitializer;
import com.damian34.gitreader.exception.GitRepositoryException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(initializers = TestContainerInitializer.class)
class GitHubRepositoryReaderTest {

    @Autowired
    private GitHubRepositoryReader gitHubRepositoryReader;

    @ParameterizedTest
    @ValueSource(strings = {
            GitData.GITHUB_USER_REPO_HTTPS,
            GitData.GITHUB_USER_REPO_HTTP,
            GitData.GITHUB_USER_REPO_DOMAIN,
            GitData.GITHUB_USER_REPO_WWW,
            GitData.GITHUB_USER_REPO_TREE
    })
    void shouldReturnTrueWhenGitHubUrls(String url) {
        // when & then
        Assertions.assertTrue(gitHubRepositoryReader.isSupported(url));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            GitData.GITLAB_URL,
            GitData.BITBUCKET_URL,
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
                GitData.GIT_REPOSITORY_READER_URL, 
                gitHubRepositoryReader.buildGitCloneUrl(GitData.GIT_REPOSITORY_READER_URL_NO_EXTENSION)
        );
        
        Assertions.assertEquals(
                GitData.GIT_REPOSITORY_READER_URL, 
                gitHubRepositoryReader.buildGitCloneUrl(GitData.GIT_REPOSITORY_READER_URL_DOMAIN)
        );
        
        Assertions.assertEquals(
                GitData.GIT_REPOSITORY_READER_URL, 
                gitHubRepositoryReader.buildGitCloneUrl(GitData.GIT_REPOSITORY_READER_URL)
        );
    }

    @Test
    void shouldThrowExceptionWhenInvalidFormat() {
        // given
        String url = GitData.INVALID_URL;

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