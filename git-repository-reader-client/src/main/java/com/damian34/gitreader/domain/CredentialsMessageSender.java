package com.damian34.gitreader.domain;

import com.damian34.gitreader.model.queue.GitConnectionCredentials;

public interface CredentialsMessageSender {
    void sendMessage(GitConnectionCredentials credentials);
}
