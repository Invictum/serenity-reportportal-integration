package com.github.invictum.reportportal.log.unit;

import com.epam.ta.reportportal.ws.model.log.SaveLogRQ;
import com.github.invictum.reportportal.LogLevel;
import com.github.invictum.reportportal.LogStorage;
import com.github.invictum.reportportal.injector.IntegrationInjector;
import net.thucydides.core.model.TestStep;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.Logs;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.logging.Level;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class SeleniumTest {

    @Mock
    private TestStep stepMock;

    private LogStorage storage = IntegrationInjector.getInjector().getInstance(LogStorage.class);

    @Test
    public void allLogs() {
        // Setup mock
        Logs logsMock = Mockito.mock(Logs.class);
        Mockito.when(logsMock.getAvailableLogTypes()).thenReturn(Collections.singleton("browser"));
        ZonedDateTime start = ZonedDateTime.now();
        LogEntry entry = new LogEntry(Level.INFO, start.toEpochSecond() * 1000, "Message");
        LogEntries entries = new LogEntries(Collections.singleton(entry));
        Mockito.when(logsMock.get("browser")).thenReturn(entries);
        storage.clean();
        storage.collect(logsMock);
        Mockito.when(stepMock.getStartTime()).thenReturn(start);
        Mockito.when(stepMock.getDuration()).thenReturn(1000L);
        // Verify
        SaveLogRQ actual = Selenium.allLogs().apply(stepMock).iterator().next();
        Assert.assertEquals("[Selenium-browser] [INFO] Message", actual.getMessage());
        Assert.assertEquals(LogLevel.DEBUG.toString(), actual.getLevel());
    }
}
