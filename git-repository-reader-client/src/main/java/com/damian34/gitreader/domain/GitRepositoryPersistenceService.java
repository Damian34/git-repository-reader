package com.damian34.gitreader.domain;

import com.damian34.gitreader.domain.dto.GitRepositoryDto;

import java.util.List;

public interface GitRepositoryPersistenceService {
    GitRepositoryDto find(String url);

    List<GitRepositoryDto> findAll();

    void saveWaiting(String url);

    void delete(String url);
}
