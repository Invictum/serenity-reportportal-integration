package com.github.invictum.reportportal.processor;

import com.epam.reportportal.service.ReportPortal;
import com.github.invictum.reportportal.Utils;
import net.thucydides.core.model.TestStep;

import java.util.Date;

/**
 * Emits new log for Report Portal for each started step
 */
public class StartStepLogger implements StepProcessor {

    @Override
    public void proceed(final TestStep step) {
        Date startDate = Date.from(step.getStartTime().toInstant());
        ReportPortal.emitLog("[STARTED] " + step.getDescription(), Utils.logLevel(step.getResult()), startDate);
    }
}
