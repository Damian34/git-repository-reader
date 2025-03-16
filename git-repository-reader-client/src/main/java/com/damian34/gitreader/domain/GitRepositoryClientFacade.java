package com.damian34.gitreader.domain;

import com.damian34.gitreader.domain.dto.GitRepositoryDto;
import com.damian34.gitreader.domain.dto.GitStatusDto;
import com.damian34.gitreader.domain.persistence.GitRepositoryPersistenceService;
import com.damian34.gitreader.domain.persistence.GitStatusPersistenceService;
import com.damian34.gitreader.model.ProcessStatus;
import com.damian34.gitreader.model.queue.GitConnectionCredentials;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GitRepositoryClientFacade {
    private final CredentialsMessageSender credentialsMessageSender;
    private final GitRepositoryPersistenceService gitRepositoryPersistenceService;
    private final GitStatusPersistenceService gitStatusPersistenceService;

    public void sendGitRepositoryToLoad(GitConnectionCredentials credentials) {
        gitRepositoryPersistenceService.cleanRepositories(credentials.url(), ProcessStatus.WAITING);
        credentialsMessageSender.sendMessage(credentials);
        gitStatusPersistenceService.saveGitStatusWaiting(credentials.url());
    }

    public GitStatusDto findGitStatus(String url) {
        return gitStatusPersistenceService.findGitStatus(url);
    }

    public GitRepositoryDto findGitRepository(String url) {
        return gitRepositoryPersistenceService.findGitRepository(url);
    }

    public List<GitRepositoryDto> findAllGitRepositories() {
        return gitRepositoryPersistenceService.findAllGitRepositories();
    }
}
