package com.damian34.gitreader.model.document;

import com.damian34.gitreader.model.repository.Branch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("git_repositories")
public class GitRepositoryDocument {
    @Id
    private String url;
    private List<Branch> branches;
}
