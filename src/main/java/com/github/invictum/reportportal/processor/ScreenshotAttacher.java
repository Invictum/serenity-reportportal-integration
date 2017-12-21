package com.github.invictum.reportportal.processor;

import com.epam.reportportal.message.ReportPortalMessage;
import com.epam.reportportal.service.ReportPortal;
import com.github.invictum.reportportal.Utils;
import net.thucydides.core.model.TestStep;
import net.thucydides.core.screenshots.ScreenshotAndHtmlSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Attaches screenshots to Report Portal log if present
 */
public class ScreenshotAttacher implements StepProcessor {

    private final static Logger LOG = LoggerFactory.getLogger(ScreenshotAttacher.class);

    @Override
    public void proceed(final TestStep step) {
        if (!step.getScreenshots().isEmpty()) {
            Date stepStartTime = Date.from(step.getStartTime().toInstant());
            for (ScreenshotAndHtmlSource screenshotAndHtmlSource : step.getScreenshots()) {
                File screenshotFile = screenshotAndHtmlSource.getScreenshot();
                Date timestamp = screenshotFile.lastModified() < stepStartTime
                        .getTime() ? stepStartTime : new Date(screenshotFile.lastModified());
                try {
                    ReportPortalMessage message = new ReportPortalMessage(screenshotFile, "Screenshot");
                    ReportPortal.emitLog(message, Utils.logLevel(step.getResult()), timestamp);
                } catch (IOException e) {
                    LOG.error("Failed to attach screenshot");
                }
            }
        }
    }
}
