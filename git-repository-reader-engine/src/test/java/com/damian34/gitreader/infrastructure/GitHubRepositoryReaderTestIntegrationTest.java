package com.damian34.gitreader.infrastructure;

import com.damian34.gitreader.GitRepositoryUrls;
import com.damian34.gitreader.config.TestContainerKafkaInitializer;
import com.damian34.gitreader.config.TestContainerMongoInitializer;
import com.damian34.gitreader.infrastructure.service.GitHubRepositoryReader;
import com.damian34.gitreader.queue.GitConnectionCredentials;
import com.damian34.gitreader.repository.Branch;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

@SpringBootTest
@ContextConfiguration(initializers = {
        TestContainerMongoInitializer.class,
        TestContainerKafkaInitializer.class
})
class GitHubRepositoryReaderTestIntegrationTest {

    @Autowired
    private GitHubRepositoryReader gitHubRepositoryReader;

    @ParameterizedTest
    @ValueSource(strings = { GitRepositoryUrls.GITHUB_SECURITY_JWT_URL, GitRepositoryUrls.GITHUB_SECURITY_JWT_URL_DOMAIN})
    void shouldFindRepositoryBranchesTest(String url) {
        // given
        var gitUrl = gitHubRepositoryReader.buildGitCloneUrl(url);
        var credentials = new GitConnectionCredentials(gitUrl, null, null, null);

        // when
        List<Branch> branches = gitHubRepositoryReader.fetchBranches(credentials)
                .collectList()
                .block();

        // then
        Assertions.assertFalse(branches.isEmpty(), "Branches should not be empty.");
        branches.forEach(branch ->
            Assertions.assertFalse(branch.getCommits().isEmpty(), "Commits for branch " + branch.getName() + " should not be empty.")
        );
    }

}
