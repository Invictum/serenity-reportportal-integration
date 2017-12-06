package com.github.invictum.reportportal;

import net.thucydides.core.model.DataTable;
import net.thucydides.core.model.Story;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.steps.ExecutedStepDescription;
import net.thucydides.core.steps.StepFailure;
import net.thucydides.core.steps.StepListener;
import org.junit.Ignore;

import java.util.Map;

public class ReportPortalListener implements StepListener {

    /**
     * Start a test run using a test case or a user story.
     * For JUnit tests, the test case should be provided. The test case should be annotated with the
     * Story annotation to indicate what user story it tests. Otherwise, the test case itself will
     * be treated as a user story.
     * For easyb stories, the story class can be provided directly.
     *
     * @param storyClass
     */
    public void testSuiteStarted(Class<?> storyClass) {

    }

    /**
     * Start a test run using a specific story, without a corresponding Java class.
     *
     * @param story
     */
    public void testSuiteStarted(Story story) {

    }

    /**
     * End of a test case or story.
     */
    public void testSuiteFinished() {

    }

    /**
     * A test with a given name has started.
     *
     * @param description
     */
    public void testStarted(String description) {

    }

    public void testStarted(String description, String id) {

    }

    /**
     * Called when a test finishes.
     *
     * @param result
     */
    public void testFinished(TestOutcome result) {

    }

    /**
     * The last test run is about to be restarted
     */
    public void testRetried() {

    }

    /**
     * Called when a test step is about to be started.
     *
     * @param description the description of the test that is about to be run
     *                    (generally a class and method name)
     */
    public void stepStarted(ExecutedStepDescription description) {

    }

    /**
     * Called when a test step is about to be started, but this step is scheduled to be skipped.
     *
     * @param description the description of the test that is about to be run
     *                    (generally a class and method name)
     */
    public void skippedStepStarted(ExecutedStepDescription description) {

    }

    /**
     * Called when a test step fails.
     *
     * @param failure describes the test that failed and the exception that was thrown
     */
    public void stepFailed(StepFailure failure) {

    }

    /**
     * Declare that a step has failed after it has finished.
     *
     * @param failure
     */
    public void lastStepFailed(StepFailure failure) {

    }

    /**
     * Called when a step will not be run, generally because a test method is annotated
     * with {@link Ignore}.
     */
    public void stepIgnored() {

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

    /**
     * Called when an test step has finished successfully
     */
    public void stepFinished() {

    }

    /**
     * The test failed, but not while executing a step.
     *
     * @param testOutcome The test outcome structure for the failing test
     * @param cause       The exception that triggered the failure
     */
    public void testFailed(TestOutcome testOutcome, Throwable cause) {

    }

    /**
     * The test as a whole was ignored.
     */
    public void testIgnored() {

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
