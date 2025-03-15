package com.damian34.gitreader.domain.service;

import com.damian34.gitreader.domain.service.presistence.GitRepositoryPersistenceService;
import com.damian34.gitreader.domain.service.presistence.GitStatusPersistenceService;
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
    private final GitRepositoryPersistenceService gitRepositoryPersistenceService;
    private final GitStatusPersistenceService gitStatusPersistenceService;
    private final GitRepositoryValidator gitRepositoryValidator;

    public void processRepositoryData(GitConnectionCredentials credentials) {
        try {
            gitRepositoryValidator.validateCredentials(credentials);
            var gitReader = findReader(credentials.url());
            var gitCloneUrl = gitReader.buildGitCloneUrl(credentials.url());
            var updatedCredentials = credentials.updateUrl(gitCloneUrl);
            gitRepositoryPersistenceService.cleanGitRepository(credentials.url());
            List<Branch> branches = gitReader.fetchBranches(updatedCredentials);
            gitRepositoryPersistenceService.saveGitBranches(updatedCredentials.url(), branches);
            gitStatusPersistenceService.saveGitStatusCompleted(updatedCredentials.url());
        } catch (Exception e) {
            gitStatusPersistenceService.saveGitStatusException(credentials.url(), e);
        }
    }

    private GitRepositoryReader findReader(String url) {
        return gitRepositoryReaders.stream()
                .filter(reader -> reader.isSupported(url))
                .findFirst()
                .orElseThrow(() -> new NotFoundGitReaderException(url));
    }

}
