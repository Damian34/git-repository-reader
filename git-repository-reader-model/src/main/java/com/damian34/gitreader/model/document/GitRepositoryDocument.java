package com.damian34.gitreader.model.document;

import com.damian34.gitreader.model.repository.Branch;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GitRepositoryDocument {
    private String url;
    private List<Branch> branches;
}
