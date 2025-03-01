package com.damian34.gitreader.domain.service;

import com.damian34.gitreader.domain.model.Branch;
import com.damian34.gitreader.domain.service.vo.GitConnectionCredentials;

import java.util.List;

public interface GitRepositoryReader {

    boolean isSupported(String url);

    List<Branch> read(GitConnectionCredentials credentials);
}
