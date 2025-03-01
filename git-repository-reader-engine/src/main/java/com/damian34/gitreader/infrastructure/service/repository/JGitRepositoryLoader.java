package com.damian34.gitreader.infrastructure.service.repository;

import com.damian34.gitreader.domain.service.vo.GitConnectionCredentials;
import com.damian34.gitreader.exception.GitRepositoryConnectionException;
import com.damian34.gitreader.exception.GitRepositoryException;
import com.damian34.gitreader.infrastructure.util.FileUtils;
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

    // TODO: add circusBreaker
    public JGitRepository loadRepository(GitConnectionCredentials credentials) {
        validateCredentials(credentials);
        Path tempRepositoryDir = null;

        try {
            tempRepositoryDir = FileUtils.createTempDirectory(tempRootFolder, UUID.randomUUID().toString());
            var git = Git.cloneRepository()
                    .setURI(credentials.url())
                    .setDirectory(tempRepositoryDir.toFile())
                    .setCredentialsProvider(createCredentialsProvider(credentials))
                    .call();
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

    private void validateCredentials(GitConnectionCredentials credentials) {
        if (credentials.url() == null || credentials.url().isBlank()) {
            log.warn("Invalid Git repository URL provided: {}", credentials.url());
            throw new GitRepositoryException("Repository URL cannot be null or empty");
        }
    }

    private CredentialsProvider createCredentialsProvider(GitConnectionCredentials credentials) {
        if (credentials.token() != null) {
            return new UsernamePasswordCredentialsProvider("git", credentials.token());
        }
        if (credentials.username() != null && credentials.password() != null) {
            return new UsernamePasswordCredentialsProvider(credentials.username(), credentials.password());
        }
        return CredentialsProvider.getDefault();
    }

}
