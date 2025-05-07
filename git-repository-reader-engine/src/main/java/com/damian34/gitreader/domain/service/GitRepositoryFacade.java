package com.damian34.gitreader.domain.service;

import com.damian34.gitreader.domain.service.presistence.GitRepositoryPersistenceService;
import com.damian34.gitreader.domain.service.presistence.GitStatusPersistenceService;
import com.damian34.gitreader.exception.NotFoundGitReaderException;
import com.damian34.gitreader.model.exception.GlobalException;
import com.damian34.gitreader.model.queue.GitConnectionCredentials;
import com.damian34.gitreader.model.repository.Branch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GitRepositoryFacade {
    private final List<GitRepositoryReader> gitRepositoryReaders;
    private final GitRepositoryPersistenceService gitRepositoryPersistenceService;
    private final GitStatusPersistenceService gitStatusPersistenceService;
    private final GitRepositoryValidator gitRepositoryValidator;

    public void processRepositoryData(GitConnectionCredentials credentials) {
        try{
            gitRepositoryValidator.validateCredentials(credentials);
            var gitReader = findReader(credentials.url());
            var gitCloneUrl = gitReader.buildGitCloneUrl(credentials.url());
            var updatedCredentials = credentials.updateUrl(gitCloneUrl);
            List<Branch> branches = gitReader.fetchBranches(updatedCredentials);
            clean(credentials.url(), gitCloneUrl);
            gitRepositoryPersistenceService.saveGitBranches(credentials.url(), gitCloneUrl, branches);
            gitStatusPersistenceService.saveGitStatusCompleted(credentials.url(), gitCloneUrl);
        } catch (GlobalException e) {
            onFail(credentials.url(), e);
        } catch(Exception e) {
            log.error("Unexpected error occurred for git repository URL {}: ", credentials.url(), e);
            onFail(credentials.url(), e);
        }
    }

    private void onFail(String url, Exception e) {
        clean(url, null);
        gitStatusPersistenceService.saveGitStatusFailed(url, e);
    }

    private void clean(String url, String cloneUrl) {
        gitRepositoryPersistenceService.cleanGitRepository(url, cloneUrl);
        gitStatusPersistenceService.cleanGitStatus(url, cloneUrl);
    }

    private GitRepositoryReader findReader(String url) {
        return gitRepositoryReaders.stream()
                .filter(reader -> reader.isSupported(url))
                .findFirst()
                .orElseThrow(() -> new NotFoundGitReaderException(url));
    }

}
