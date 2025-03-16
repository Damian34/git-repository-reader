package com.damian34.gitreader.exception;

import java.text.MessageFormat;

public class NotFoundGitReaderException extends GlobalException {

    public NotFoundGitReaderException(String url) {
        super(MessageFormat.format("Not found supported Git Reader for url: {0}", url));
    }
}
