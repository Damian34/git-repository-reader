package com.damian34.gitreader.infrastructure.service;

import com.damian34.gitreader.domain.GitRepositoryPersistence;
import com.damian34.gitreader.infrastructure.repository.GitRepositoryDocumentRepository;
import com.damian34.gitreader.ExceptionDetails;
import com.damian34.gitreader.ProcessStatus;
import com.damian34.gitreader.document.GitRepositoryDocument;
import com.damian34.gitreader.repository.Branch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GitRepositoryPersistenceDefault implements GitRepositoryPersistence {
    private final GitRepositoryDocumentRepository gitRepositoryDocumentRepository;

    @Override
    public Mono<Void> cleanGitRepository(String url, String cloneUrl) {
        if(cloneUrl != null) {
            return gitRepositoryDocumentRepository.deleteByCloneUrl(cloneUrl);
        } else {
            return gitRepositoryDocumentRepository.deleteById(url);
        }
    }

    @Override
    public Mono<Void> saveGitBranches(String url, String cloneUrl, List<Branch> branches) {
        var document = new GitRepositoryDocument(url, cloneUrl, ProcessStatus.COMPLETED, null, branches);
        log.info("Saving Completed GitRepositoryDocument with url: {}", document.getCloneUrl());
        return gitRepositoryDocumentRepository.save(document).then();
    }

    @Override
    public Mono<Void> saveGitFail(String url, Throwable e) {
        var error = new ExceptionDetails(e.getClass().getSimpleName(), e.getMessage());
        var document = new GitRepositoryDocument(url, null, ProcessStatus.FAILED, error, null);
        log.info("Saving Failed GitRepositoryDocument with url: {}", document.getUrl());
        return gitRepositoryDocumentRepository.save(document).then();
    }
}
