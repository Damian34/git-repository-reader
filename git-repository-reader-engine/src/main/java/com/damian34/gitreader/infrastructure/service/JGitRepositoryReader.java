package com.damian34.gitreader.infrastructure.service;

import com.damian34.gitreader.domain.service.GitRepositoryReader;
import com.damian34.gitreader.exception.GitRepositoryException;
import com.damian34.gitreader.infrastructure.service.repository.JGitRepository;
import com.damian34.gitreader.infrastructure.service.repository.JGitRepositoryLoader;
import com.damian34.gitreader.model.queue.GitConnectionCredentials;
import com.damian34.gitreader.model.repository.Branch;
import com.damian34.gitreader.model.repository.Commit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public abstract class JGitRepositoryReader implements GitRepositoryReader {
    protected final JGitRepositoryLoader jGitRepositoryLoader;

    @Override
    public Flux<Branch> fetchBranches(GitConnectionCredentials credentials) {
        return jGitRepositoryLoader.loadRepository(credentials)
                .flatMapMany(gitRepository ->
                    Mono.fromCallable(() -> getGitBranches(gitRepository))
                            .flatMapMany(Flux::fromIterable)
                            .flatMap(branch ->
                                    getGitCommitsAndMap(gitRepository.getTempRepositoryDir().toString(), branch)
                            )
                            .subscribeOn(Schedulers.boundedElastic())
                            .doFinally(signalType -> gitRepository.close())
                );
    }

    protected abstract String getOriginPath();

    private Mono<Branch> getGitCommitsAndMap(String repoPath, Ref branch) {
        return getGitCommitsReactive(repoPath, branch.getName())
                .map(commits -> Branch.builder()
                        .id(branch.getObjectId().toString())
                        .name(branch.getName())
                        .commits(commits)
                        .build());
    }

    private List<Ref> getGitBranches(JGitRepository gitRepository) {
        Git git = gitRepository.getGit();
        try {
            return git.branchList()
                    .setListMode(ListBranchCommand.ListMode.ALL)
                    .call()
                    .stream()
                    .filter(ref -> ref.getName().startsWith(getOriginPath()))
                    .toList();
        } catch (GitAPIException e) {
            log.error("Error occurred while fetching branches for Git repository.", e);
            throw new GitRepositoryException("Error occurred while fetching branches for Git repository.", e);
        }
    }

    private Mono<List<Commit>> getGitCommitsReactive(String repoPath, String branch) {
        return Mono.fromCallable(() -> getGitCommits(repoPath, branch))
                .subscribeOn(Schedulers.boundedElastic());
    }

    private List<Commit> getGitCommits(String repoPath, String branch) {
        String command = "git log --pretty=format:%H|%s|%an|%ad --date=iso " + branch;
        ProcessBuilder pb = new ProcessBuilder(command.split(" "));
        pb.directory(new File(repoPath));
        pb.redirectErrorStream(true);
        try {
            Process process = pb.start();

            try (InputStreamReader isr = new InputStreamReader(process.getInputStream());
                 BufferedReader reader = new BufferedReader(isr)) {
                List<Commit> commits = reader.lines()
                        .map(line -> line.split("\\|", 4))
                        .filter(parts -> parts.length == 4)
                        .map(parts -> Commit.builder()
                                .id(parts[0])
                                .message(parts[1])
                                .author(parts[2])
                                .date(parts[3])
                                .build()
                        )
                        .toList();
                process.waitFor();
                return commits;
            } finally {
                process.destroy();
            }
        } catch (Exception e) {
            log.error("Error occurred while processing commits for Git repository.", e);
            throw new GitRepositoryException("Error occurred while processing commits for Git repository.", e);
        }
    }
}
