package com.damian34.gitreader.domain.service.presistence;

import com.damian34.gitreader.model.repository.Branch;

import java.util.List;

public interface GitRepositoryPersistenceService {

    void cleanGitRepository(String url);

    void saveGitBranches(String url, List<Branch> branches);
}
