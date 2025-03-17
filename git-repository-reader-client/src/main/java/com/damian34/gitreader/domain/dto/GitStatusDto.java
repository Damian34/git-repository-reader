package com.damian34.gitreader.domain.dto;

import com.damian34.gitreader.model.ExceptionDetails;
import com.damian34.gitreader.model.ProcessStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GitStatusDto {
    private String url;
    private ProcessStatus status;
    private ExceptionDetails exception;
}
