package com.github.invictum.reportportal.processor;

import net.thucydides.core.model.TestStep;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.ZonedDateTime;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class StartStepLoggerTest {

    @Mock
    private TestStep stepMock;

    @Test
    public void startStepLoggerTest() {
        StartStepLogger startStepLogger = new StartStepLogger();
        /* Setup mock */
        Mockito.when(stepMock.getStartTime()).thenReturn(ZonedDateTime.now());
        startStepLogger.proceed(stepMock);
        Mockito.verify(stepMock, Mockito.times(1)).getStartTime();
        Mockito.verify(stepMock, Mockito.times(1)).getDescription();
        Mockito.verify(stepMock, Mockito.times(1)).getResult();
    }
}
