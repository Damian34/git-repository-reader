package com.damian34.gitreader.infrastructure.config;

import com.mongodb.ConnectionString;
import com.mongodb.reactivestreams.client.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import java.util.Objects;

@Configuration
@EnableReactiveMongoRepositories(basePackages = "com.damian34.gitreader.infrastructure.repository")
public class ReactiveMongoConfig {
    
    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate(MongoClient mongoClient) {
        ConnectionString connectionString = new ConnectionString(mongoUri);
        String connection = Objects.requireNonNull(connectionString.getDatabase());
        var factory = new SimpleReactiveMongoDatabaseFactory(mongoClient, connection);
        return new ReactiveMongoTemplate(factory);
    }
} 