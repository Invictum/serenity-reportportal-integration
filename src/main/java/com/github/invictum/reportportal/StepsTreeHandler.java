package com.github.invictum.reportportal;

import com.epam.reportportal.service.ReportPortal;
import com.epam.ta.reportportal.ws.model.FinishTestItemRQ;
import com.epam.ta.reportportal.ws.model.StartTestItemRQ;
import io.reactivex.Maybe;

import java.util.ArrayDeque;
import java.util.Calendar;
import java.util.Deque;

/**
 * Handles steps as a tree of nested entities.
 * Realization supposed to be thread-safe.
 */
public class StepsTreeHandler extends BasicHandler {

    private ThreadLocal<Deque<Maybe<String>>> steps = ThreadLocal.withInitial(ArrayDeque::new);

    @Override
    public void startStep(StartTestItemRQ stepDetails) {
        if (activeTest.get() != null) {
            stepDetails.setType("STEP");
            stepDetails.setStartTime(Calendar.getInstance().getTime());
            if (steps.get().isEmpty()) {
                steps.get().push(portal.startTestItem(activeTest.get(), stepDetails));
            } else {
                steps.get().push(portal.startTestItem(steps.get().peekFirst(), stepDetails));
            }
        }
    }

    @Override
    public void finishStep() {
        if (steps.get().peekFirst() != null) {
            FinishTestItemRQ finishStep = new FinishTestItemRQ();
            finishStep.setEndTime(Calendar.getInstance().getTime());
            finishStep.setStatus("PASSED");
            portal.finishTestItem(steps.get().pollFirst(), finishStep);
        }
    }

    @Override
    public void failStep(Throwable throwable) {
        if (steps.get().peekFirst() != null) {
            ReportPortal.emitLog(Utils.verboseError(throwable), "ERROR", Calendar.getInstance().getTime());
            FinishTestItemRQ finishStep = new FinishTestItemRQ();
            finishStep.setEndTime(Calendar.getInstance().getTime());
            finishStep.setStatus("FAILED");
            portal.finishTestItem(steps.get().pollFirst(), finishStep);
        }
    }

    @Override
    public void ignoreStep() {
        if (steps.get().peekFirst() != null) {
            FinishTestItemRQ finishStep = new FinishTestItemRQ();
            finishStep.setEndTime(Calendar.getInstance().getTime());
            finishStep.setStatus("SKIPPED");
            portal.finishTestItem(steps.get().pollFirst(), finishStep);
        }
    }

    @Override
    public void ignoreStep(String reason) {
        if (steps.get().peekFirst() != null) {
            FinishTestItemRQ ignoredStep = new FinishTestItemRQ();
            ignoredStep.setDescription(reason);
            ignoredStep.setEndTime(Calendar.getInstance().getTime());
            ignoredStep.setStatus("SKIPPED");
            portal.finishTestItem(steps.get().pollFirst(), ignoredStep);
        }
    }
}
