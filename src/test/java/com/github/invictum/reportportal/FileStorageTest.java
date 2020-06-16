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
        fileStorage.touch("abcd-ef12-3456-7890");
        String[] actual = directory.getRoot().list();
        Assert.assertArrayEquals(new String[]{"abcd-ef12-3456-7890"}, actual);
    }

    @Test
    public void count() throws IOException {
        directory.newFile("abcd-ef12-3456-789a");
        directory.newFile("abcd-ef12-3456-789b");
        Assert.assertEquals(2, fileStorage.count());
    }

    @Test
    public void loadAndClean() throws IOException {
        directory.newFile("abcd-ef12-3456-789a");
        directory.newFile("abcd-ef12-3456-789b");
        Set<String> actual = fileStorage.loadAndClean();
        Set<String> expected = new HashSet<>();
        expected.add("abcd-ef12-3456-789a");
        expected.add("abcd-ef12-3456-789b");
        Assert.assertEquals(expected, actual);
    }
}
