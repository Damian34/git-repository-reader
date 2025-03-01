package com.damian34.gitreader.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Commit {
    private String id;
    private String message;
    private String author;
    private String date;
}