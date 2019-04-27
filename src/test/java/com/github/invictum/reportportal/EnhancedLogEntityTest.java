package com.github.invictum.reportportal;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.openqa.selenium.logging.LogEntry;

import java.util.logging.Level;

@RunWith(JUnit4.class)
public class EnhancedLogEntityTest {

    @Test
    public void toJsonTest() {
        LogEntry logEntry = new LogEntry(Level.INFO, 42, "Message");
        EnhancedLogEntry entry = new EnhancedLogEntry("type", logEntry);
        Assert.assertTrue(entry.toJson().containsKey("type"));
    }
}
