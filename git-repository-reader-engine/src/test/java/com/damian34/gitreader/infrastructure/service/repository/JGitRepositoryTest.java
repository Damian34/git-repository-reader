package com.damian34.gitreader.infrastructure.service.repository;

import com.damian34.gitreader.TestContainerInitializer;
import com.damian34.gitreader.infrastructure.util.FileUtils;
import org.eclipse.jgit.api.Git;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import lombok.SneakyThrows;

import java.io.File;
import java.nio.file.Path;

@SpringBootTest
@ContextConfiguration(initializers = TestContainerInitializer.class)
class JGitRepositoryTest {

    @Test
    void shouldCloseGitAndDeleteDirectory() {
        // given
        Git git = Mockito.mock(Git.class);
        Path tempRepositoryDir = Mockito.mock(Path.class);
        File mockFile = Mockito.mock(File.class);
        Mockito.when(tempRepositoryDir.toFile()).thenReturn(mockFile);
        JGitRepository repository = new JGitRepository(tempRepositoryDir, git);

        try (MockedStatic<FileUtils> fileUtilsMock = Mockito.mockStatic(FileUtils.class)) {
            // when
            repository.close();

            // then
            Mockito.verify(git).close();
            fileUtilsMock.verify(() -> FileUtils.deleteDirectory(mockFile));
        }
    }

    @Test
    void shouldOnlyDeleteDirectoryWhenNullGit() {
        // given
        Path tempRepositoryDir = Mockito.mock(Path.class);
        File mockFile = Mockito.mock(File.class);
        Mockito.when(tempRepositoryDir.toFile()).thenReturn(mockFile);
        JGitRepository repository = new JGitRepository(tempRepositoryDir, null);

        try (MockedStatic<FileUtils> fileUtilsMock = Mockito.mockStatic(FileUtils.class)) {
            // when
            repository.close();

            // then
            fileUtilsMock.verify(() -> FileUtils.deleteDirectory(mockFile));
        }
    }

    @Test
    void shouldOnlyCloseGitWhenNullDirectory() {
        // given
        Git git = Mockito.mock(Git.class);
        JGitRepository repository = new JGitRepository(null, git);

        try (MockedStatic<FileUtils> fileUtilsMock = Mockito.mockStatic(FileUtils.class)) {
            // when
            repository.close();

            // then
            Mockito.verify(git).close();
            fileUtilsMock.verify(() -> FileUtils.deleteDirectory(Mockito.any(File.class)), Mockito.never());
        }
    }

    @Test
    @SneakyThrows
    void shouldNotThrowExceptionWhenAllNull() {
        // given
        JGitRepository jGitRepository = new JGitRepository(null, null);
        
        // when & then
        Assertions.assertDoesNotThrow(jGitRepository::close);
    }
} 