package com.github.invictum.reportportal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@RunWith(JUnit4.class)
public class FileStorageTest {

    @Rule
    public TemporaryFolder directory = new TemporaryFolder();
    private FileStorage fileStorage;

    @Before
    public void before() {
        fileStorage = new FileStorage(directory.getRoot().getAbsolutePath());
    }

    @Test
    public void touch() {
        fileStorage.touch("1");
        String[] actual = directory.getRoot().list();
        Assert.assertArrayEquals(new String[]{"1"}, actual);
    }

    @Test
    public void count() throws IOException {
        directory.newFile("1");
        directory.newFile("2");
        Assert.assertEquals(2, fileStorage.count());
    }

    @Test
    public void loadAndClean() throws IOException {
        directory.newFile("1");
        directory.newFile("2");
        Set<Long> actual = fileStorage.loadAndClean();
        Set<Long> expected = new HashSet<>();
        expected.add(Long.valueOf("1"));
        expected.add(Long.valueOf("2"));
        Assert.assertEquals(expected, actual);
    }
}
