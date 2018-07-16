package com.github.invictum.reportportal.extractor;

import com.github.invictum.reportportal.EnhancedMessage;
import com.github.invictum.reportportal.LogLevel;
import net.thucydides.core.model.TestResult;
import net.thucydides.core.model.TestStep;
import net.thucydides.core.screenshots.ScreenshotAndHtmlSource;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class StepScreenshotsTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Mock
    private TestStep stepMock;

    @Test
    public void noScreenshots() {
        StepScreenshots stepScreenshots = new StepScreenshots();
        Collection<EnhancedMessage> logs = stepScreenshots.extract(stepMock);
        Assert.assertTrue(logs.isEmpty());
    }

    @Test
    public void screenshotPresent() throws IOException {
        StepScreenshots stepsScreenshots = new StepScreenshots();
        ScreenshotAndHtmlSource screenshotMock = Mockito.mock(ScreenshotAndHtmlSource.class);
        Mockito.when(stepMock.getResult()).thenReturn(TestResult.SUCCESS);
        Mockito.when(stepMock.getScreenshots()).thenReturn(Collections.singletonList(screenshotMock));
        Mockito.when(stepMock.getStartTime()).thenReturn(ZonedDateTime.now());
        Mockito.when(screenshotMock.getScreenshot()).thenReturn(folder.newFile("image.png"));
        Collection<EnhancedMessage> logs = stepsScreenshots.extract(stepMock);
        Assert.assertEquals(1, logs.size());
        EnhancedMessage actual = logs.iterator().next();
        Assert.assertEquals("Screenshot", actual.getMessage());
        Assert.assertEquals(LogLevel.INFO, actual.getLevel());
    }
}
