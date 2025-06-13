package com.damian34.gitreader.infrastructure.repository;

import com.damian34.gitreader.document.GitRepositoryDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GitRepositoryDocumentRepository extends MongoRepository<GitRepositoryDocument, String> {
}
