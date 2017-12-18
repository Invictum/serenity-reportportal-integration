package com.github.invictum.reportportal.handler;

import com.epam.reportportal.service.ReportPortal;
import com.epam.ta.reportportal.ws.model.FinishTestItemRQ;
import com.epam.ta.reportportal.ws.model.StartTestItemRQ;
import com.github.invictum.reportportal.*;
import io.reactivex.Maybe;

import java.util.ArrayDeque;
import java.util.Calendar;
import java.util.Deque;

/**
 * Proceed steps as a tree of nested entities. Saves original serenity steps hierarchy.
 */
public class StepTreeHandler extends BaseHandler {

    private ThreadLocal<Deque<Maybe<String>>> steps = ThreadLocal.withInitial(ArrayDeque::new);

    @Override
    public void startStep(EventData stepData) {
        if (test.get().getId() != null) {
            StartTestItemRQ step = new StartTestItemRQ();
            step.setType(ItemType.STEP.key());
            step.setStartTime(Calendar.getInstance().getTime());
            step.setName(stepData.getName());
            step.setDescription(stepData.getDescription());
            step.setTags(stepData.getTags());
            if (steps.get().isEmpty()) {
                /* Steps pool is empty, attach step directly to test */
                steps.get().push(portal.startTestItem(test.get().getId(), step));
            } else {
                /* Steps pool is not empty, attach step directly to the first step */
                Maybe<String> parentId = steps.get().peekFirst();
                steps.get().push(portal.startTestItem(parentId, step));
            }
        }
    }

    @Override
    public void finishStep() {
        if (!steps.get().isEmpty()) {
            FinishTestItemRQ finishStep = new FinishTestItemRQ();
            finishStep.setStatus(Status.PASSED.toString());
            finishStep.setEndTime(Calendar.getInstance().getTime());
            portal.finishTestItem(steps.get().pollFirst(), finishStep);
        }
    }

    @Override
    public void failStep(Throwable throwable) {
        if (!steps.get().isEmpty()) {
            ReportPortal.emitLog(Utils.verboseError(throwable), MessageLevel.ERROR.toString(), Calendar.getInstance()
                    .getTime());
            FinishTestItemRQ failStep = new FinishTestItemRQ();
            failStep.setStatus(Status.FAILED.toString());
            failStep.setEndTime(Calendar.getInstance().getTime());
            portal.finishTestItem(steps.get().pollFirst(), failStep);
        }
    }

    @Override
    public void ignoreStep() {
        ignoreStep("Step is ignored");
    }

    @Override
    public void ignoreStep(String reason) {
        if (!steps.get().isEmpty()) {
            ReportPortal.emitLog("Step is ignored: " + reason, MessageLevel.WARN.toString(), Calendar.getInstance()
                    .getTime());
            FinishTestItemRQ ignoredStep = new FinishTestItemRQ();
            ignoredStep.setStatus(Status.SKIPPED.toString());
            ignoredStep.setEndTime(Calendar.getInstance().getTime());
            portal.finishTestItem(steps.get().pollFirst(), ignoredStep);
        }
    }
}
