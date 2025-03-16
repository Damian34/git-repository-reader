package com.damian34.gitreader.infrastructure.service.persistence;

import com.damian34.gitreader.domain.dto.GitRepositoryDto;
import com.damian34.gitreader.domain.persistence.GitRepositoryPersistenceService;
import com.damian34.gitreader.exception.NotFoundGitRepositoryException;
import com.damian34.gitreader.infrastructure.db.GitRepositoryDocumentRepository;
import com.damian34.gitreader.infrastructure.db.GitStatusRepository;
import com.damian34.gitreader.infrastructure.mapper.GitRepositoryMapper;
import com.damian34.gitreader.model.ProcessStatus;
import com.damian34.gitreader.model.document.GitRepositoryDocument;
import com.damian34.gitreader.model.document.GitStatusDocument;
import com.damian34.gitreader.model.repository.Branch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GitRepositoryPersistenceServiceImpl implements GitRepositoryPersistenceService {
    private final GitRepositoryDocumentRepository gitRepositoryDocumentRepository;
    private final GitStatusRepository gitStatusRepository;

    @Override
    public GitRepositoryDto findGitRepository(String url) {
        GitStatusDocument status = gitStatusRepository.findById(url).orElseThrow(() -> new NotFoundGitRepositoryException(url));
        var repository = gitRepositoryDocumentRepository.findById(url);
        List<Branch> branches = repository.map(GitRepositoryDocument::getBranches).orElse(null);
        return GitRepositoryMapper.INSTANCE.documentToDto(status, branches);
    }

    @Override
    public List<GitRepositoryDto> findAllGitRepositories() {
        List<GitStatusDocument> statuses = gitStatusRepository.findAll();
        List<GitRepositoryDocument> repositories = gitRepositoryDocumentRepository.findAll();
        Map<String, GitRepositoryDocument> repositoriesMap = repositories.stream()
                .collect(Collectors.toMap(GitRepositoryDocument::getUrl, Function.identity()));
        return statuses.stream()
                .map(status -> {
                    var repository = repositoriesMap.get(status.getUrl());
                    return GitRepositoryMapper.INSTANCE.documentToDto(status, repository.getBranches());
                }).toList();
    }

    @Override
    public void cleanRepositories(String url, ProcessStatus skipStatus) {
        gitStatusRepository.findById(url)
                .filter(it -> !Objects.equals(it.getStatus(), skipStatus))
                .ifPresent(status -> {
                    gitRepositoryDocumentRepository.deleteById(url);
                    gitStatusRepository.deleteById(url);
                });
    }
}
