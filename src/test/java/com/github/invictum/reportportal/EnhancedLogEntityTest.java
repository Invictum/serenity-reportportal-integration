package com.github.invictum.reportportal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.logging.LogEntry;

import java.util.logging.Level;

public class EnhancedLogEntityTest {

    @Test
    public void toJsonTest() {
        LogEntry logEntry = new LogEntry(Level.INFO, 42, "Message");
        EnhancedLogEntry entry = new EnhancedLogEntry("type", logEntry);
        Assertions.assertTrue(entry.toJson().containsKey("type"));
    }
}
