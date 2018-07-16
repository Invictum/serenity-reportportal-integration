package com.github.invictum.reportportal;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ItemTypeTest {

    @Test
    public void suiteKey() {
        Assert.assertEquals("TEST", ItemType.SUITE.key());
    }

    @Test
    public void testKey() {
        Assert.assertEquals("STEP", ItemType.TEST.key());
    }
}
