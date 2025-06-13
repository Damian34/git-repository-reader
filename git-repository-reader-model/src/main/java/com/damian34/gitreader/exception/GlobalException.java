package com.damian34.gitreader.exception;

public class GlobalException extends RuntimeException {

    public GlobalException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public GlobalException(String message) {
        super(message);
    }
}