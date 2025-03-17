package com.damian34.gitreader.infrastructure.db;

import com.damian34.gitreader.model.document.GitRepositoryDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GitRepositoryDocumentRepository extends MongoRepository<GitRepositoryDocument, String> {
}
