package com.damian34.gitreader.domain.service;

import com.damian34.gitreader.model.repository.Branch;

import java.util.List;

public interface GitPersistenceService {
    void saveGitBranches(String url, List<Branch> branches);

    void saveGitStatusCompleted(String url);

    void saveGitStatusException(String url, Exception e);
}
