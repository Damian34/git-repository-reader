package com.damian34.gitreader.encryption;

import com.damian34.gitreader.queue.GitConnectionCredentials;

public class CredentialsEncryptionService {
    private final SimpleEncryptor simpleEncryptor;

    public CredentialsEncryptionService(String secretKey) {
        this.simpleEncryptor = new SimpleEncryptor(secretKey);
    }

    public GitConnectionCredentials encrypt(GitConnectionCredentials credentials) {
        if (credentials == null) return null;

        return new GitConnectionCredentials(
                credentials.url(),
                simpleEncryptor.encrypt(credentials.username()),
                simpleEncryptor.encrypt(credentials.password()),
                simpleEncryptor.encrypt(credentials.token())
        );
    }

    public GitConnectionCredentials decrypt(GitConnectionCredentials encryptedCredentials) {
        if (encryptedCredentials == null) return null;

        return new GitConnectionCredentials(
                encryptedCredentials.url(),
                simpleEncryptor.decrypt(encryptedCredentials.username()),
                simpleEncryptor.decrypt(encryptedCredentials.password()),
                simpleEncryptor.decrypt(encryptedCredentials.token())
        );
    }
} 