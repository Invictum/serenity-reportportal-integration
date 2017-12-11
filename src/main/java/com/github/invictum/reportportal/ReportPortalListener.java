package com.github.invictum.reportportal;

import com.epam.ta.reportportal.ws.model.StartTestItemRQ;
import com.google.inject.Guice;
import net.thucydides.core.model.DataTable;
import net.thucydides.core.model.Story;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.steps.ExecutedStepDescription;
import net.thucydides.core.steps.StepFailure;
import net.thucydides.core.steps.StepListener;

import java.util.Map;

public class ReportPortalListener implements StepListener {

    private Handler handler = Guice.createInjector(new SerenityPortalModule()).getInstance(Handler.class);

    public ReportPortalListener() {
        handler.init();
    }

    public void testSuiteStarted(Class<?> storyClass) {
        StartTestItemRQ suiteDetails = new StartTestItemRQ();
        suiteDetails.setName(storyClass.getSimpleName());
        handler.startSuite(suiteDetails);
    }

    public void testSuiteStarted(Story story) {
        StartTestItemRQ suiteDetails = new StartTestItemRQ();
        suiteDetails.setName(story.getDisplayName());
        handler.startSuite(suiteDetails);
    }

    public void testSuiteFinished() {
        handler.finishSuite();
    }

    public void testStarted(String description) {
        StartTestItemRQ testDetails = new StartTestItemRQ();
        testDetails.setName(description);
        handler.startTest(testDetails);
    }

    public void testStarted(String description, String id) {
        StartTestItemRQ testDetails = new StartTestItemRQ();
        testDetails.setName(description);
        handler.startTest(testDetails);
    }

    public void testFinished(TestOutcome result) {
        handler.finishTest();
    }

    /**
     * The last test run is about to be restarted
     */
    public void testRetried() {
    }

    public void stepStarted(ExecutedStepDescription description) {
        StartTestItemRQ stepItem = new StartTestItemRQ();
        stepItem.setName(description.getTitle());
        handler.startStep(stepItem);
    }

    public void skippedStepStarted(ExecutedStepDescription description) {
        StartTestItemRQ stepItem = new StartTestItemRQ();
        stepItem.setName(description.getTitle());
        handler.startStep(stepItem);
    }

    public void stepFailed(StepFailure failure) {
        handler.failStep(failure.getException());
    }

    /**
     * Declare that a step has failed after it has finished.
     *
     * @param failure
     */
    public void lastStepFailed(StepFailure failure) {
    }

    public void stepIgnored() {
        handler.ignoreStep();
    }

    /**
     * The step is marked as pending.
     */
    public void stepPending() {
    }

    /**
     * The step is marked as pending with a descriptive message.
     *
     * @param message
     */
    public void stepPending(String message) {
    }

    public void stepFinished() {
        handler.finishStep();
    }

    public void testFailed(TestOutcome testOutcome, Throwable cause) {
        handler.failTest(cause);
    }

    public void testIgnored() {
        handler.ignoreTest();
    }

    /**
     * The test as a whole was skipped.
     */
    public void testSkipped() {
    }

    /**
     * The test as a whole should be marked as 'pending'.
     */
    public void testPending() {
    }

    public void testIsManual() {
    }

    public void notifyScreenChange() {
    }

    /**
     * The current scenario is a data-driven scenario using test data from the specified table.
     *
     * @param table
     */
    public void useExamplesFrom(DataTable table) {
    }

    /**
     * If multiple tables are used, this method will add any new rows to the test data
     *
     * @param table
     */
    public void addNewExamplesFrom(DataTable table) {
    }

    /**
     * A new example has just started.
     *
     * @param data
     */
    public void exampleStarted(Map<String, String> data) {
    }

    /**
     * An example has finished.
     */
    public void exampleFinished() {
    }

    public void assumptionViolated(String message) {
    }

    public void testRunFinished() {
    }
}
