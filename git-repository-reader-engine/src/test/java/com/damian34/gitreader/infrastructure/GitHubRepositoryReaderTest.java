package com.damian34.gitreader.infrastructure;

import com.damian34.gitreader.infrastructure.service.GitHubRepositoryReader;
import com.damian34.gitreader.model.queue.GitConnectionCredentials;
import com.damian34.gitreader.model.repository.Branch;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class GitHubRepositoryReaderTest {

    @Autowired
    private GitHubRepositoryReader gitHubRepositoryReader;

    @ParameterizedTest
    @ValueSource(strings = {
            "https://github.com/Damian34/spring-security-jwt-auth",
            "github.com/Damian34/spring-security-jwt-auth"
    })
    void shouldFindRepositoryBranchesTest(String url) {
        // given
        var gitUrl = gitHubRepositoryReader.buildGitCloneUrl(url);
        var credentials = new GitConnectionCredentials(gitUrl,null,null,null);

        // when
        List<Branch> branches = gitHubRepositoryReader.fetchBranches(credentials);

        // then
        Assertions.assertFalse(branches.isEmpty(), "Branches should not be empty.");
        branches.forEach(branch ->
            Assertions.assertFalse(branch.getCommits().isEmpty(), "Commits for branch " + branch.getName() + " should not be empty.")
        );
    }

}
