package com.damian34.gitreader.infrastructure.service.repository;

import com.damian34.gitreader.infrastructure.util.FileUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.eclipse.jgit.api.Git;

import java.nio.file.Path;
import java.util.Optional;

@Getter
@AllArgsConstructor
public class JGitRepository implements AutoCloseable {
    private Path tempRepositoryDir;
    private Git git;

    @Override
    public void close() {
        Optional.ofNullable(git).ifPresent(Git::close);
        Optional.ofNullable(tempRepositoryDir).ifPresent(path -> FileUtils.deleteDirectory(path.toFile()));
    }
}
