package com.github.invictum.reportportal.handler;

import com.github.invictum.reportportal.EventData;
import com.github.invictum.reportportal.MessageLevel;
import com.github.invictum.reportportal.Utils;

import java.util.Calendar;

import static com.epam.reportportal.service.ReportPortal.emitLog;

/**
 * Proceed steps as a sequence of logs inside a test. Deflates Serenity steps hierarchy.
 */
public class StepFlatHandler extends BaseHandler {

    @Override
    public void startStep(EventData stepData) {
        emitLog("[STEP] " + stepData.getName(), MessageLevel.INFO.toString(), Calendar.getInstance().getTime());
    }

    @Override
    public void finishStep() {
        emitLog("[STEP] Finished", MessageLevel.DEBUG.toString(), Calendar.getInstance().getTime());
    }

    @Override
    public void failStep(Throwable throwable) {
        emitLog("[STEP] Failed", MessageLevel.ERROR.toString(), Calendar.getInstance().getTime());
        emitLog(Utils.verboseError(throwable), MessageLevel.ERROR.toString(), Calendar.getInstance().getTime());
    }

    @Override
    public void ignoreStep() {
        ignoreStep("Ignored without reason details");
    }

    @Override
    public void ignoreStep(String reason) {
        emitLog("[STEP] Ignored. Reason " + reason, MessageLevel.WARN.toString(), Calendar.getInstance().getTime());
    }
}
