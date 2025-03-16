package com.damian34.gitreader.infrastructure.service.persistence;

import com.damian34.gitreader.domain.service.presistence.GitRepositoryPersistenceService;
import com.damian34.gitreader.infrastructure.db.GitRepositoryDocumentRepository;
import com.damian34.gitreader.model.document.GitRepositoryDocument;
import com.damian34.gitreader.model.repository.Branch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GitRepositoryPersistenceServiceImpl implements GitRepositoryPersistenceService {
    private final GitRepositoryDocumentRepository gitRepositoryDocumentRepository;

    @Override
    public void cleanGitRepository(String url) {
        gitRepositoryDocumentRepository.deleteById(url);
    }

    @Override
    public void saveGitBranches(String url, List<Branch> branches) {
        var document = new GitRepositoryDocument(url, branches);
        log.info("Saving GitRepositoryDocument: {}", document);
        gitRepositoryDocumentRepository.save(document);
    }
}
