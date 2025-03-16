package com.damian34.gitreader.infrastructure.db;

import com.damian34.gitreader.model.document.GitStatusDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GitStatusRepository extends MongoRepository<GitStatusDocument, String> {
}
