package com.github.invictum.reportportal.log.unit;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

import net.thucydides.model.domain.TestOutcome;
import net.thucydides.model.domain.TestResult;
import net.thucydides.model.domain.TestStep;
import net.thucydides.model.domain.stacktrace.FailureCause;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.epam.ta.reportportal.ws.model.log.SaveLogRQ;
import com.github.invictum.reportportal.LogLevel;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ErrorTest {

    @Mock
    private TestStep stepMock;

    @Mock(lenient = true)
    private TestOutcome testOutcomeMock;

    @Mock
    private FailureCause failureCauseMock;

    @Test
    public void noException() {
        Assertions.assertTrue(Error.basic().apply(stepMock).isEmpty());
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
        Assertions.assertEquals("Custom error", actual.getMessage());
        Assertions.assertEquals(LogLevel.ERROR.toString(), actual.getLevel());
    }

    @Test
    public void errorAtTestLevelShouldBeLogged(){
        Mockito.when(testOutcomeMock.getStartTime()).thenReturn(ZonedDateTime.now());
        Mockito.when(testOutcomeMock.getTestFailureCause()).thenReturn(new FailureCause(new RuntimeException()));
        Iterator<SaveLogRQ> iterator = Error.configuredTestError(TestOutcome::getConciseErrorMessage).apply(testOutcomeMock).iterator();
        Assertions.assertTrue(iterator.hasNext());
    }

    @Test
    public void errorAtStepLevelShouldNotBeLoggedAtTestLevel(){
        Mockito.when(testOutcomeMock.getStartTime()).thenReturn(ZonedDateTime.now());
        Mockito.when(testOutcomeMock.getTestFailureCause()).thenReturn(new FailureCause(new RuntimeException()));
        Mockito.when(testOutcomeMock.getFailingStep()).thenReturn(Optional.of(new TestStep()));
        Iterator<SaveLogRQ> iterator = Error.configuredTestError(TestOutcome::getConciseErrorMessage).apply(testOutcomeMock).iterator();
        Assertions.assertFalse(iterator.hasNext());
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
        Assertions.assertEquals(1, logs.size());
        SaveLogRQ actual = logs.iterator().next();
        Assertions.assertEquals(LogLevel.ERROR.toString(), actual.getLevel());
    }
}
