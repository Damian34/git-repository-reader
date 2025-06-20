package com.damian34.gitreader.infrastructure.service;

import com.damian34.gitreader.infrastructure.exception.GitRepositoryException;
import com.damian34.gitreader.infrastructure.service.loader.JGitRepositoryLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Slf4j
@Service
public class GitHubRepositoryReader extends JGitRepositoryReader {

    public static final String GITHUB_DOMAIN = "github.com";
    public static final String URL_BASE = "https://github.com";
    public static final String GITHUB_ORIGIN_PATH = "refs/remotes/origin/";

    public GitHubRepositoryReader(JGitRepositoryLoader jGitRepositoryLoader) {
        super(jGitRepositoryLoader);
    }

    @Override
    public boolean isSupported(String url) {
        return url.contains(GITHUB_DOMAIN);
    }

    @Override
    public String buildGitCloneUrl(String url) {
        String urlBase = url.substring(0, url.indexOf(GITHUB_DOMAIN) + GITHUB_DOMAIN.length());
        String repoStrip = url.substring(urlBase.length() + 1);
        String[] repoParts = repoStrip.split("/");
        if (repoParts.length < 2) {
            log.error("Repository URL is too short or malformed: {}", url);
            throw new GitRepositoryException("Repository URL is too short or malformed: " + url);
        }
        String cloneUrl = MessageFormat.format("{0}/{1}/{2}", URL_BASE, repoParts[0], repoParts[1]);
        if (!cloneUrl.endsWith(".git")) {
            cloneUrl += ".git";
        }
        return cloneUrl;
    }

    @Override
    protected String getOriginPath() {
        return GITHUB_ORIGIN_PATH;
    }
}
