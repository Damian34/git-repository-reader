package com.damian34.gitreader.exception;

import com.damian34.gitreader.model.exception.GlobalException;

public class GitRepositoryException extends GlobalException {

    public GitRepositoryException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public GitRepositoryException(String message) {
        super(message);
    }
}
