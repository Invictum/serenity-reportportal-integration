package com.github.invictum.reportportal.log.unit;

import com.epam.ta.reportportal.ws.model.log.SaveLogRQ;
import com.github.invictum.reportportal.LogLevel;
import net.serenitybdd.model.environment.ConfiguredEnvironment;
import net.thucydides.model.domain.ReportData;
import net.thucydides.model.domain.TestResult;
import net.thucydides.model.domain.TestStep;
import net.thucydides.model.screenshots.ScreenshotAndHtmlSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AttachmentTest {

    @TempDir
    Path tempDir;

    @Mock
    private TestStep stepMock;

    @Test
    public void noScreenshots() {
        Collection<SaveLogRQ> logs = Attachment.screenshots().apply(stepMock);
        Assertions.assertTrue(logs.isEmpty());
    }

    @Test
    public void screenshotPresent() throws IOException {
        ScreenshotAndHtmlSource screenshotMock = Mockito.mock(ScreenshotAndHtmlSource.class);
        Mockito.when(stepMock.getResult()).thenReturn(TestResult.SUCCESS);
        Mockito.when(stepMock.getScreenshots()).thenReturn(Collections.singletonList(screenshotMock));
        Mockito.when(stepMock.getStartTime()).thenReturn(ZonedDateTime.now());
        Path screenshotPath = tempDir.resolve("image.png");
        Files.createFile(screenshotPath);
        Mockito.when(screenshotMock.getScreenshot()).thenReturn(screenshotPath.toFile());
        Collection<SaveLogRQ> logs = Attachment.screenshots().apply(stepMock);
        Assertions.assertEquals(1, logs.size());
        SaveLogRQ actual = logs.iterator().next();
        Assertions.assertEquals("Screenshot", actual.getMessage());
        Assertions.assertEquals(LogLevel.INFO.toString(), actual.getLevel());
    }

    @Test
    public void noSources() {
        Collection<SaveLogRQ> logs = Attachment.htmlSources().apply(stepMock);
        Assertions.assertTrue(logs.isEmpty());
    }

    @Test
    public void sourcesPresent() throws IOException {
        ScreenshotAndHtmlSource screenshotMock = Mockito.mock(ScreenshotAndHtmlSource.class);
        Mockito.when(stepMock.getScreenshots()).thenReturn(Collections.singletonList(screenshotMock));
        Mockito.when(stepMock.getStartTime()).thenReturn(ZonedDateTime.now());
        Path sourcePath = tempDir.resolve("source.txt");
        Files.createFile(sourcePath);
        Optional<File> source = Optional.of(sourcePath.toFile());
        Mockito.when(screenshotMock.getHtmlSource()).thenReturn(source);
        Collection<SaveLogRQ> logs = Attachment.htmlSources().apply(stepMock);
        Assertions.assertEquals(1, logs.size());
        SaveLogRQ actual = logs.iterator().next();
        Assertions.assertEquals("HTML Source", actual.getMessage());
        Assertions.assertEquals(LogLevel.FATAL.toString(), actual.getLevel());
    }

    @Test
    public void noEvidences() {
        Collection<SaveLogRQ> logs = Attachment.evidences().apply(stepMock);
        Assertions.assertTrue(logs.isEmpty());
    }

    @Test
    public void evidenceFromContents() {
        ReportData reportData = new ReportData("title", "content", null, true);
        Mockito.when(stepMock.getReportEvidence()).thenReturn(Collections.singletonList(reportData));
        Mockito.when(stepMock.getStartTime()).thenReturn(ZonedDateTime.now());
        Collection<SaveLogRQ> logs = Attachment.evidences().apply(stepMock);
        Assertions.assertEquals(1, logs.size());
        SaveLogRQ actual = logs.iterator().next();
        Assertions.assertEquals("title", actual.getMessage());
        Assertions.assertEquals("content", new String(actual.getFile().getContent()));
        Assertions.assertEquals(LogLevel.DEBUG.toString(), actual.getLevel());
    }

    @Test
    public void evidenceFromFile() throws IOException {
        Path evidencePath = tempDir.resolve("note.txt");
        Files.write(evidencePath, Collections.singleton("content"));
        ReportData reportData = new ReportData("note", null, "note.txt", true);
        Mockito.when(stepMock.getReportEvidence()).thenReturn(Collections.singletonList(reportData));
        Mockito.when(stepMock.getStartTime()).thenReturn(ZonedDateTime.now());
        ConfiguredEnvironment.getConfiguration().setOutputDirectory(tempDir.toFile());
        Collection<SaveLogRQ> logs = Attachment.evidences().apply(stepMock);
        Assertions.assertEquals(1, logs.size());
        SaveLogRQ actual = logs.iterator().next();
        Assertions.assertEquals("note", actual.getMessage());
        Assertions.assertEquals("content" + System.lineSeparator(), new String(actual.getFile().getContent()));
        Assertions.assertEquals(LogLevel.DEBUG.toString(), actual.getLevel());
    }
}
