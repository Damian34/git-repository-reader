package com.damian34.gitreader.exception;

public class GitRepositoryException extends GlobalException {

    public GitRepositoryException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public GitRepositoryException(String message) {
        super(message);
    }
}
