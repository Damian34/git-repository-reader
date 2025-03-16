package com.damian34.gitreader.domain.service.presistence;

public interface GitStatusPersistenceService {
    void saveGitStatusCompleted(String url);

    void saveGitStatusException(String url, Exception e);
}
