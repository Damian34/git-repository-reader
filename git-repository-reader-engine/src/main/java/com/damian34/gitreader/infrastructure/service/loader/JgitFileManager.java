package com.damian34.gitreader.infrastructure.service.loader;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class JgitFileManager {

    static Path createTempDirectory(String rootFolder, String tempSubFolderName) throws IOException {
        Path baseDir = Paths.get(System.getProperty("java.io.tmpdir"), rootFolder);
        Files.createDirectories(baseDir);
        return Files.createTempDirectory(baseDir, tempSubFolderName);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    static void deleteDirectory(File directory) {
        if (directory.exists()) {
            for (File file : directory.listFiles()) {
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
