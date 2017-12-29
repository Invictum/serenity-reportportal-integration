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
import java.util.Optional;

/**
 * Attaches HTML sources to Report Portal log if present
 */
public class HtmlSourceAttacher implements StepProcessor {

    private final static Logger LOG = LoggerFactory.getLogger(HtmlSourceAttacher.class);

    @Override
    public void proceed(final TestStep step) {
        if (!step.getScreenshots().isEmpty()) {
            Date stepStartTime = Date.from(step.getStartTime().toInstant());
            for (ScreenshotAndHtmlSource screenshotAndHtmlSource : step.getScreenshots()) {
                Optional<File> sourceFile = screenshotAndHtmlSource.getHtmlSource().toJavaUtil();
                if (sourceFile.isPresent()) {
                    Date timestamp = sourceFile.get().lastModified() < stepStartTime
                            .getTime() ? stepStartTime : new Date(sourceFile.get().lastModified());
                    try {
                        ReportPortalMessage message = new ReportPortalMessage(sourceFile.get(), "HTML Source");
                        ReportPortal.emitLog(message, Utils.logLevel(step.getResult()), timestamp);
                    } catch (IOException e) {
                        LOG.error("Failed to attach sources");
                    }
                }
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof HtmlSourceAttacher;
    }
}
