package com.github.invictum.reportportal.log.unit;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.model.TestResult;
import net.thucydides.core.model.TestStep;
import net.thucydides.core.model.stacktrace.FailureCause;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.epam.ta.reportportal.ws.model.log.SaveLogRQ;
import com.github.invictum.reportportal.LogLevel;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class ErrorTest {

    @Mock
    private TestStep stepMock;

    @Mock(lenient = true)
    private TestOutcome testOutcomeMock;

    @Mock
    private FailureCause failureCauseMock;

    @Test
    public void noException() {
        Assert.assertTrue(Error.basic().apply(stepMock).isEmpty());
    }

    @Test
    public void customErrorProcessing() {
        // Setup mock
        Mockito.when(stepMock.getResult()).thenReturn(TestResult.ERROR);
        Mockito.when(stepMock.getStartTime()).thenReturn(ZonedDateTime.now());
        Mockito.when(stepMock.getException()).thenReturn(failureCauseMock);
        Mockito.when(stepMock.getConciseErrorMessage()).thenReturn("Custom error");
        SaveLogRQ actual = Error.configuredStepError(TestStep::getConciseErrorMessage).apply(stepMock).iterator().next();
        // Verification
        Assert.assertEquals("Custom error", actual.getMessage());
        Assert.assertEquals(LogLevel.ERROR.toString(), actual.getLevel());
    }

    @Test
    public void errorAtTestLevelShouldBeLogged(){
        Mockito.when(testOutcomeMock.getStartTime()).thenReturn(ZonedDateTime.now());
        Mockito.when(testOutcomeMock.getTestFailureCause()).thenReturn(new FailureCause(new RuntimeException()));
        Iterator<SaveLogRQ> iterator = Error.configuredTestError(TestOutcome::getConciseErrorMessage).apply(testOutcomeMock).iterator();
        Assert.assertTrue(iterator.hasNext());
    }

    @Test
    public void errorAtStepLevelShouldNotBeLoggedAtTestLevel(){
        Mockito.when(testOutcomeMock.getStartTime()).thenReturn(ZonedDateTime.now());
        Mockito.when(testOutcomeMock.getTestFailureCause()).thenReturn(new FailureCause(new RuntimeException()));
        Mockito.when(testOutcomeMock.getFailingStep()).thenReturn(Optional.of(new TestStep()));
        Iterator<SaveLogRQ> iterator = Error.configuredTestError(TestOutcome::getConciseErrorMessage).apply(testOutcomeMock).iterator();
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void defaultError() {
        // Setup mock
        Mockito.when(stepMock.getResult()).thenReturn(TestResult.FAILURE);
        Mockito.when(stepMock.getStartTime()).thenReturn(ZonedDateTime.now());
        Mockito.when(stepMock.getException()).thenReturn(failureCauseMock);
        Mockito.when(failureCauseMock.getOriginalCause()).thenReturn(new IllegalStateException("Details"));
        Collection<SaveLogRQ> logs = Error.basic().apply(stepMock);
        // Verification
        Assert.assertEquals(1, logs.size());
        SaveLogRQ actual = logs.iterator().next();
        Assert.assertEquals(LogLevel.ERROR.toString(), actual.getLevel());
    }
}
