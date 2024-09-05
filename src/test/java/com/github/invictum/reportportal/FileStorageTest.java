package com.github.invictum.reportportal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class FileStorageTest {

    @TempDir
    Path tempDir;

    private FileStorage fileStorage;

    @BeforeEach
    public void before() {
        fileStorage = new FileStorage(tempDir.toAbsolutePath().toString());
    }

    @Test
    public void touch() throws IOException {
        fileStorage.touch(42L);
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(tempDir)) {
            Set<String> actual = new HashSet<>();
            for (Path path : directoryStream) {
                actual.add(path.getFileName().toString());
            }
            Assertions.assertArrayEquals(new String[]{"42"}, actual.toArray(new String[0]));
        }
    }

    @Test
    public void count() throws IOException {
        Files.createFile(tempDir.resolve("19"));
        Files.createFile(tempDir.resolve("32"));
        Assertions.assertEquals(2, fileStorage.count());
    }

    @Test
    public void loadAndClean() throws IOException {
        Files.createFile(tempDir.resolve("11"));
        Files.createFile(tempDir.resolve("12"));
        Set<Long> actual = fileStorage.loadAndClean();
        Set<Long> expected = new HashSet<>();
        expected.add(11L);
        expected.add(12L);
        Assertions.assertEquals(expected, actual);
    }
}
