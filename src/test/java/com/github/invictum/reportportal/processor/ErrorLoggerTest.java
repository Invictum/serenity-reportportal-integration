package com.github.invictum.reportportal.processor;

import net.thucydides.core.model.TestStep;
import net.thucydides.core.model.stacktrace.FailureCause;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.ZonedDateTime;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class ErrorLoggerTest {

    @Mock
    private TestStep stepMock;

    @Mock
    private FailureCause failureCauseMock;

    @Test
    public void noExceptionTest() {
        ErrorLogger errorLogger = new ErrorLogger(false);
        errorLogger.proceed(stepMock);
        Mockito.verify(stepMock, Mockito.times(1)).getException();
        Mockito.verify(stepMock, Mockito.never()).getConciseErrorMessage();
    }

    @Test
    public void shortExceptionLogTest() {
        ErrorLogger errorLogger = new ErrorLogger(false);
        /* Setup mock */
        Mockito.when(stepMock.getStartTime()).thenReturn(ZonedDateTime.now());
        Mockito.when(stepMock.getException()).thenReturn(failureCauseMock);
        errorLogger.proceed(stepMock);
        /* Verification */
        Mockito.verify(stepMock, Mockito.times(1)).getException();
        Mockito.verify(stepMock, Mockito.times(1)).getConciseErrorMessage();
        Mockito.verify(stepMock, Mockito.times(1)).getResult();
    }

    @Test
    public void fullExceptionLogTest() {
        ErrorLogger errorLogger = new ErrorLogger(true);
        /* Setup mock */
        Mockito.when(stepMock.getStartTime()).thenReturn(ZonedDateTime.now());
        Mockito.when(stepMock.getException()).thenReturn(failureCauseMock);
        Mockito.when(failureCauseMock.getOriginalCause()).thenReturn(new IllegalStateException("Details"));
        errorLogger.proceed(stepMock);
        /* Verification */
        Mockito.verify(stepMock, Mockito.times(2)).getException();
        Mockito.verify(stepMock, Mockito.times(1)).getConciseErrorMessage();
        Mockito.verify(stepMock, Mockito.times(1)).getResult();
    }
}
