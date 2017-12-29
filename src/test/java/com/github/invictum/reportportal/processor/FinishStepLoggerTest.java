package com.github.invictum.reportportal.processor;

import net.thucydides.core.model.TestResult;
import net.thucydides.core.model.TestStep;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.ZonedDateTime;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class FinishStepLoggerTest {

    @Mock
    private TestStep stepMock;

    @Test
    public void finishStepLoggerTest() {
        FinishStepLogger finishStepLogger = new FinishStepLogger();
        /* Setup mock */
        Mockito.when(stepMock.getStartTime()).thenReturn(ZonedDateTime.now());
        Mockito.when(stepMock.getResult()).thenReturn(TestResult.SUCCESS);
        Mockito.when(stepMock.getDuration()).thenReturn(1000L);
        finishStepLogger.proceed(stepMock);
        /* Verification */
        Mockito.verify(stepMock, Mockito.times(1)).getStartTime();
        Mockito.verify(stepMock, Mockito.times(1)).getDescription();
        Mockito.verify(stepMock, Mockito.times(1)).getDuration();
        Mockito.verify(stepMock, Mockito.times(2)).getResult();
    }
}
