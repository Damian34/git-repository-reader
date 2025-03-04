package com.damian34.gitreader.domain.service;

import com.damian34.gitreader.exception.NotFoundGitReaderException;
import com.damian34.gitreader.model.queue.GitConnectionCredentials;
import com.damian34.gitreader.model.repository.Branch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GitRepositoryFacade {
    private final List<GitRepositoryReader> gitRepositoryReaders;
    private final GitPersistenceService gitPersistenceService;

    public void processRepositoryData(GitConnectionCredentials credentials) {
        try {
            List<Branch> branches = findBranches(credentials);
            gitPersistenceService.saveGitBranches(credentials.url(), branches);
            gitPersistenceService.saveGitStatusCompleted(credentials.url());
        } catch (Exception e) {
            gitPersistenceService.saveGitStatusException(credentials.url(), e);
        }
    }

    private List<Branch> findBranches(GitConnectionCredentials credentials) {
        return gitRepositoryReaders.stream()
                .filter(reader -> reader.isSupported(credentials.url()))
                .findFirst()
                .orElseThrow(() -> new NotFoundGitReaderException(credentials.url()))
                .fetchBranches(credentials);
    }

}
