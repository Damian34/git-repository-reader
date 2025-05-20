package com.damian34.gitreader.infrastructure.db;

import com.damian34.gitreader.model.document.GitRepositoryDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface GitRepositoryDocumentRepository extends ReactiveMongoRepository<GitRepositoryDocument, String> {
    Mono<Void> deleteByCloneUrl(String cloneUrl);
}
