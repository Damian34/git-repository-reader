package com.damian34.gitreader.common.config;

import com.damian34.gitreader.model.encryption.service.CredentialsEncryptionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EncryptionConfiguration {

    @Value("${encryption.secret-key}")
    private String secretKey;

    @Bean
    public CredentialsEncryptionService credentialsEncryptionService() {
        return new CredentialsEncryptionService(secretKey);
    }
} 