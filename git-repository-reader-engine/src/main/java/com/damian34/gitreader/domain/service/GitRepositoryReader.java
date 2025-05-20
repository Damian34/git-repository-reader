package com.damian34.gitreader.domain.service;


import com.damian34.gitreader.model.queue.GitConnectionCredentials;
import com.damian34.gitreader.model.repository.Branch;
import reactor.core.publisher.Flux;

public interface GitRepositoryReader {

    boolean isSupported(String url);

    String buildGitCloneUrl(String url);

    Flux<Branch> fetchBranches(GitConnectionCredentials credentials);
}
