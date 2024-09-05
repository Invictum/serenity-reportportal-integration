package com.github.invictum.reportportal;

import io.reactivex.Maybe;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SuiteStorageTest {

    SuiteStorage storage;

    @BeforeEach
    public void setupStorage() {
        storage = new SuiteStorage();
        storage.start("suite", Maybe::empty);
    }


    @Test
    public void testAddNewFail() {
        storage.addNewFail("suite", "storage");
        Assertions.assertTrue(storage.isFailPresent("suite", "storage"));
    }

    @Test
    public void testIncrementRetiresCount() {
        storage.addNewFail("suite", "storage");
        Assertions.assertEquals(1, storage.incrementAndGetRetriesCount("suite", "storage"));
        Assertions.assertEquals(2, storage.incrementAndGetRetriesCount("suite", "storage"));
        Assertions.assertEquals(3, storage.incrementAndGetRetriesCount("suite", "storage"));
    }

}
