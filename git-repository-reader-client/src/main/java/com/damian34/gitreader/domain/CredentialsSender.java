package com.damian34.gitreader.domain;

import com.damian34.gitreader.queue.GitConnectionCredentials;

public interface CredentialsSender {

    void send(GitConnectionCredentials credentials);
}
