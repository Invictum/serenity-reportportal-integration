package com.github.invictum.reportportal;

import com.github.invictum.reportportal.extractor.StartStep;
import net.thucydides.core.model.TestStep;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class StepDataExtractorsHolderTest {

    @Mock
    private StartStep extractorMock;

    @Mock
    private TestStep stepMock;

    @Test
    public void proceedTest() {
        StepDataExtractorsHolder holder = new StepDataExtractorsHolder();
        holder.register(extractorMock);
        // Handle clone case
        Mockito.when(stepMock.clone()).thenReturn(stepMock);
        holder.proceed(stepMock);
        Mockito.verify(extractorMock, Mockito.times(1)).extract(stepMock);
    }
}
