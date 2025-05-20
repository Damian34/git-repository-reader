package com.damian34.gitreader.infrastructure.db;

import com.damian34.gitreader.model.ProcessStatus;
import com.damian34.gitreader.model.document.GitRepositoryDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface GitRepositoryDocumentRepository extends MongoRepository<GitRepositoryDocument, String> {
    @Query("{ 'url': ?0, 'status': { $ne: ?1 } }")
    void deleteByUrlAndNotStatus(String url, ProcessStatus status);
}
