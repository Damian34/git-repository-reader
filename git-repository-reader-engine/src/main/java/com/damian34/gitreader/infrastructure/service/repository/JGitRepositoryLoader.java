package com.damian34.gitreader.infrastructure.service.repository;

import com.damian34.gitreader.exception.GitRepositoryConnectionException;
import com.damian34.gitreader.exception.GitRepositoryException;
import com.damian34.gitreader.infrastructure.util.FileUtils;
import com.damian34.gitreader.model.queue.GitConnectionCredentials;
import com.damian34.gitreader.model.encryption.service.CredentialsEncryptionService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class JGitRepositoryLoader {

    @Value("${jgit.temp-root-folder}")
    private String tempRootFolder;
    
    private final CredentialsEncryptionService credentialsEncryptionService;

    @CircuitBreaker(name = "git-repository-loader")
    @Retry(name = "git-repository-loader")
    public JGitRepository loadRepository(GitConnectionCredentials credentials) {
        validateCredentials(credentials);
        GitConnectionCredentials decryptedCredentials = credentialsEncryptionService.decrypt(credentials);

        try {
            Path tempDir = FileUtils.createTempDirectory(tempRootFolder, UUID.randomUUID().toString());
            
            Git git = Git.cloneRepository()
                    .setURI(decryptedCredentials.url())
                    .setDirectory(tempDir.toFile())
                    .setCredentialsProvider(createCredentialsProvider(decryptedCredentials))
                    .call();
            
            return new JGitRepository(tempDir, git);
        } catch (TransportException e) {
            throw new GitRepositoryConnectionException("Failed to connect to Git repository: " + e.getMessage(), e);
        } catch (GitAPIException | IOException e) {
            throw new GitRepositoryException("An exception occurred while loading the repository: " + e.getMessage(), e);
        }
    }

    private void validateCredentials(GitConnectionCredentials credentials) {
        if (credentials.url() == null || credentials.url().isBlank()) {
            log.warn("Invalid Git repository URL provided: {}", credentials.url());
            throw new GitRepositoryException("Repository URL cannot be null or empty");
        }
    }

    private CredentialsProvider createCredentialsProvider(GitConnectionCredentials credentials) {
        if (credentials.username() == null && credentials.password() == null && credentials.token() == null) {
            return null;
        }
        
        if (credentials.token() != null) {
            return new UsernamePasswordCredentialsProvider("token", credentials.token());
        }
        
        return new UsernamePasswordCredentialsProvider(
                credentials.username() != null ? credentials.username() : "",
                credentials.password() != null ? credentials.password() : ""
        );
    }

}
