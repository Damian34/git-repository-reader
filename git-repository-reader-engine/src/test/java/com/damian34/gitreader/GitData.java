package com.damian34.gitreader;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public class GitData {
    public static final String GITHUB_SECURITY_JWT_URL = "https://github.com/Damian34/spring-security-jwt-auth";
    public static final String GITHUB_SECURITY_JWT_URL_DOMAIN = "github.com/Damian34/spring-security-jwt-auth";
    
    public static final String GIT_REPOSITORY_READER_URL = "https://github.com/Damian34/git-repository-reader.git";
    public static final String GIT_REPOSITORY_READER_URL_NO_EXTENSION = "https://github.com/Damian34/git-repository-reader";
    public static final String GIT_REPOSITORY_READER_URL_DOMAIN = "github.com/Damian34/git-repository-reader";
    
    public static final String GITLAB_URL = "https://gitlab.com/user/repo";
    public static final String BITBUCKET_URL = "https://bitbucket.org/user/repo";
    
    public static final String INVALID_URL = "github.com/invalid-format";
    public static final String GOOGLE_URL = "https://www.google.pl/";
    
    public static final String GITHUB_USER_REPO_HTTPS = "https://github.com/user/repo";
    public static final String GITHUB_USER_REPO_HTTP = "http://github.com/user/repo";
    public static final String GITHUB_USER_REPO_DOMAIN = "github.com/user/repo";
    public static final String GITHUB_USER_REPO_WWW = "www.github.com/user/repo";
    public static final String GITHUB_USER_REPO_TREE = "https://www.github.com/user/repo/tree/main";
}
