package com.github.invictum.reportportal.log.unit;

import com.epam.ta.reportportal.ws.model.log.SaveLogRQ;
import com.github.invictum.reportportal.LogLevel;
import net.thucydides.core.model.TestResult;
import net.thucydides.core.model.TestStep;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.ZonedDateTime;
import java.util.Collection;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class EssentialsTest {

    @Mock
    private TestStep stepMock;

    @Test
    public void successFinishTest() {
        // Setup mock
        Mockito.when(stepMock.getStartTime()).thenReturn(ZonedDateTime.now());
        Mockito.when(stepMock.getDuration()).thenReturn(1000L);
        Mockito.when(stepMock.getResult()).thenReturn(TestResult.SUCCESS);
        Mockito.when(stepMock.getDescription()).thenReturn("Description");
        Collection<SaveLogRQ> logs = Essentials.finishStep().apply(stepMock);
        // Verification
        Assert.assertEquals(1, logs.size());
        SaveLogRQ actual = logs.iterator().next();
        Assert.assertEquals(LogLevel.INFO.toString(), actual.getLevel());
        Assert.assertEquals("[SUCCESS] Description", actual.getMessage());
    }

    @Test
    public void errorFinishTest() {
        // Setup mock
        Mockito.when(stepMock.getStartTime()).thenReturn(ZonedDateTime.now());
        Mockito.when(stepMock.getDuration()).thenReturn(1000L);
        Mockito.when(stepMock.getResult()).thenReturn(TestResult.ERROR);
        Mockito.when(stepMock.getDescription()).thenReturn("Error");
        Collection<SaveLogRQ> logs = Essentials.finishStep().apply(stepMock);
        // Verification
        Assert.assertEquals(1, logs.size());
        SaveLogRQ actual = logs.iterator().next();
        Assert.assertEquals(LogLevel.ERROR.toString(), actual.getLevel());
        Assert.assertEquals("[ERROR] Error", actual.getMessage());
    }

    @Test
    public void startStep() {
        // Setup mock
        Mockito.when(stepMock.getResult()).thenReturn(TestResult.SUCCESS);
        Mockito.when(stepMock.getDescription()).thenReturn("Step");
        Mockito.when(stepMock.getStartTime()).thenReturn(ZonedDateTime.now());
        Collection<SaveLogRQ> logs = Essentials.startStep().apply(stepMock);
        // Verify
        Assert.assertEquals(1, logs.size());
        SaveLogRQ actual = logs.iterator().next();
        Assert.assertEquals("[STARTED] Step", actual.getMessage());
        Assert.assertEquals(LogLevel.INFO.toString(), actual.getLevel());
    }
}
