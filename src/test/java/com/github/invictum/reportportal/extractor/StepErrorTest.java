package com.github.invictum.reportportal.extractor;

import com.github.invictum.reportportal.EnhancedMessage;
import com.github.invictum.reportportal.LogLevel;
import net.thucydides.core.model.TestResult;
import net.thucydides.core.model.TestStep;
import net.thucydides.core.model.stacktrace.FailureCause;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.ZonedDateTime;
import java.util.Collection;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class StepErrorTest {

    @Mock
    private TestStep stepMock;

    @Mock
    private FailureCause failureCauseMock;

    @Test
    public void noException() {
        StepError stepError = new StepError();
        Assert.assertTrue(stepError.extract(stepMock).isEmpty());
    }

    @Test
    public void customErrorProcessing() {
        StepError stepError = new StepError(TestStep::getConciseErrorMessage);
        // Setup mock
        Mockito.when(stepMock.getResult()).thenReturn(TestResult.ERROR);
        Mockito.when(stepMock.getStartTime()).thenReturn(ZonedDateTime.now());
        Mockito.when(stepMock.getException()).thenReturn(failureCauseMock);
        Mockito.when(stepMock.getConciseErrorMessage()).thenReturn("Custom error");
        EnhancedMessage actual = stepError.extract(stepMock).iterator().next();
        // Verification
        Assert.assertEquals("Custom error", actual.getMessage());
        Assert.assertEquals(LogLevel.ERROR, actual.getLevel());
    }

    @Test
    public void defaultError() {
        StepError stepError = new StepError();
        // Setup mock
        Mockito.when(stepMock.getResult()).thenReturn(TestResult.FAILURE);
        Mockito.when(stepMock.getStartTime()).thenReturn(ZonedDateTime.now());
        Mockito.when(stepMock.getException()).thenReturn(failureCauseMock);
        Mockito.when(failureCauseMock.getOriginalCause()).thenReturn(new IllegalStateException("Details"));
        Collection<EnhancedMessage> logs = stepError.extract(stepMock);
        // Verification
        Assert.assertEquals(1, logs.size());
        EnhancedMessage actual = logs.iterator().next();
        Assert.assertEquals(LogLevel.ERROR, actual.getLevel());
    }
}
