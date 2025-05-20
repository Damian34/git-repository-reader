package com.damian34.gitreader.domain.persistence;

import com.damian34.gitreader.domain.dto.GitRepositoryDto;
import com.damian34.gitreader.model.ProcessStatus;

import java.util.List;

public interface GitRepositoryPersistenceService {
    GitRepositoryDto findGitRepository(String url);

    List<GitRepositoryDto> findAllGitRepositories();

    void saveGitRepositoryWaiting(String url);

    void cleanRepositories(String url, ProcessStatus skipStatus);
}
