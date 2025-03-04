package com.damian34.gitreader.model.document;

import com.damian34.gitreader.model.ExceptionDetails;
import com.damian34.gitreader.model.ProcessStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GitStatusDocument {
    private String url;
    private ProcessStatus status;
    private ExceptionDetails exception;
}
