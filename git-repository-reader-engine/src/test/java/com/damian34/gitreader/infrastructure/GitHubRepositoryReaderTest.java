package com.damian34.gitreader.infrastructure;

import com.damian34.gitreader.domain.model.Branch;
import com.damian34.gitreader.domain.service.vo.GitConnectionCredentials;
import com.damian34.gitreader.infrastructure.service.GitHubRepositoryReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class GitHubRepositoryReaderTest {

    @Autowired
    private GitHubRepositoryReader gitHubRepositoryReader;

    @Test
    void shouldFindRepositoryBranchesTest() {
        // given
        String url = "https://github.com/Damian34/spring-security-jwt-auth";
        var credentials = new GitConnectionCredentials(url,null,null,null);

        // when
        List<Branch> branches = gitHubRepositoryReader.read(credentials);

        // then
        Assertions.assertFalse(branches.isEmpty(), "Branches should not be empty.");
        branches.forEach(branch ->
            Assertions.assertFalse(branch.getCommits().isEmpty(), "Commits for branch " + branch.getName() + " should not be empty.")
        );
    }

}
