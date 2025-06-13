package com.damian34.gitreader.domain;

import com.damian34.gitreader.ProcessStatus;
import com.damian34.gitreader.domain.dto.GitRepositoryDto;
import com.damian34.gitreader.domain.exception.GitRepositoryDeleteException;
import com.damian34.gitreader.queue.GitConnectionCredentials;
import com.damian34.gitreader.encryption.CredentialsEncryptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class GitRepositoryClientFacade {
    private final CredentialsSender credentialsSender;
    private final GitRepositoryPersistenceService gitRepositoryPersistenceService;
    private final CredentialsEncryptionService credentialsEncryptionService;

    public void sendGitRepositoryToLoad(GitConnectionCredentials credentials) {
        GitConnectionCredentials encryptedCredentials = credentialsEncryptionService.encrypt(credentials);
        credentialsSender.send(encryptedCredentials);
        gitRepositoryPersistenceService.saveWaiting(credentials.url());
    }

    public GitRepositoryDto findGitRepository(String url) {
        return gitRepositoryPersistenceService.find(url);
    }

    public List<GitRepositoryDto> findAllGitRepositories() {
        return gitRepositoryPersistenceService.findAll();
    }

    public void deleteGitRepository(String url) {
        GitRepositoryDto dto = findGitRepository(url);
        if(Objects.equals(dto.getStatus(), ProcessStatus.WAITING)) {
            throw new GitRepositoryDeleteException(url, ProcessStatus.WAITING);
        }
        gitRepositoryPersistenceService.delete(url);
    }
}
