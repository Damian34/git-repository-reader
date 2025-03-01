package com.damian34.gitreader.exception;

public class GitRepositoryConnectionException extends GitRepositoryException{

    public GitRepositoryConnectionException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
