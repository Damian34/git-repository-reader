package com.damian34.gitreader.infrastructure.service.persistence;

import com.damian34.gitreader.domain.service.presistence.GitStatusPersistenceService;
import com.damian34.gitreader.infrastructure.db.GitStatusRepository;
import com.damian34.gitreader.model.ExceptionDetails;
import com.damian34.gitreader.model.ProcessStatus;
import com.damian34.gitreader.model.document.GitStatusDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GitStatusPersistenceServiceImpl implements GitStatusPersistenceService {
    private final GitStatusRepository gitStatusRepository;

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
        gitStatusRepository.save(document);
    }
}
