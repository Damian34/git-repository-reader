package com.damian34.gitreader.infrastructure.service.repository;

import com.damian34.gitreader.exception.GitRepositoryConnectionException;
import com.damian34.gitreader.exception.GitRepositoryException;
import com.damian34.gitreader.infrastructure.util.FileUtils;
import com.damian34.gitreader.model.queue.GitConnectionCredentials;
import com.damian34.gitreader.model.encryption.service.CredentialsEncryptionService;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import io.github.resilience4j.reactor.retry.RetryOperator;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

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
    private final CircuitBreakerRegistry circuitBreakerRegistry;
    private final RetryRegistry retryRegistry;
    
    private static final String CIRCUIT_BREAKER_NAME = "git-repository-loader";
    private static final String RETRY_NAME = "git-repository-loader";

    public Mono<JGitRepository> loadRepository(GitConnectionCredentials credentials) {
        if (StringUtils.isBlank(credentials.url())) {
            log.warn("Invalid Git repository URL provided: {}", credentials.url());
            return Mono.error(new GitRepositoryException("Repository URL cannot be null or empty"));
        }
        
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(CIRCUIT_BREAKER_NAME);
        Retry retry = retryRegistry.retry(RETRY_NAME);

        return Mono.just(credentials)
                .map(credentialsEncryptionService::decrypt)
                .flatMap(decryptedCredentials -> 
                    Mono.fromCallable(() -> processLoadRepository(decryptedCredentials))
                        .subscribeOn(Schedulers.boundedElastic())
                )
                .transform(RetryOperator.of(retry))
                .transform(CircuitBreakerOperator.of(circuitBreaker));
    }

    private JGitRepository processLoadRepository(GitConnectionCredentials decryptedCredentials) {
        try {
            Path tempDir = FileUtils.createTempDirectory(tempRootFolder, UUID.randomUUID().toString());

            Git git = Git.cloneRepository()
                    .setURI(decryptedCredentials.url())
                    .setDirectory(tempDir.toFile())
                    .setCredentialsProvider(createCredentialsProvider(decryptedCredentials))
                    .call();

            return new JGitRepository(tempDir, git);
        } catch (InvalidRemoteException e) {
            log.error("Invalid Git repository URL format: {}", decryptedCredentials.url());
            throw new GitRepositoryException("Invalid Git repository URL format: " + e.getMessage(), e);
        } catch (TransportException e) {
            log.error("Failed to connect to Git repository: {}", e.getMessage());
            throw new GitRepositoryConnectionException("Failed to connect to Git repository: " + e.getMessage(), e);
        } catch (GitAPIException | IOException e) {
            log.error("Error while loading repository: {}", e.getMessage());
            throw new GitRepositoryException("An exception occurred while loading the repository: " + e.getMessage(), e);
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
