package com.damian34.gitreader.model.document;

import com.damian34.gitreader.model.repository.Branch;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Data
@AllArgsConstructor
@Document("git_repositories")
public class GitRepositoryDocument {
    @MongoId
    private String url;
    private List<Branch> branches;
}
