package com.github.invictum.reportportal;

import com.github.invictum.reportportal.injector.IntegrationInjector;
import com.github.invictum.reportportal.recorder.TestRecorder;
import com.google.inject.Inject;
import net.thucydides.core.webdriver.ThucydidesWebDriverSupport;
import net.thucydides.model.domain.DataTable;
import net.thucydides.model.domain.Story;
import net.thucydides.model.domain.TestOutcome;
import net.thucydides.model.domain.TestResult;
import net.thucydides.model.screenshots.ScreenshotAndHtmlSource;
import net.thucydides.model.steps.ExecutedStepDescription;
import net.thucydides.model.steps.StepFailure;
import net.thucydides.model.steps.StepListener;
import org.openqa.selenium.logging.Logs;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

public class ReportPortalListener implements StepListener {

    @Inject
    private LogStorage logStorage;

    @Inject
    private SuiteStorage suiteStorage;

    public ReportPortalListener() {
        IntegrationInjector.getInjector().injectMembers(this);
    }

    @Override
    public void testSuiteStarted(Class<?> aClass) {
        // Not used by listener
    }

    @Override
    public void testSuiteStarted(Story story) {
        // Not used by listener
    }

    @Override
    public void testSuiteFinished() {
        suiteStorage.finalizeActive();
    }

    @Override
    public void testStarted(String s) {
        // Not used by listener
    }

    @Override
    public void testStarted(String s, String s1) {
        // Not used by listener
    }

    @Override
    public void testStarted(String s, String s1, ZonedDateTime zonedDateTime) {
        // Not used by listener
    }

    @Override
    public void testFinished(TestOutcome testOutcome) {
        TestRecorder recorder = TestRecorder.forTest(testOutcome);
        recorder.record(testOutcome);
        logStorage.clean();
    }

    @Override
    public void testFinished(TestOutcome testOutcome, boolean b, ZonedDateTime zonedDateTime) {
        testFinished(testOutcome);
    }

    @Override
    public void testRetried() {
        // Not used by listener
    }

    @Override
    public void stepStarted(ExecutedStepDescription executedStepDescription) {
        // Not used by listener
    }

    @Override
    public void skippedStepStarted(ExecutedStepDescription executedStepDescription) {
        // Not used by listener
    }

    @Override
    public void stepFailed(StepFailure stepFailure) {
        harvestDriverLogs();
    }

    @Override
    public void stepFailed(StepFailure stepFailure, List<ScreenshotAndHtmlSource> list) {
        harvestDriverLogs();
    }

    @Override
    public void lastStepFailed(StepFailure stepFailure) {
        // Not used by listener
    }

    @Override
    public void stepIgnored() {
        // Not used by listener
    }

    @Override
    public void stepPending() {
        // Not used by listener
    }

    @Override
    public void stepPending(String s) {
        // Not used by listener
    }

    @Override
    public void stepFinished() {
        harvestDriverLogs();
    }

    @Override
    public void stepFinished(List<ScreenshotAndHtmlSource> list, ZonedDateTime zonedDateTime) {
        harvestDriverLogs();
    }

    @Override
    public void testFailed(TestOutcome testOutcome, Throwable throwable) {
        // Not used by listener
    }

    @Override
    public void testIgnored() {
        // Not used by listener
    }

    @Override
    public void testSkipped() {
        // Not used by listener
    }

    @Override
    public void testPending() {
        // Not used by listener
    }

    @Override
    public void testIsManual() {
        // Not used by listener
    }

    @Override
    public void notifyScreenChange() {
        // Not used by listener
    }

    @Override
    public void useExamplesFrom(DataTable dataTable) {
        // Not used by listener
    }

    @Override
    public void addNewExamplesFrom(DataTable dataTable) {
        // Not used by listener
    }

    @Override
    public void exampleStarted(Map<String, String> data) {
        // Not used by listener
    }

    @Override
    public void exampleFinished() {
        // Not used by listener
    }

    @Override
    public void assumptionViolated(String message) {
        // Not used by listener
    }

    @Override
    public void testRunFinished() {
        // Not used by listener
    }

    @Override
    public void takeScreenshots(List<ScreenshotAndHtmlSource> list) {
        // Not used by listener
    }

    @Override
    public void takeScreenshots(TestResult testResult, List<ScreenshotAndHtmlSource> list) {
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
