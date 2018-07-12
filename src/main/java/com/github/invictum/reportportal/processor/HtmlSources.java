package com.github.invictum.reportportal.processor;

import com.github.invictum.reportportal.EnhancedMessage;
import com.github.invictum.reportportal.Utils;
import net.thucydides.core.model.TestStep;
import net.thucydides.core.screenshots.ScreenshotAndHtmlSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Extracts HTML sources, if present
 */
public class HtmlSources implements StepDataExtractor {

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
                        EnhancedMessage message = new EnhancedMessage(sourceFile.get(), "Screenshot");
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

    @Override
    public boolean equals(Object obj) {
        return obj instanceof HtmlSources;
    }
}
