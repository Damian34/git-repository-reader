package com.damian34.gitreader.domain;


import com.damian34.gitreader.queue.GitConnectionCredentials;
import com.damian34.gitreader.repository.Branch;
import reactor.core.publisher.Flux;

public interface GitRepositoryReader {

    boolean isSupported(String url);

    String buildGitCloneUrl(String url);

    Flux<Branch> fetchBranches(GitConnectionCredentials credentials);
}
