package com.damian34.gitreader.infrastructure.util;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootTest
class FileUtilsTest {

    private static final String TEST_ROOT_FOLDER = "test_root_folder";
    private Path testDirectory;

    @AfterEach
    void cleanup() {
        if (testDirectory != null) {
            FileUtils.deleteDirectory(testDirectory);
        }
    }

    @Test
    @SneakyThrows
    void shouldCreateDirectoryInSystemTemp() {
        // when
        testDirectory = FileUtils.createTempDirectory(TEST_ROOT_FOLDER, "test_subfolder");

        // then
        Assertions.assertTrue(Files.exists(testDirectory));
        Assertions.assertTrue(Files.isDirectory(testDirectory));
        Assertions.assertTrue(testDirectory.toString().contains(TEST_ROOT_FOLDER));
    }

    @Test
    @SneakyThrows
    void shouldDeleteDirectoryAndContents() {
        // given
        testDirectory = FileUtils.createTempDirectory(TEST_ROOT_FOLDER, "delete_test");
        Path testFile = testDirectory.resolve("test_file.txt");
        Files.write(testFile, "test content".getBytes());
        Path nestedDir = testDirectory.resolve("nested");
        Files.createDirectory(nestedDir);
        Path nestedFile = nestedDir.resolve("nested_file.txt");
        Files.write(nestedFile, "nested content".getBytes());

        // when
        FileUtils.deleteDirectory(testDirectory);

        // then
        Assertions.assertFalse(Files.exists(testDirectory));
        Assertions.assertFalse(Files.exists(nestedFile));
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = "non-existent-directory")
    void shouldNotThrowExceptionWhenInvalidDirectory(String path) {
        // given
        File directory = new File(path);
        
        // when & then
        Assertions.assertDoesNotThrow(() -> FileUtils.deleteDirectory(directory));
    }
} 