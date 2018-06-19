package com.github.invictum.reportportal;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.logging.Level;

@RunWith(JUnit4.class)
public class EnhancedLogEntityTest {

    @Test
    public void toJsonTest() {
        EnhancedLogEntry entry = new EnhancedLogEntry("type", Level.INFO, 0, "Message");
        Assert.assertTrue(entry.toJson().containsKey("type"));
    }
}
