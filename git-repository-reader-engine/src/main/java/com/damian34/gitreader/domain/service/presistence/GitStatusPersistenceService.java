package com.damian34.gitreader.domain.service.presistence;

public interface GitStatusPersistenceService {
    void cleanGitStatus(String url, String cloneUrl);

    void saveGitStatusCompleted(String url, String cloneUrl);

    void saveGitStatusFailed(String url, Exception e);
}
