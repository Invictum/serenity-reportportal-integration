package com.github.invictum.reportportal;

import com.github.invictum.reportportal.injector.IntegrationInjector;
import com.github.invictum.reportportal.recorder.TestRecorder;
import com.google.inject.Inject;
import net.thucydides.core.model.DataTable;
import net.thucydides.core.model.Story;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.steps.ExecutedStepDescription;
import net.thucydides.core.steps.StepFailure;
import net.thucydides.core.steps.StepListener;
import net.thucydides.core.webdriver.ThucydidesWebDriverSupport;
import org.openqa.selenium.logging.Logs;

import java.util.Map;

public class ReportPortalListener implements StepListener {

    @Inject
    private LogStorage logStorage;

    @Inject
    private SuiteStorage suiteStorage;

    public ReportPortalListener() {
        IntegrationInjector.getInjector().injectMembers(this);
    }

    public void testSuiteStarted(Class<?> storyClass) {
        // Not used by listener
    }

    public void testSuiteStarted(Story story) {
        // Not used by listener
    }

    public void testSuiteFinished() {
        suiteStorage.finalizeActive();
    }

    public void testStarted(String description) {
        // Not used by listener
    }

    public void testStarted(String description, String id) {
        // Not used by listener
    }

    public void testFinished(TestOutcome result) {
        TestRecorder recorder = TestRecorder.forTest(result);
        recorder.record(result);
        logStorage.clean();
    }

    public void testRetried() {
        // Not used by listener
    }

    public void stepStarted(ExecutedStepDescription description) {
        // Not used by listener
    }

    public void skippedStepStarted(ExecutedStepDescription description) {
        // Not used by listener
    }

    public void stepFailed(StepFailure failure) {
        harvestDriverLogs();
    }

    public void lastStepFailed(StepFailure failure) {
        // Not used by listener
    }

    public void stepIgnored() {
        // Not used by listener
    }

    public void stepPending() {
        // Not used by listener
    }

    public void stepPending(String message) {
        // Not used by listener
    }

    public void stepFinished() {
        harvestDriverLogs();
    }

    public void testFailed(TestOutcome testOutcome, Throwable cause) {
        // Not used by listener
    }

    public void testIgnored() {
        // Not used by listener
    }

    public void testSkipped() {
        // Not used by listener
    }

    public void testPending() {
        // Not used by listener
    }

    public void testIsManual() {
        // Not used by listener
    }

    public void notifyScreenChange() {
        // Not used by listener
    }

    public void useExamplesFrom(DataTable table) {
        // Not used by listener
    }

    public void addNewExamplesFrom(DataTable table) {
        // Not used by listener
    }

    public void exampleStarted(Map<String, String> data) {
        // Not used by listener
    }

    public void exampleFinished() {
        // Not used by listener
    }

    public void assumptionViolated(String message) {
        // Not used by listener
    }

    public void testRunFinished() {
        // Not used by listener
    }

    private void harvestDriverLogs() {
        boolean harvestLogs = ReportIntegrationConfig.get().harvestSeleniumLogs;
        if (harvestLogs && ThucydidesWebDriverSupport.isDriverInstantiated()) {
            Logs logs = ThucydidesWebDriverSupport.getDriver().manage().logs();
            logStorage.collect(logs);
        }
    }
}
