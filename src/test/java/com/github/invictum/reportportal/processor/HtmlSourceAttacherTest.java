package com.github.invictum.reportportal.processor;

import net.thucydides.core.model.TestStep;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class HtmlSourceAttacherTest {

    @Mock
    private TestStep stepMock;

    @Test
    public void noScreenshotsTest() {
        HtmlSourceAttacher htmlSourceAttacher = new HtmlSourceAttacher();
        htmlSourceAttacher.proceed(stepMock);
        Mockito.verify(stepMock, Mockito.never()).getResult();
    }
}
