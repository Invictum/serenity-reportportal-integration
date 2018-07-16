package com.github.invictum.reportportal.extractor;

import com.github.invictum.reportportal.EnhancedMessage;
import com.github.invictum.reportportal.LogLevel;
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

import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class HtmlSourcesTest {

    @Mock
    private TestStep stepMock;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void noSources() {
        HtmlSources htmlSources = new HtmlSources();
        Collection<EnhancedMessage> logs = htmlSources.extract(stepMock);
        Assert.assertTrue(logs.isEmpty());
    }

    @Test
    public void sourcesPresent() throws IOException {
        HtmlSources htmlSources = new HtmlSources();
        ScreenshotAndHtmlSource screenshotMock = Mockito.mock(ScreenshotAndHtmlSource.class);
        Mockito.when(stepMock.getScreenshots()).thenReturn(Collections.singletonList(screenshotMock));
        Mockito.when(stepMock.getStartTime()).thenReturn(ZonedDateTime.now());
        Optional<File> source = Optional.of(folder.newFile("source.txt"));
        Mockito.when(screenshotMock.getHtmlSource()).thenReturn(source);
        Collection<EnhancedMessage> logs = htmlSources.extract(stepMock);
        Assert.assertEquals(1, logs.size());
        EnhancedMessage actual = logs.iterator().next();
        Assert.assertEquals("HTML Source", actual.getMessage());
        Assert.assertEquals(LogLevel.WARN, actual.getLevel());
    }
}
