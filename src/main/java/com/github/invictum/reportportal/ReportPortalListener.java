package com.github.invictum.reportportal;

import net.thucydides.core.model.DataTable;
import net.thucydides.core.model.ReportType;
import net.thucydides.core.model.Story;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.steps.ExecutedStepDescription;
import net.thucydides.core.steps.StepFailure;
import net.thucydides.core.steps.StepListener;
import net.thucydides.core.webdriver.ThucydidesWebDriverSupport;
import org.openqa.selenium.logging.Logs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.Map;

public class ReportPortalListener implements StepListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportPortalListener.class);

    private boolean exporterActive = false;
    private final Context context = new Context();

    public ReportPortalListener() {
    }

    private void activateExporterTask() {
        if (context.rpParams.getEnable() && !exporterActive) {
            synchronized (ReportPortalListener.class) {
                if (!exporterActive) {
                    context.startTime = Calendar.getInstance().getTime();
                    Runnable importerTask = new ReportExporter(context);
                    Runtime.getRuntime().addShutdownHook(new Thread(importerTask));
                    exporterActive = true;
                    LOGGER.debug("Exporter task was scheduled");
                }
            }
        }
    }

    public void testSuiteStarted(Class<?> storyClass) {
        activateExporterTask();
    }

    public void testSuiteStarted(Story story) {
        activateExporterTask();
    }

    public void testSuiteFinished() {
        // Not used by listener
    }

    public void testStarted(String description) {
        // Not used by listener
    }

    public void testStarted(String description, String id) {
        // Not used by listener
    }

    public void testFinished(TestOutcome result) {
        String id = result.getUserStory().getPath();
        context.suites.compute(id, (key, old) -> {
            LinkedHashSet<String> set = old == null ? new LinkedHashSet<>() : old;
            set.add(result.getReportName(ReportType.JSON));
            return set;
        });
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
        if (context.rpParams.getEnable() && harvestLogs && ThucydidesWebDriverSupport.isDriverInstantiated()) {
            Logs logs = ThucydidesWebDriverSupport.getDriver().manage().logs();
            context.logStorage.collect(logs);
        }
    }
}
