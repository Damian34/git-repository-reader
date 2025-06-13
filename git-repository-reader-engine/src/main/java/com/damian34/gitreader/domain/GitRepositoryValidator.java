package com.damian34.gitreader.domain;

import com.damian34.gitreader.infrastructure.exception.GitRepositoryException;
import com.damian34.gitreader.queue.GitConnectionCredentials;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class GitRepositoryValidator {

    public void validateCredentials(GitConnectionCredentials credentials) {
        if (StringUtils.isBlank(credentials.url())) {
            throw new GitRepositoryException("Url can't be blank at message: " + credentials);
        }
    }
}
