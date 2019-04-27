package com.github.invictum.reportportal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.Logs;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

@RunWith(JUnit4.class)
public class LogStorageTest {

    private LogStorage storage;
    private Logs logsMock;

    @Before
    public void beforeTest() {
        storage = new LogStorage();
        logsMock = Mockito.mock(Logs.class);
        Mockito.when(logsMock.getAvailableLogTypes()).thenReturn(Collections.singleton("data"));
        LogEntry logEntry = new LogEntry(Level.INFO, 42, "Message");
        LogEntry entry = new EnhancedLogEntry("data", logEntry);
        LogEntries entries = new LogEntries(Collections.singleton(entry));
        Mockito.when(logsMock.get("data")).thenReturn(entries);
    }

    @Test
    public void collectLogsTest() {
        storage.collect(logsMock);
        List<EnhancedLogEntry> actual = storage.query(item -> true);
        Assert.assertEquals(1, actual.size());
    }

    @Test
    public void queryLogsRemoveTest() {
        storage.query(item -> true);
        List<EnhancedLogEntry> actual = storage.query(item -> true);
        Assert.assertTrue(actual.isEmpty());
    }

    @Test
    public void cleanLogsTest() {
        storage.collect(logsMock);
        storage.clean();
        Assert.assertTrue(storage.query(item -> true).isEmpty());
    }

    @Test
    public void collectAvailableTypesOnlyOnceTest() {
        storage.collect(logsMock);
        storage.collect(logsMock);
        Mockito.verify(logsMock, Mockito.times(1)).getAvailableLogTypes();
    }

    @Test
    public void skipCollectionIfNull() {
        Mockito.when(logsMock.get("data")).thenReturn(null);
        storage.collect(logsMock);
        Assert.assertTrue(storage.query(item -> true).isEmpty());
    }

    @Test
    public void disabledOnError() {
        Mockito.when(logsMock.getAvailableLogTypes()).thenThrow(WebDriverException.class);
        storage.collect(logsMock);
        storage.collect(logsMock);
        Mockito.verify(logsMock, Mockito.times(1)).getAvailableLogTypes();
    }
}
