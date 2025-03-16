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

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public abstract class JGitRepositoryReader implements GitRepositoryReader {
    protected final JGitRepositoryLoader jGitRepositoryLoader;

    @Override
    public List<Branch> fetchBranches(GitConnectionCredentials credentials) {
        try (JGitRepository gitRepository = jGitRepositoryLoader.loadRepository(credentials)) {
            List<Ref> branches = getGitBranches(gitRepository);
            return branches.stream().map(branch -> {
                var commits = getGitCommits(gitRepository.getTempRepositoryDir().toString(), branch.getName());
                return Branch.builder()
                        .id(branch.getObjectId().toString())
                        .name(branch.getName())
                        .commits(commits)
                        .build();
            }).collect(Collectors.toList());
        }
    }

    protected abstract String getOriginPath();

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
                        .collect(Collectors.toList());
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
