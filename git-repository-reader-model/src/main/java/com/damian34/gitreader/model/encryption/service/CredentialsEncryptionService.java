package com.damian34.gitreader.model.encryption.service;

import com.damian34.gitreader.model.queue.GitConnectionCredentials;
import com.damian34.gitreader.model.encryption.util.EncryptionUtil;

public class CredentialsEncryptionService {
    private final EncryptionUtil encryptionUtil;

    public CredentialsEncryptionService(String secretKey) {
        this.encryptionUtil = new EncryptionUtil(secretKey);
    }

    public GitConnectionCredentials encrypt(GitConnectionCredentials credentials) {
        if (credentials == null) return null;

        return new GitConnectionCredentials(
                credentials.url(),
                encryptionUtil.encrypt(credentials.username()),
                encryptionUtil.encrypt(credentials.password()),
                encryptionUtil.encrypt(credentials.token())
        );
    }

    public GitConnectionCredentials decrypt(GitConnectionCredentials encryptedCredentials) {
        if (encryptedCredentials == null) return null;

        return new GitConnectionCredentials(
                encryptedCredentials.url(),
                encryptionUtil.decrypt(encryptedCredentials.username()),
                encryptionUtil.decrypt(encryptedCredentials.password()),
                encryptionUtil.decrypt(encryptedCredentials.token())
        );
    }
} 