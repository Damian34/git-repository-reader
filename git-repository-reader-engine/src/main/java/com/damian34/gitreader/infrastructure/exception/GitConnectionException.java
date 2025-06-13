package com.damian34.gitreader.infrastructure.exception;

import com.damian34.gitreader.exception.GlobalException;

public class GitConnectionException extends GlobalException {

    public GitConnectionException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
