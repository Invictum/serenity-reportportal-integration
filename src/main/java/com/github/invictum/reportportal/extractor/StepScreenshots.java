package com.github.invictum.reportportal.extractor;

import com.github.invictum.reportportal.EnhancedMessage;
import com.github.invictum.reportportal.Utils;
import net.thucydides.core.model.TestStep;
import net.thucydides.core.screenshots.ScreenshotAndHtmlSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Extracts screenshots from {@link TestStep}, if present
 */
public class StepScreenshots implements StepDataExtractor {

    private final static Logger LOG = LoggerFactory.getLogger(StepScreenshots.class);

    @Override
    public Collection<EnhancedMessage> extract(final TestStep step) {
        Set<EnhancedMessage> messages = new HashSet<>();
        if (!step.getScreenshots().isEmpty()) {
            Date stepStartTime = Date.from(step.getStartTime().toInstant());
            for (ScreenshotAndHtmlSource screenshotAndHtmlSource : step.getScreenshots()) {
                File screenshotFile = screenshotAndHtmlSource.getScreenshot();
                Date timestamp = screenshotFile.lastModified() < stepStartTime.getTime() ?
                        stepStartTime : new Date(screenshotFile.lastModified());
                try {
                    EnhancedMessage message = new EnhancedMessage(screenshotFile, "Screenshot");
                    message.withDate(timestamp).withLevel(Utils.logLevel(step.getResult()));
                    messages.add(message);
                } catch (IOException e) {
                    LOG.error("Unable to attach screenshot");
                }
            }
        }
        return messages;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof StepScreenshots;
    }
}
