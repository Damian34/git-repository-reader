package com.damian34.gitreader.infrastructure.service.persistence;

import com.damian34.gitreader.domain.dto.GitRepositoryDto;
import com.damian34.gitreader.domain.persistence.GitRepositoryPersistenceService;
import com.damian34.gitreader.exception.NotFoundGitRepositoryException;
import com.damian34.gitreader.infrastructure.db.GitRepositoryDocumentRepository;
import com.damian34.gitreader.infrastructure.mapper.GitRepositoryMapper;
import com.damian34.gitreader.model.ProcessStatus;
import com.damian34.gitreader.model.document.GitRepositoryDocument;
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
    public GitRepositoryDto findGitRepository(String url) {
        var repository = gitRepositoryDocumentRepository.findById(url)
                .orElseThrow(() -> new NotFoundGitRepositoryException(url));
        return GitRepositoryMapper.INSTANCE.documentToDto(repository);
    }

    @Override
    public List<GitRepositoryDto> findAllGitRepositories() {
        List<GitRepositoryDocument> repositories = gitRepositoryDocumentRepository.findAll();
        return GitRepositoryMapper.INSTANCE.toDtoList(repositories);
    }

    @Override
    public void saveGitRepositoryWaiting(String url) {
        var document = gitRepositoryDocumentRepository.findById(url)
                .orElseGet(() -> new GitRepositoryDocument(url, null, null, null, null));
        document.setStatus(ProcessStatus.WAITING);
        log.info("Saving Waiting GitRepositoryDocument: {}", document);
        gitRepositoryDocumentRepository.save(document);
    }

    @Override
    public void cleanRepositories(String url, ProcessStatus skipStatus) {
        gitRepositoryDocumentRepository.deleteByUrlAndNotStatus(url, skipStatus);
    }
}
