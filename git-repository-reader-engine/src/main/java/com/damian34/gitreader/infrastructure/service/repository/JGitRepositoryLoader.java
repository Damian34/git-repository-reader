package com.damian34.gitreader.infrastructure.service.repository;

import com.damian34.gitreader.exception.GitRepositoryConnectionException;
import com.damian34.gitreader.exception.GitRepositoryException;
import com.damian34.gitreader.infrastructure.util.FileUtils;
import com.damian34.gitreader.model.queue.GitConnectionCredentials;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.UUID;

@Slf4j
@Service
public class JGitRepositoryLoader {

    @Value("${jgit.temp-root-folder}")
    private String tempRootFolder;

    @CircuitBreaker(name = "git-repository-loader")
    @Retry(name = "git-repository-loader")
    public JGitRepository loadRepository(GitConnectionCredentials credentials) {
        validateCredentials(credentials);
        Path tempRepositoryDir = null;
        try {
            tempRepositoryDir = FileUtils.createTempDirectory(tempRootFolder, UUID.randomUUID().toString());
            var git = cloneRepository(credentials.url(), tempRepositoryDir, createCredentialsProvider(credentials));
            return new JGitRepository(tempRepositoryDir, git);
        } catch (TransportException e) {
            log.error("Failed to connect to the Git repository with credentials: {}", credentials, e);
            FileUtils.deleteDirectory(tempRepositoryDir);
            throw new GitRepositoryConnectionException("Failed to connect to the Git repository with credentials: " + credentials, e);
        } catch (Exception e) {
            log.error("An exception occurred while trying to download the Git repository.", e);
            FileUtils.deleteDirectory(tempRepositoryDir);
            throw new GitRepositoryException("An exception occurred while trying to download the Git repository.", e);
        }
    }

    private Git cloneRepository(String url, Path tempDirectory, CredentialsProvider credentialsProvider) throws Exception {
        return Git.cloneRepository()
                .setURI(url)
                .setDirectory(tempDirectory.toFile())
                .setCredentialsProvider(credentialsProvider)
                .call();
    }

    private void validateCredentials(GitConnectionCredentials credentials) {
        if (credentials.url() == null || credentials.url().isBlank()) {
            log.warn("Invalid Git repository URL provided: {}", credentials.url());
            throw new GitRepositoryException("Repository URL cannot be null or empty");
        }
    }

    private CredentialsProvider createCredentialsProvider(GitConnectionCredentials credentials) {
        if (StringUtils.isNotBlank(credentials.token())) {
            return new UsernamePasswordCredentialsProvider("git", credentials.token());
        }
        if (StringUtils.isNotBlank(credentials.username()) && StringUtils.isNotBlank(credentials.password())) {
            return new UsernamePasswordCredentialsProvider(credentials.username(), credentials.password());
        }
        return CredentialsProvider.getDefault();
    }

}
