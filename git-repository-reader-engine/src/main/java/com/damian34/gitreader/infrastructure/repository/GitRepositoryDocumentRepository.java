package com.damian34.gitreader.infrastructure.repository;

import com.damian34.gitreader.document.GitRepositoryDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface GitRepositoryDocumentRepository extends ReactiveMongoRepository<GitRepositoryDocument, String> {
    Mono<Void> deleteByCloneUrl(String cloneUrl);
}
