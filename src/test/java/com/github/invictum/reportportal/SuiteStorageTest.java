package com.github.invictum.reportportal;

import io.reactivex.Maybe;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SuiteStorageTest {

    SuiteStorage storage;

    @Before
    public void setupStorage() {
        storage = new SuiteStorage();
        storage.start("suite", Maybe::empty);
    }


    @Test
    public void testAddNewFail() {
        storage.addNewFail("suite", "storage");
        Assert.assertTrue(storage.isFailPresent("suite", "storage"));
    }

    @Test
    public void testIncreaseFailCount() {
        storage.addNewFail("suite", "storage");
        Assert.assertEquals(1, storage.increaseFailCount("suite", "storage"));
        Assert.assertEquals(2, storage.increaseFailCount("suite", "storage"));
    }

}
