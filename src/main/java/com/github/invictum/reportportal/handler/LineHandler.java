package com.github.invictum.reportportal.handler;

import com.epam.reportportal.service.ReportPortal;
import com.epam.ta.reportportal.ws.model.FinishTestItemRQ;
import com.epam.ta.reportportal.ws.model.StartTestItemRQ;
import com.github.invictum.reportportal.*;
import com.google.inject.Inject;
import io.reactivex.Maybe;

import java.util.ArrayDeque;
import java.util.Calendar;
import java.util.Deque;

public class LineHandler implements Handler {

    private ThreadLocal<Maybe<String>> suite = new ThreadLocal<>();
    private ThreadLocal<Test> test = ThreadLocal.withInitial(Test::new);
    private ThreadLocal<Deque<Maybe<String>>> steps = ThreadLocal.withInitial(ArrayDeque::new);

    @Inject
    private ReportPortal portal;

    /**
     * Initializes handler.
     * Usually used to init Report Portal communication facility.
     */
    @Override
    public void init() {
    }

    /**
     * Starts new suite.
     *
     * @param suiteData related to new suite
     */
    @Override
    public void startSuite(EventData suiteData) {
        if (suite.get() == null) {
            StartTestItemRQ startSuite = new StartTestItemRQ();
            startSuite.setType(ItemType.SUITE.key());
            startSuite.setName(suiteData.getName());
            startSuite.setStartTime(Calendar.getInstance().getTime());
            startSuite.setDescription(suiteData.getDescription());
            startSuite.setTags(suiteData.getTags());
            suite.set(portal.startTestItem(startSuite));
        }
    }

    /**
     * Finishes current active suite.
     */
    @Override
    public void finishSuite() {
        if (suite.get() != null) {
            FinishTestItemRQ finishSuite = new FinishTestItemRQ();
            finishSuite.setEndTime(Calendar.getInstance().getTime());
            finishSuite.setStatus(Status.PASSED.toString());
            portal.finishTestItem(suite.get(), finishSuite);
            suite.remove();
        }
    }

    /**
     * Start new test.
     *
     * @param testData related to new test
     */
    @Override
    public void startTest(EventData testData) {
        if (test.get().getId() == null) {
            StartTestItemRQ startTest = new StartTestItemRQ();
            startTest.setType(ItemType.TEST.key());
            startTest.setName(testData.getName());
            startTest.setStartTime(Calendar.getInstance().getTime());
            startTest.setDescription(testData.getDescription());
            startTest.setTags(testData.getTags());
            test.get().setId(portal.startTestItem(suite.get(), startTest));
        }
    }

    /**
     * Finishes currently active test.
     */
    @Override
    public void finishTest() {
        if (test.get().getId() != null) {
            FinishTestItemRQ finishTest = new FinishTestItemRQ();
            finishTest.setEndTime(Calendar.getInstance().getTime());
            finishTest.setStatus(test.get().getStatus().toString());
            portal.finishTestItem(test.get().getId(), finishTest);
            test.remove();
        }
    }

    /**
     * Updates current active test status to FAILED. Attach related stack trace.
     *
     * @param cause fail test occur.
     */
    @Override
    public void failTest(Throwable cause) {
        test.get().setStatus(Status.FAILED);
        String errorMessage = Utils.verboseError(cause);
        ReportPortal.emitLog(errorMessage, MessageLevel.ERROR.toString(), Calendar.getInstance().getTime());
    }

    /**
     * Updates currently active test status to SKIPPED.
     */
    @Override
    public void ignoreTest() {
        test.get().setStatus(Status.SKIPPED);
    }

    /**
     * Starts a new step.
     *
     * @param stepData related to new step
     */
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

    /**
     * Finishes current active step.
     */
    @Override
    public void finishStep() {
        if (!steps.get().isEmpty()) {
            FinishTestItemRQ finishStep = new FinishTestItemRQ();
            finishStep.setStatus(Status.PASSED.toString());
            finishStep.setEndTime(Calendar.getInstance().getTime());
            portal.finishTestItem(steps.get().pollFirst(), finishStep);
        }
    }

    /**
     * Finishes current step with FAILED status. Attach related stack trace.
     *
     * @param throwable root cause.
     */
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

    /**
     * Finishes current active step with SKIPPED status.
     */
    @Override
    public void ignoreStep() {
        ignoreStep("Step is ignored");
    }

    /**
     * Finishes current active step with SKIPPED status.
     *
     * @param reason to skip current step.
     */
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
