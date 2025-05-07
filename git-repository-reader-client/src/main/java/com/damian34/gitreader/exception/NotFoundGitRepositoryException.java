package com.damian34.gitreader.exception;

import com.damian34.gitreader.model.exception.GlobalException;

import java.text.MessageFormat;

public class NotFoundGitRepositoryException extends GlobalException {

    public NotFoundGitRepositoryException(String url) {
        super(MessageFormat.format("Not found Git Repository with url: {0}", url));
    }
}
