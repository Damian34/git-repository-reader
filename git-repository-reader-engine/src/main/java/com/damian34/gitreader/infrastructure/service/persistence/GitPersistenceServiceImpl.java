package com.damian34.gitreader.infrastructure.service.persistence;

import com.damian34.gitreader.domain.service.GitPersistenceService;
import com.damian34.gitreader.model.ExceptionDetails;
import com.damian34.gitreader.model.ProcessStatus;
import com.damian34.gitreader.model.document.GitRepositoryDocument;
import com.damian34.gitreader.model.document.GitStatusDocument;
import com.damian34.gitreader.model.repository.Branch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GitPersistenceServiceImpl implements GitPersistenceService {

    @Override
    public void saveGitBranches(String url, List<Branch> branches) {
        var document = new GitRepositoryDocument(url, branches);
        log.info("Saving GitRepositoryDocument: {}", document);
        // TODO: add save to mongo
    }

    @Override
    public void saveGitStatusCompleted(String url) {
        var document = new GitStatusDocument(url, ProcessStatus.COMPLETED, null);
        saveGitStatus(document);
    }

    @Override
    public void saveGitStatusException(String url, Exception e) {
        var error = new ExceptionDetails(e.getClass().getSimpleName(), e.getMessage());
        var document = new GitStatusDocument(url, ProcessStatus.EXCEPTION, error);
        saveGitStatus(document);
    }

    private void saveGitStatus(GitStatusDocument document) {
        log.info("Saving GitStatusDocument: {}", document);
        // TODO: add save to mongo
    }
}
