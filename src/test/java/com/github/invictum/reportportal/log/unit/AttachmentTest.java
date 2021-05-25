package com.github.invictum.reportportal.log.unit;

import com.epam.ta.reportportal.ws.model.log.SaveLogRQ;
import com.github.invictum.reportportal.LogLevel;
import net.serenitybdd.core.environment.ConfiguredEnvironment;
import net.thucydides.core.model.ReportData;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class AttachmentTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Mock
    private TestStep stepMock;

    @Test
    public void noScreenshots() {
        Collection<SaveLogRQ> logs = Attachment.screenshots().apply(stepMock);
        Assert.assertTrue(logs.isEmpty());
    }

    @Test
    public void screenshotPresent() throws IOException {
        ScreenshotAndHtmlSource screenshotMock = Mockito.mock(ScreenshotAndHtmlSource.class);
        Mockito.when(stepMock.getResult()).thenReturn(TestResult.SUCCESS);
        Mockito.when(stepMock.getScreenshots()).thenReturn(Collections.singletonList(screenshotMock));
        Mockito.when(stepMock.getStartTime()).thenReturn(ZonedDateTime.now());
        Mockito.when(screenshotMock.getScreenshot()).thenReturn(folder.newFile("image.png"));
        Collection<SaveLogRQ> logs = Attachment.screenshots().apply(stepMock);
        Assert.assertEquals(1, logs.size());
        SaveLogRQ actual = logs.iterator().next();
        Assert.assertEquals("Screenshot", actual.getMessage());
        Assert.assertEquals(LogLevel.INFO.toString(), actual.getLevel());
    }

    @Test
    public void noSources() {
        Collection<SaveLogRQ> logs = Attachment.htmlSources().apply(stepMock);
        Assert.assertTrue(logs.isEmpty());
    }

    @Test
    public void sourcesPresent() throws IOException {
        ScreenshotAndHtmlSource screenshotMock = Mockito.mock(ScreenshotAndHtmlSource.class);
        Mockito.when(stepMock.getScreenshots()).thenReturn(Collections.singletonList(screenshotMock));
        Mockito.when(stepMock.getStartTime()).thenReturn(ZonedDateTime.now());
        Optional<File> source = Optional.of(folder.newFile("source.txt"));
        Mockito.when(screenshotMock.getHtmlSource()).thenReturn(source);
        Collection<SaveLogRQ> logs = Attachment.htmlSources().apply(stepMock);
        Assert.assertEquals(1, logs.size());
        SaveLogRQ actual = logs.iterator().next();
        Assert.assertEquals("HTML Source", actual.getMessage());
        Assert.assertEquals(LogLevel.FATAL.toString(), actual.getLevel());
    }

    @Test
    public void noEvidences() {
        Collection<SaveLogRQ> logs = Attachment.evidences().apply(stepMock);
        Assert.assertTrue(logs.isEmpty());
    }

    @Test
    public void evidenceFromContents() {
        ReportData reportData = new ReportData("title", "content", null, true);
        Mockito.when(stepMock.getReportEvidence()).thenReturn(Collections.singletonList(reportData));
        Mockito.when(stepMock.getStartTime()).thenReturn(ZonedDateTime.now());
        Collection<SaveLogRQ> logs = Attachment.evidences().apply(stepMock);
        Assert.assertEquals(1, logs.size());
        SaveLogRQ actual = logs.iterator().next();
        Assert.assertEquals("title", actual.getMessage());
        Assert.assertEquals("content", new String(actual.getFile().getContent()));
        Assert.assertEquals(LogLevel.DEBUG.toString(), actual.getLevel());
    }

    @Test
    public void evidenceFromFile() throws IOException {
        File evidence = folder.newFile("note.txt");
        Files.write(evidence.toPath(), Collections.singleton("content"));
        ReportData reportData = new ReportData("note", null, "note.txt", true);
        Mockito.when(stepMock.getReportEvidence()).thenReturn(Collections.singletonList(reportData));
        Mockito.when(stepMock.getStartTime()).thenReturn(ZonedDateTime.now());
        ConfiguredEnvironment.getConfiguration().setOutputDirectory(folder.getRoot());
        Collection<SaveLogRQ> logs = Attachment.evidences().apply(stepMock);
        Assert.assertEquals(1, logs.size());
        SaveLogRQ actual = logs.iterator().next();
        Assert.assertEquals("note", actual.getMessage());
        Assert.assertEquals("content" + System.lineSeparator(), new String(actual.getFile().getContent()));
        Assert.assertEquals(LogLevel.DEBUG.toString(), actual.getLevel());
    }
}
