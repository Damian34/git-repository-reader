package com.damian34.gitreader;

import com.damian34.gitreader.domain.model.Branch;

import java.util.List;

@Deprecated(since = "Class only to debug. Potentially will be removed later", forRemoval = true)
public class GitRepositoryHelperTest {

    public static void printBranches(List<Branch> branches, String url) {
        System.out.println("repository: " + url);
        branches.forEach(branch -> {
            System.out.println("------");
            System.out.println("branch: " + branch.getName());
            System.out.println("commits: ");
            branch.getCommits().forEach(commit -> {
                System.out.println(commit.getId() + " | " + commit.getMessage());
            });
        });
    }
}
