package com.github.invictum.reportportal.processor;

import net.thucydides.core.model.TestStep;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.ZonedDateTime;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class StartStepTest {

    @Mock
    private TestStep stepMock;

    @Test
    public void startStepLoggerTest() {
        StartStep startStep = new StartStep();
        /* Setup mock */
        Mockito.when(stepMock.getStartTime()).thenReturn(ZonedDateTime.now());
        startStep.extract(stepMock);
        Mockito.verify(stepMock, Mockito.times(1)).getStartTime();
        Mockito.verify(stepMock, Mockito.times(1)).getDescription();
        Mockito.verify(stepMock, Mockito.times(1)).getResult();
    }
}
