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
    public void cleanGitStatus(String url, String cloneUrl) {
        if(cloneUrl != null) {
            gitStatusRepository.deleteByCloneUrl(cloneUrl);
        } else {
            gitStatusRepository.deleteById(url);
        }
    }

    @Override
    public void saveGitStatusCompleted(String url, String cloneUrl) {
        var document = new GitStatusDocument(url, cloneUrl, ProcessStatus.COMPLETED, null);
        saveGitStatus(document);
    }

    @Override
    public void saveGitStatusFailed(String url, Exception e) {
        var error = new ExceptionDetails(e.getClass().getSimpleName(), e.getMessage());
        var document = new GitStatusDocument(url, null, ProcessStatus.FAILED, error);
        saveGitStatus(document);
    }

    private void saveGitStatus(GitStatusDocument document) {
        log.info("Saving GitStatusDocument with url: {}", document.getUrl());
        gitStatusRepository.save(document);
    }
}
