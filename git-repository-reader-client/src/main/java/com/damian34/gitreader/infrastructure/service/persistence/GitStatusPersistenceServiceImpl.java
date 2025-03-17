package com.damian34.gitreader.infrastructure.service.persistence;

import com.damian34.gitreader.domain.dto.GitStatusDto;
import com.damian34.gitreader.domain.persistence.GitStatusPersistenceService;
import com.damian34.gitreader.exception.NotFoundGitRepositoryException;
import com.damian34.gitreader.infrastructure.db.GitStatusRepository;
import com.damian34.gitreader.infrastructure.mapper.GitStatusMapper;
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
    public GitStatusDto findGitStatus(String url) {
        GitStatusDocument status = gitStatusRepository.findById(url).orElseThrow(() -> new NotFoundGitRepositoryException(url));
        return GitStatusMapper.INSTANCE.documentToDto(status);
    }

    @Override
    public void saveGitStatusWaiting(String url) {
        var document = new GitStatusDocument(url, null, ProcessStatus.WAITING, null);
        log.info("Saving GitStatusDocument: {}", document);
        gitStatusRepository.save(document);
    }
}
