package com.github.invictum.reportportal;

import com.epam.reportportal.service.Launch;
import com.epam.ta.reportportal.ws.model.FinishTestItemRQ;
import com.epam.ta.reportportal.ws.model.StartTestItemRQ;
import com.github.invictum.reportportal.injector.IntegrationInjector;
import com.google.inject.Inject;
import io.reactivex.Maybe;
import net.thucydides.core.model.DataTable;
import net.thucydides.core.model.Story;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.requirements.annotations.NarrativeFinder;
import net.thucydides.core.steps.ExecutedStepDescription;
import net.thucydides.core.steps.StepFailure;
import net.thucydides.core.steps.StepListener;

import java.time.Duration;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportPortalListener implements StepListener {

    @Inject
    private Launch launch;

    @Inject
    private StepProcessorsHolder holder;

    private Maybe<String> suiteId = null;
    private Maybe<String> testId = null;

    public ReportPortalListener() {
        IntegrationInjector.getInjector().injectMembers(this);
    }

    public void testSuiteStarted(Class<?> storyClass) {
        if (suiteId == null) {
            StartTestItemRQ startSuite = new StartTestItemRQ();
            startSuite.setType(ItemType.SUITE.key());
            startSuite.setName(storyClass.getSimpleName());
            startSuite.setStartTime(Calendar.getInstance().getTime());
            /* Add narrative to description if present */
            if (NarrativeFinder.forClass(storyClass).isPresent()) {
                String description = Arrays.stream(NarrativeFinder.forClass(storyClass).get().text())
                        .collect(Collectors.joining(" "));
                startSuite.setDescription(description);
            }
            suiteId = launch.startTestItem(startSuite);
        }
    }

    public void testSuiteStarted(Story story) {
        if (suiteId == null) {
            StartTestItemRQ startSuite = new StartTestItemRQ();
            startSuite.setType(ItemType.SUITE.key());
            startSuite.setName(story.getDisplayName());
            startSuite.setStartTime(Calendar.getInstance().getTime());
            startSuite.setDescription(story.getNarrative());
            suiteId = launch.startTestItem(startSuite);
        }
    }

    public void testSuiteFinished() {
        if (suiteId != null) {
            FinishTestItemRQ finishSuite = new FinishTestItemRQ();
            finishSuite.setEndTime(Calendar.getInstance().getTime());
            finishSuite.setStatus(Status.PASSED.toString());
            launch.finishTestItem(suiteId, finishSuite);
            suiteId = null;
        }
    }

    public void testStarted(String description) {
        if (testId == null) {
            StartTestItemRQ startTest = new StartTestItemRQ();
            startTest.setType(ItemType.TEST.key());
            startTest.setName(description);
            startTest.setStartTime(Calendar.getInstance().getTime());
            testId = launch.startTestItem(suiteId, startTest);
        }
    }

    public void testStarted(String description, String id) {
        testStarted(description);
    }

    public void testFinished(TestOutcome result) {
        if (testId != null) {
            /* Proceed all steps */
            result.getFlattenedTestSteps().forEach(step -> holder.proceed(step));
            /* Finish active test */
            FinishTestItemRQ finishTest = new FinishTestItemRQ();
            Date endDate = Date.from(result.getStartTime().plus(Duration.ofMillis(result.getDuration())).toInstant());
            finishTest.setEndTime(endDate);
            finishTest.setStatus(Status.mapTo(result.getResult()).toString());
            finishTest.setTags(Utils.refineTags(result));
            launch.finishTestItem(testId, finishTest);
            testId = null;
        }
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
    }

    public void lastStepFailed(StepFailure failure) {
        /* Not used by listener */
    }

    public void stepIgnored() {
        /* Not used by listener */
    }

    public void stepPending() {
        /* Not used by listener */
    }

    public void stepPending(String message) {
        /* Not used by listener */
    }

    public void stepFinished() {
        /* Not used by listener */
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
}
