package com.damian34.gitreader.domain.service.vo;

public record GitConnectionCredentials(
        String url,
        String username,
        String password,
        String token
) {
}
