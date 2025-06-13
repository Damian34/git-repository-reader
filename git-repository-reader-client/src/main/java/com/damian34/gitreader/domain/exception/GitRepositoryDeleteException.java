package com.damian34.gitreader.domain.exception;

import com.damian34.gitreader.ProcessStatus;
import com.damian34.gitreader.exception.GlobalException;

import java.text.MessageFormat;

public class GitRepositoryDeleteException extends GlobalException {

    public GitRepositoryDeleteException(String url, ProcessStatus status) {
        super(MessageFormat.format("Can''t remove Git Repository {0} with status {1}", url, status));
    }
}
