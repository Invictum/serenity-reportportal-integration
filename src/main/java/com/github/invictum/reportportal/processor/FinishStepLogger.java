package com.github.invictum.reportportal.processor;

import com.epam.reportportal.service.ReportPortal;
import com.github.invictum.reportportal.Utils;
import net.thucydides.core.model.TestStep;

/**
 * Emits details about finished step to Report Portal log facility.
 */
public class FinishStepLogger implements StepProcessor {

    @Override
    public void proceed(final TestStep step) {
        String message = String.format("[%s] %s", step.getResult().name(), step.getDescription());
        ReportPortal.emitLog(message, Utils.logLevel(step.getResult()), Utils.stepEndDate(step));
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FinishStepLogger;
    }
}
