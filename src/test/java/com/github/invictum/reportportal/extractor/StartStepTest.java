package com.github.invictum.reportportal.extractor;

import com.github.invictum.reportportal.EnhancedMessage;
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
public class StartStepTest {

    @Mock
    private TestStep stepMock;

    @Test
    public void startStep() {
        StartStep startStep = new StartStep();
        // Setup mock
        Mockito.when(stepMock.getResult()).thenReturn(TestResult.SUCCESS);
        Mockito.when(stepMock.getDescription()).thenReturn("Step");
        Mockito.when(stepMock.getStartTime()).thenReturn(ZonedDateTime.now());
        Collection<EnhancedMessage> logs = startStep.extract(stepMock);
        // Verify
        Assert.assertEquals(1, logs.size());
        EnhancedMessage actual = logs.iterator().next();
        Assert.assertEquals("[STARTED] Step", actual.getMessage());
        Assert.assertEquals(LogLevel.INFO, actual.getLevel());
    }
}
