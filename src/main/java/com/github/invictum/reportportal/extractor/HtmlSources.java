package com.github.invictum.reportportal.extractor;

import com.github.invictum.reportportal.EnhancedMessage;
import com.github.invictum.reportportal.Utils;
import net.thucydides.core.model.TestStep;
import net.thucydides.core.screenshots.ScreenshotAndHtmlSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rp.com.google.common.io.ByteSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

/**
 * Extracts details about HTML sources from {@link TestStep} if present
 */
public class HtmlSources implements StepDataExtractor {

    private final static String MIME = "text/plain";
    private final static Logger LOG = LoggerFactory.getLogger(HtmlSources.class);

    @Override
    public Collection<EnhancedMessage> extract(final TestStep step) {
        Set<EnhancedMessage> sources = new HashSet<>();
        if (!step.getScreenshots().isEmpty()) {
            Date stepStartTime = Date.from(step.getStartTime().toInstant());
            for (ScreenshotAndHtmlSource screenshotAndHtmlSource : step.getScreenshots()) {
                Optional<File> sourceFile = screenshotAndHtmlSource.getHtmlSource();
                if (sourceFile.isPresent()) {
                    Date timestamp = sourceFile.get().lastModified() < stepStartTime
                            .getTime() ? stepStartTime : new Date(sourceFile.get().lastModified());
                    try {
                        byte[] data = Files.readAllBytes(sourceFile.get().toPath());
                        EnhancedMessage message = new EnhancedMessage(ByteSource.wrap(data), MIME, "HTML Source");
                        message.withDate(timestamp).withLevel(Utils.logLevel(step.getResult()));
                        sources.add(message);
                    } catch (IOException e) {
                        LOG.error("Failed to attach sources");
                    }
                }
            }
        }
        return sources;
    }
}
