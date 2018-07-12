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
public class StepErrorTest {

    @Mock
    private TestStep stepMock;

    @Mock
    private FailureCause failureCauseMock;

    @Test
    public void noExceptionTest() {
        StepError stepError = new StepError();
        stepError.extract(stepMock);
        Mockito.verify(stepMock, Mockito.times(1)).getException();
        Mockito.verify(stepMock, Mockito.never()).getConciseErrorMessage();
    }

    @Test
    public void customFormatExceptionLogTest() {
        StepError stepError = new StepError(TestStep::getConciseErrorMessage);
        /* Setup mock */
        Mockito.when(stepMock.getStartTime()).thenReturn(ZonedDateTime.now());
        Mockito.when(stepMock.getException()).thenReturn(failureCauseMock);
        stepError.extract(stepMock);
        /* Verification */
        Mockito.verify(stepMock, Mockito.times(1)).getException();
        Mockito.verify(stepMock, Mockito.times(1)).getConciseErrorMessage();
        Mockito.verify(stepMock, Mockito.times(1)).getResult();
    }

    @Test
    public void fullExceptionLogTest() {
        StepError stepError = new StepError();
        /* Setup mock */
        Mockito.when(stepMock.getStartTime()).thenReturn(ZonedDateTime.now());
        Mockito.when(stepMock.getException()).thenReturn(failureCauseMock);
        Mockito.when(failureCauseMock.getOriginalCause()).thenReturn(new IllegalStateException("Details"));
        stepError.extract(stepMock);
        /* Verification */
        Mockito.verify(stepMock, Mockito.times(2)).getException();
        Mockito.verify(stepMock, Mockito.times(1)).getResult();
    }
}
