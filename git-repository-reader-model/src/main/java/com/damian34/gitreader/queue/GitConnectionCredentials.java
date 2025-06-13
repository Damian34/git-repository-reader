package com.damian34.gitreader.queue;

public record GitConnectionCredentials(
        String url,
        String username,
        String password,
        String token
) {
    public GitConnectionCredentials updateUrl(String gitCloneUrl) {
        return new GitConnectionCredentials(gitCloneUrl, username, password, token);
    }
}
