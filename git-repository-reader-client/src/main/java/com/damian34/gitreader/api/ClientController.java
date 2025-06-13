package com.damian34.gitreader.api;

import com.damian34.gitreader.api.protocol.mapper.GitProtocolMapper;
import com.damian34.gitreader.api.protocol.reqeust.GitCredentialsRequest;
import com.damian34.gitreader.api.protocol.reqeust.GitUrlRequest;
import com.damian34.gitreader.api.protocol.response.GitRepositoryResponse;
import com.damian34.gitreader.domain.GitRepositoryClientFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/git/repository")
@RequiredArgsConstructor
@Tag(name = "Git Repository Client", description = "API for managing Git repositories")
public class ClientController {
    private final GitRepositoryClientFacade gitRepositoryClientFacade;
    private final GitProtocolMapper repositoryMapper;

    @PostMapping("/load")
    @Operation(summary = "Send Git repository for processing", description = "Sends Git repository credentials to be processed by the system")
    public ResponseEntity<String> sendGitRepositoryToLoad(@Valid @RequestBody GitCredentialsRequest request) {
        repositoryMapper.mapCredentials(request.getCredentials())
                .forEach(gitRepositoryClientFacade::sendGitRepositoryToLoad);
        return ResponseEntity.ok("Git credentials request was successfully handled");
    }

    @GetMapping
    @Operation(summary = "Get Git repository", description = "Retrieves information about a specific Git repository")
    public ResponseEntity<GitRepositoryResponse> getGitRepository(@Valid @RequestBody GitUrlRequest request) {
        return ResponseEntity.ok(repositoryMapper.toResponse(
            gitRepositoryClientFacade.findGitRepository(request.getUrl())
        ));
    }

    @GetMapping("/all")
    @Operation(summary = "Get all Git repositories", description = "Retrieves information about all processed Git repositories")
    public ResponseEntity<List<GitRepositoryResponse>> getAllGitRepository() {
        return ResponseEntity.ok(repositoryMapper.toResponseList(
            gitRepositoryClientFacade.findAllGitRepositories()
        ));
    }

    @DeleteMapping
    @Operation(summary = "Delete Git repository", description = "Delete Git repository when not processed")
    public void deleteGitRepository(@Valid @RequestBody GitUrlRequest request) {
        gitRepositoryClientFacade.deleteGitRepository(request.getUrl());
    }
}
