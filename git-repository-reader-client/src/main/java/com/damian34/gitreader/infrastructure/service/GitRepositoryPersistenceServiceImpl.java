package com.damian34.gitreader.infrastructure.service;

import com.damian34.gitreader.domain.dto.GitRepositoryDto;
import com.damian34.gitreader.domain.GitRepositoryPersistenceService;
import com.damian34.gitreader.infrastructure.exception.NotFoundGitRepositoryException;
import com.damian34.gitreader.infrastructure.repository.GitRepositoryDocumentRepository;
import com.damian34.gitreader.infrastructure.mapper.GitRepositoryMapper;
import com.damian34.gitreader.ProcessStatus;
import com.damian34.gitreader.document.GitRepositoryDocument;
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
    public GitRepositoryDto find(String url) {
        var repository = gitRepositoryDocumentRepository.findById(url)
                .orElseThrow(() -> new NotFoundGitRepositoryException(url));
        return GitRepositoryMapper.INSTANCE.documentToDto(repository);
    }

    @Override
    public List<GitRepositoryDto> findAll() {
        List<GitRepositoryDocument> repositories = gitRepositoryDocumentRepository.findAll();
        return GitRepositoryMapper.INSTANCE.toDtoList(repositories);
    }

    @Override
    public void saveWaiting(String url) {
        var document = gitRepositoryDocumentRepository.findById(url)
                .orElseGet(() -> new GitRepositoryDocument(url, null, null, null, null));
        document.setStatus(ProcessStatus.WAITING);
        log.info("Saving Waiting GitRepositoryDocument: {}", document);
        gitRepositoryDocumentRepository.save(document);
    }

    @Override
    public void delete(String url) {
        gitRepositoryDocumentRepository.deleteById(url);
    }
}
