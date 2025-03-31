package com.damian34.gitreader.api;

import com.damian34.gitreader.api.protocol.mapper.GitCredentialsMapper;
import com.damian34.gitreader.api.protocol.reqeust.GitCredentialsRequest;
import com.damian34.gitreader.api.protocol.reqeust.GitUrlRequest;
import com.damian34.gitreader.domain.GitRepositoryClientFacade;
import com.damian34.gitreader.domain.dto.GitRepositoryDto;
import com.damian34.gitreader.domain.dto.GitStatusDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/git/repository")
@RequiredArgsConstructor
public class ClientController {
    private final GitRepositoryClientFacade gitRepositoryClientFacade;

    @PostMapping("/load")
    public ResponseEntity<String> sendGitRepositoryToLoad(@Valid @RequestBody GitCredentialsRequest request) {
        GitCredentialsMapper.INSTANCE.mapCredentials(request.getCredentials())
                .forEach(gitRepositoryClientFacade::sendGitRepositoryToLoad);
        return ResponseEntity.ok("Git credentials request was successfully handled");
    }

    @GetMapping("/status")
    public ResponseEntity<GitStatusDto> getStatus(@Valid @RequestBody GitUrlRequest request) {
        GitStatusDto status = gitRepositoryClientFacade.findGitStatus(request.getUrl());
        return ResponseEntity.ok(status);
    }

    @GetMapping
    public ResponseEntity<GitRepositoryDto> getGitRepository(@Valid @RequestBody GitUrlRequest request) {
        GitRepositoryDto repository = gitRepositoryClientFacade.findGitRepository(request.getUrl());
        return ResponseEntity.ok(repository);
    }

    @GetMapping("/all")
    public ResponseEntity<List<GitRepositoryDto>> getAllGitRepository() {
        List<GitRepositoryDto> repositories = gitRepositoryClientFacade.findAllGitRepositories();
        return ResponseEntity.ok(repositories);
    }
}
