package com.github.invictum.reportportal.handler;

import com.github.invictum.reportportal.EventData;

/**
 * Interface represents operations related to interactions between Serenity and ReportPortal.
 */
public interface Handler {
    /**
     * Starts new suite.
     *
     * @param suiteData related to new suite
     */
    void startSuite(EventData suiteData);

    /**
     * Finishes current active suite.
     */
    void finishSuite();

    /**
     * Start new test.
     *
     * @param testData related to new test
     */
    void startTest(EventData testData);

    /**
     * Finishes currently active test.
     */
    void finishTest();

    /**
     * Updates current active test status to FAILED. Attach related stack trace.
     *
     * @param cause fail test occur.
     */
    void failTest(Throwable cause);

    /**
     * Updates currently active test status to SKIPPED.
     */
    void ignoreTest();

    /**
     * Starts a new step.
     *
     * @param stepData related to new step
     */
    void startStep(EventData stepData);

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

    /**
     * Finishes current active step with SKIPPED status.
     *
     * @param reason to skip current step.
     */
    void ignoreStep(String reason);

}
