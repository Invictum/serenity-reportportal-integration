package com.github.invictum.reportportal;

import com.epam.ta.reportportal.ws.model.StartTestItemRQ;

/**
 * Interface represents operations related to interactions between Serenity and ReportPortal.
 */
public interface Handler {

    /**
     * Initializes handler.
     * Usually used to init Report Portal communication facility.
     */
    void init();

    void startSuite(StartTestItemRQ suiteDetails);

    /**
     * Finishes current active suite if open one is present. Otherwise nothing happens.
     */
    void finishSuite();

    /**
     * Start new active test if possible. If there is another active test method do nothing.
     *
     * @param testDetails related to new test
     */
    void startTest(StartTestItemRQ testDetails);

    /**
     * Finishes currently active test if possible. Otherwise nothing happens.
     */
    void finishTest();

    /**
     * Finish current active test with FAILED status. Attach related stack trace.
     *
     * @param cause fail test occur.
     */
    void failTest(Throwable cause);

    /**
     * Finishes currently active test with SKIPPED status.
     */
    void ignoreTest();

    /**
     * Starts a new step.
     *
     * @param stepDetails related to new step
     */
    void startStep(StartTestItemRQ stepDetails);

    /**
     * Finishes current active step.
     */
    void finishStep();

    /**
     * Finishes current step with FAILED status. Attach related stack trace.
     *
     * @param throwable root cause.
     */
    void failStep(Throwable throwable);

    /**
     * Finishes current active step with SKIPPED status.
     */
    void ignoreStep();

}
