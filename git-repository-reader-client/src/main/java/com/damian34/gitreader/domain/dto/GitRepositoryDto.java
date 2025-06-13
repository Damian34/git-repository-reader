package com.damian34.gitreader.domain.dto;

import com.damian34.gitreader.ExceptionDetails;
import com.damian34.gitreader.ProcessStatus;
import com.damian34.gitreader.repository.Branch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GitRepositoryDto {
    private String url;
    private ProcessStatus status;
    private List<Branch> branches;
    private ExceptionDetails exception;
}
