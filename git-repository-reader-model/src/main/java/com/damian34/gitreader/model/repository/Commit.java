package com.damian34.gitreader.model.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Commit {
    private String id;
    private String message;
    private String author;
    private String date;
}