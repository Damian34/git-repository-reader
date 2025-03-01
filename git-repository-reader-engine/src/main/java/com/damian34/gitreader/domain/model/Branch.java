package com.damian34.gitreader.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Branch {
    private String id;
    private String name;
    private List<Commit> commits;
}
