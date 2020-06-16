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
        fileStorage.touch(42L);
        String[] actual = directory.getRoot().list();
        Assert.assertArrayEquals(new String[]{"42"}, actual);
    }

    @Test
    public void count() throws IOException {
        directory.newFile("19");
        directory.newFile("32");
        Assert.assertEquals(2, fileStorage.count());
    }

    @Test
    public void loadAndClean() throws IOException {
        directory.newFile("11");
        directory.newFile("12");
        Set<Long> actual = fileStorage.loadAndClean();
        Set<Long> expected = new HashSet<>();
        expected.add(11L);
        expected.add(12L);
        Assert.assertEquals(expected, actual);
    }
}
