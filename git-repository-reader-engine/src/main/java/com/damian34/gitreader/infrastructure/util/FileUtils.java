package com.damian34.gitreader.infrastructure.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtils {

    public static Path createTempDirectory(String rootFolder, String tempSubFolderName) throws IOException {
        Path baseDir = Paths.get(System.getProperty("java.io.tmpdir"), rootFolder);
        Files.createDirectories(baseDir);
        return Files.createTempDirectory(baseDir, tempSubFolderName);
    }

    public static void deleteDirectory(Path directory) {
        if (directory != null) {
            deleteDirectory(directory.toFile());
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void deleteDirectory(File directory) {
        if (directory.exists()) {
            for (File file : Objects.requireNonNull(directory.listFiles())) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
            directory.delete();
        }
    }
}
