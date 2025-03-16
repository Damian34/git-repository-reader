package com.damian34.gitreader.domain.service;

import com.damian34.gitreader.exception.GitRepositoryException;
import com.damian34.gitreader.model.queue.GitConnectionCredentials;
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
