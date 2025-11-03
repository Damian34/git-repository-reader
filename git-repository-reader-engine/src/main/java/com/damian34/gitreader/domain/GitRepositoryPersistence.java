package com.damian34.gitreader.domain;

import com.damian34.gitreader.repository.Branch;
import reactor.core.publisher.Mono;

import java.util.List;

public interface GitRepositoryPersistence {

    Mono<Void> cleanGitRepository(String url, String cloneUrl);

    Mono<Void> saveGitBranches(String url, String cloneUrl, List<Branch> branches);

    Mono<Void> saveGitFail(String url, Throwable e);
}
