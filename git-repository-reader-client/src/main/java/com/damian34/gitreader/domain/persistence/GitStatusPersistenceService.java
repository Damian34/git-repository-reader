package com.damian34.gitreader.domain.persistence;

import com.damian34.gitreader.domain.dto.GitStatusDto;

public interface GitStatusPersistenceService {
    GitStatusDto findGitStatus(String url);

    void saveGitStatusWaiting(String url);
}
