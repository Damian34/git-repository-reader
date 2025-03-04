package com.damian34.gitreader.model.queue;

public record GitConnectionCredentials(
        String url,
        String username,
        String password,
        String token
) {
}
