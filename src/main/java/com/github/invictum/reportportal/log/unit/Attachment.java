package com.github.invictum.reportportal.log.unit;

import com.epam.ta.reportportal.ws.model.log.SaveLogRQ;
import com.github.invictum.reportportal.LogLevel;
import com.github.invictum.reportportal.Utils;
import net.serenitybdd.core.environment.ConfiguredEnvironment;
import net.thucydides.core.model.ReportData;
import net.thucydides.core.model.TestStep;
import net.thucydides.core.screenshots.ScreenshotAndHtmlSource;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Attachment {

    private final static Logger LOG = LoggerFactory.getLogger(Attachment.class);
    private final static Tika TIKA = new Tika();

    /**
     * Logs screenshots from passed {@link TestStep} if present
     */
    public static Function<TestStep, Collection<SaveLogRQ>> screenshots() {
        return step -> {
            Set<SaveLogRQ> logs = new HashSet<>();
            if (!step.getScreenshots().isEmpty()) {
                Date stepStartTime = Date.from(step.getStartTime().toInstant());
                for (ScreenshotAndHtmlSource screenshotAndHtmlSource : step.getScreenshots()) {
                    File screenshotFile = screenshotAndHtmlSource.getScreenshot();
                    Date timestamp = screenshotFile.lastModified() < stepStartTime.getTime() ?
                            stepStartTime : new Date(screenshotFile.lastModified());
                    try {
                        byte[] data = Files.readAllBytes(screenshotFile.toPath());
                        SaveLogRQ.File screenshot = new SaveLogRQ.File();
                        screenshot.setContent(data);
                        screenshot.setName(screenshotFile.getName());
                        screenshot.setContentType("image/png");
                        SaveLogRQ log = new SaveLogRQ();
                        log.setFile(screenshot);
                        log.setMessage("Screenshot");
                        log.setLogTime(timestamp);
                        log.setLevel(Utils.logLevel(step.getResult()));
                        logs.add(log);
                    } catch (IOException e) {
                        LOG.error("Unable to attach screenshot");
                    }
                }
            }
            return logs;
        };
    }

    /**
     * Logs all HTML sources from passed {@link TestStep} if present
     */
    public static Function<TestStep, Collection<SaveLogRQ>> htmlSources() {
        return step -> {
            Set<SaveLogRQ> sources = new HashSet<>();
            if (!step.getScreenshots().isEmpty()) {
                Date stepStartTime = Date.from(step.getStartTime().toInstant());
                for (ScreenshotAndHtmlSource screenshotAndHtmlSource : step.getScreenshots()) {
                    Optional<File> sourceFile = screenshotAndHtmlSource.getHtmlSource();
                    sourceFile.ifPresent(file -> {
                        Date timestamp = file.lastModified() < stepStartTime.getTime() ? stepStartTime
                                : new Date(file.lastModified());
                        try {
                            byte[] data = Files.readAllBytes(file.toPath());
                            // Build RP file entity
                            SaveLogRQ.File attachment = new SaveLogRQ.File();
                            attachment.setContent(data);
                            attachment.setName(file.getName());
                            attachment.setContentType("text/plain");
                            // build RP log entity
                            SaveLogRQ log = new SaveLogRQ();
                            log.setFile(attachment);
                            log.setMessage("HTML Source");
                            log.setLevel(Utils.logLevel(step.getResult()));
                            log.setLogTime(timestamp);
                            sources.add(log);
                        } catch (IOException e) {
                            LOG.error("Failed to attach HTML sources");
                        }
                    });
                }
            }
            return sources;
        };
    }

    /**
     * Logs all evidences in a test step as attachment files in RP
     */
    public static Function<TestStep, Collection<SaveLogRQ>> evidences() {
        return step -> step.getReportEvidence().stream()
                .filter(ReportData::isEvidence)
                .map(report -> report.getPath() == null ? fromContent(report) : fromFile(report))
                .filter(Objects::nonNull)
                .peek(log -> log.setLogTime(Utils.stepEndDate(step)))
                .collect(Collectors.toSet());
    }

    private static SaveLogRQ fromFile(ReportData data) {
        SaveLogRQ.File file = new SaveLogRQ.File();
        file.setName(data.getId());
        try {
            String reportRoot = ConfiguredEnvironment.getConfiguration().getOutputDirectory().toString();
            Path path = Paths.get(reportRoot, data.getPath());
            byte[] content = Files.readAllBytes(path);
            file.setContent(content);
            String mime = TIKA.detect(content, data.getPath());
            file.setContentType(mime);
        } catch (IOException e) {
            LOG.error("Unable to attach evidence", e);
            return null;
        }
        SaveLogRQ log = new SaveLogRQ();
        log.setMessage(data.getTitle());
        log.setFile(file);
        log.setLevel(LogLevel.DEBUG.toString());
        return log;
    }

    private static SaveLogRQ fromContent(ReportData data) {
        SaveLogRQ.File file = new SaveLogRQ.File();
        file.setName(data.getId());
        file.setContentType("application/txt");
        file.setContent(data.getContents().getBytes());
        SaveLogRQ log = new SaveLogRQ();
        log.setFile(file);
        log.setMessage(data.getTitle());
        log.setLevel(LogLevel.DEBUG.toString());
        return log;
    }
}
