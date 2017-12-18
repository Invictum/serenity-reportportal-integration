package com.github.invictum.reportportal;

import com.github.invictum.reportportal.handler.Handler;
import net.thucydides.core.model.DataTable;
import net.thucydides.core.model.Story;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.steps.ExecutedStepDescription;
import net.thucydides.core.steps.StepFailure;
import net.thucydides.core.steps.StepListener;

import java.util.Map;

public class ReportPortalListener implements StepListener {

    private Handler handler = IntegrationInjector.getInjector().getInstance(Handler.class);

    public void testSuiteStarted(Class<?> storyClass) {
        EventData suiteData = new EventData();
        suiteData.setName(storyClass.getSimpleName());
        handler.startSuite(suiteData);
    }

    public void testSuiteStarted(Story story) {
        EventData suiteData = new EventData();
        suiteData.setName(story.getDisplayName());
        handler.startSuite(suiteData);
    }

    public void testSuiteFinished() {
        handler.finishSuite();
    }

    public void testStarted(String description) {
        EventData testData = new EventData();
        testData.setName(description);
        handler.startTest(testData);
    }

    public void testStarted(String description, String id) {
        EventData testData = new EventData();
        testData.setName(description);
        handler.startTest(testData);
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
        EventData stepData = new EventData();
        stepData.setName(description.getTitle());
        handler.startStep(stepData);
    }

    public void skippedStepStarted(ExecutedStepDescription description) {
        EventData stepData = new EventData();
        stepData.setName(description.getTitle());
        handler.startStep(stepData);
    }

    public void stepFailed(StepFailure failure) {
        handler.failStep(failure.getException());
    }

    /**
     * Declare that a step has failed after it has finished.
     *
     * @param failure failure
     */
    public void lastStepFailed(StepFailure failure) {
    }

    public void stepIgnored() {
        handler.ignoreStep();
    }

    public void stepPending() {
        handler.ignoreStep();
    }

    public void stepPending(String message) {
        handler.ignoreStep(message);
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

    public void testSkipped() {
        handler.ignoreTest();
    }

    public void testPending() {
        handler.ignoreTest();
    }

    public void testIsManual() {
    }

    public void notifyScreenChange() {
    }

    /**
     * The current scenario is a data-driven scenario using test data from the specified table.
     *
     * @param table table
     */
    public void useExamplesFrom(DataTable table) {
    }

    /**
     * If multiple tables are used, this method will add any new rows to the test data
     *
     * @param table table
     */
    public void addNewExamplesFrom(DataTable table) {
    }

    /**
     * A new example has just started.
     *
     * @param data data
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
