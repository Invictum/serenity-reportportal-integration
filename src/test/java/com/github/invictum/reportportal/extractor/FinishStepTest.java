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
public class FinishStepTest {

    @Mock
    private TestStep stepMock;

    @Test
    public void successTest() {
        FinishStep finishStep = new FinishStep();
        /* Setup mock */
        Mockito.when(stepMock.getStartTime()).thenReturn(ZonedDateTime.now());
        Mockito.when(stepMock.getDuration()).thenReturn(1000L);
        Mockito.when(stepMock.getResult()).thenReturn(TestResult.SUCCESS);
        Mockito.when(stepMock.getDescription()).thenReturn("Description");
        Collection<EnhancedMessage> logs = finishStep.extract(stepMock);
        /* Verification */
        Assert.assertEquals(1, logs.size());
        EnhancedMessage actual = logs.iterator().next();
        Assert.assertEquals(LogLevel.INFO, actual.getLevel());
        Assert.assertEquals("[SUCCESS] Description", actual.getMessage());
    }

    @Test
    public void errorTest() {
        FinishStep finishStep = new FinishStep();
        /* Setup mock */
        Mockito.when(stepMock.getStartTime()).thenReturn(ZonedDateTime.now());
        Mockito.when(stepMock.getDuration()).thenReturn(1000L);
        Mockito.when(stepMock.getResult()).thenReturn(TestResult.ERROR);
        Mockito.when(stepMock.getDescription()).thenReturn("Error");
        Collection<EnhancedMessage> logs = finishStep.extract(stepMock);
        /* Verification */
        Assert.assertEquals(1, logs.size());
        EnhancedMessage actual = logs.iterator().next();
        Assert.assertEquals(LogLevel.ERROR, actual.getLevel());
        Assert.assertEquals("[ERROR] Error", actual.getMessage());
    }
}
