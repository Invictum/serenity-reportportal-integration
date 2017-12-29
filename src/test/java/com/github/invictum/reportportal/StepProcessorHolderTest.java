package com.github.invictum.reportportal;

import com.github.invictum.reportportal.processor.StartStepLogger;
import net.thucydides.core.model.TestStep;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class StepProcessorHolderTest {

    @Mock
    private StartStepLogger processorMock;

    @Test
    public void proceedTest() {
        StepProcessorsHolder holder = new StepProcessorsHolder();
        holder.register(processorMock);
        TestStep step = new TestStep();
        holder.proceed(step);
        Mockito.verify(processorMock, Mockito.times(1)).proceed(step);
    }
}
