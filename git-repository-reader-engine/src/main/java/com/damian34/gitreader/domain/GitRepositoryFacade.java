package com.damian34.gitreader.domain;

import com.damian34.gitreader.infrastructure.exception.NotFoundGitReaderException;
import com.damian34.gitreader.queue.GitConnectionCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GitRepositoryFacade {
    private final List<GitRepositoryReader> gitRepositoryReaders;
    private final GitRepositoryPersistence gitRepositoryPersistence;
    private final GitRepositoryValidator gitRepositoryValidator;

    public Mono<Void> processRepositoryData(GitConnectionCredentials credentials) {
        return Mono.fromCallable(() -> {
                gitRepositoryValidator.validateCredentials(credentials);
                return findReader(credentials.url());
            })
            .flatMap(gitReader -> processAndSave(credentials, gitReader))
            .onErrorResume(e -> handleError(credentials.url(), null, e));
    }

    private Mono<Void> processAndSave(GitConnectionCredentials credentials, GitRepositoryReader reader) {
        String url = credentials.url();
        String gitCloneUrl = reader.buildGitCloneUrl(url);
        GitConnectionCredentials updatedCredentials = credentials.updateUrl(gitCloneUrl);
        
        return reader.fetchBranches(updatedCredentials)
                .collectList()
                .flatMap(branches ->
                        cleanRepositories(url, gitCloneUrl)
                                .then(gitRepositoryPersistence.saveGitBranches(url, gitCloneUrl, branches))
                )
                .onErrorResume(e -> handleError(url, gitCloneUrl, e))
                .then();
    }

    private Mono<Void> handleError(String url, String cloneUrl, Throwable e) {
        log.error("Error processing repository data for URL {}: {}", url, e.getMessage(), e);
        return cleanRepositories(url, cloneUrl)
            .then(gitRepositoryPersistence.saveGitFail(url, e));
    }

    private Mono<Void> cleanRepositories(String url, String cloneUrl) {
        return gitRepositoryPersistence.cleanGitRepository(url, cloneUrl);
    }

    private GitRepositoryReader findReader(String url) {
        return gitRepositoryReaders.stream()
                .filter(reader -> reader.isSupported(url))
                .findFirst()
                .orElseThrow(() -> new NotFoundGitReaderException(url));
    }
}
