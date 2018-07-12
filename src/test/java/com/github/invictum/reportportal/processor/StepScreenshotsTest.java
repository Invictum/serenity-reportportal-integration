package com.github.invictum.reportportal.processor;

import net.thucydides.core.model.TestStep;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class StepScreenshotsTest {

    @Mock
    private TestStep stepMock;

    @Test
    public void noScreenshotsTest() {
        StepScreenshots stepScreenshots = new StepScreenshots();
        stepScreenshots.extract(stepMock);
        Mockito.verify(stepMock, Mockito.never()).getResult();
    }
}
