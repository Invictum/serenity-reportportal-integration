package com.github.invictum.reportportal;

import com.github.invictum.reportportal.handler.FlatHandler;
import com.github.invictum.reportportal.handler.Handler;
import com.github.invictum.reportportal.handler.HandlerType;
import com.github.invictum.reportportal.handler.TreeHandler;
import com.github.invictum.reportportal.injector.IntegrationInjector;
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

    private Handler handler;
    private LogStorage logStorage;

    public ReportPortalListener() {
        handler = (ReportIntegrationConfig.handlerType == HandlerType.FLAT) ? new FlatHandler() : new TreeHandler();
        logStorage = IntegrationInjector.getInjector().getInstance(LogStorage.class);
    }

    public void testSuiteStarted(Class<?> storyClass) {
        handler.startSuite(storyClass);
    }

    public void testSuiteStarted(Story story) {
        handler.startSuite(story);
    }

    public void testSuiteFinished() {
        handler.finishSuite();
    }

    public void testStarted(String description) {
        handler.startTest(description);
    }

    public void testStarted(String description, String id) {
        testStarted(description);
    }

    public void testFinished(TestOutcome result) {
        handler.finishTest(result);
        logStorage.clean();
    }

    public void testRetried() {
        /* Not used by listener */
    }

    public void stepStarted(ExecutedStepDescription description) {
        /* Not used by listener */
    }

    public void skippedStepStarted(ExecutedStepDescription description) {
        /* Not used by listener */
    }

    public void stepFailed(StepFailure failure) {
        /* Not used by listener */
        collectDriverLogs();
    }

    public void lastStepFailed(StepFailure failure) {
        /* Not used by listener */
        collectDriverLogs();
    }

    public void stepIgnored() {
        /* Not used by listener */
        collectDriverLogs();
    }

    public void stepPending() {
        /* Not used by listener */
        collectDriverLogs();
    }

    public void stepPending(String message) {
        /* Not used by listener */
        collectDriverLogs();
    }

    public void stepFinished() {
        /* Not used by listener */
        collectDriverLogs();
    }

    public void testFailed(TestOutcome testOutcome, Throwable cause) {
        /* Not used by listener */
    }

    public void testIgnored() {
        /* Not used by listener */
    }

    public void testSkipped() {
        /* Not used by listener */
    }

    public void testPending() {
        /* Not used by listener */
    }

    public void testIsManual() {
        /* Not used by listener */
    }

    public void notifyScreenChange() {
        /* Not used by listener */
    }

    public void useExamplesFrom(DataTable table) {
        /* Not used by listener */
    }

    public void addNewExamplesFrom(DataTable table) {
        /* Not used by listener */
    }

    public void exampleStarted(Map<String, String> data) {
        /* Not used by listener */
    }

    public void exampleFinished() {
        /* Not used by listener */
    }

    public void assumptionViolated(String message) {
        /* Not used by listener */
    }

    public void testRunFinished() {
        /* Not used by listener */
    }

    private void collectDriverLogs() {
        if (ThucydidesWebDriverSupport.isInitialised()) {
            Logs logs = ThucydidesWebDriverSupport.getDriver().manage().logs();
            logStorage.collect(logs);
        }
    }
}
