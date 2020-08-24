package com.github.invictum.reportportal.recorder;

import com.epam.reportportal.service.Launch;
import com.epam.reportportal.service.ReportPortal;
import com.epam.ta.reportportal.ws.model.FinishTestItemRQ;
import com.epam.ta.reportportal.ws.model.StartTestItemRQ;
import com.epam.ta.reportportal.ws.model.log.SaveLogRQ;
import com.github.invictum.reportportal.*;
import com.github.invictum.reportportal.log.unit.Error;
import com.google.inject.Inject;
import io.reactivex.Maybe;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.model.TestResult;

import java.util.Collection;

/**
 * Common test recorder suitable for most cases
 */
public class Regular extends TestRecorder {
    static final int RETRIES_COUNT = ReportIntegrationConfig.get().retriesCount();

    @Inject
    public Regular(SuiteStorage suiteStorage, Launch launch, LogUnitsHolder holder) {
        super(suiteStorage, launch, holder);
    }

    @Override
    public void record(TestOutcome out) {
        StartEventBuilder suiteEventBuilder = new StartEventBuilder(ItemType.TEST)
                .withName(out.getUserStory().getDisplayName())
                .withStartTime(out.getStartTime())
                .withDescription(out.getUserStory().getNarrative());
        NarrativeExtractor extractor = new NarrativeExtractor(out, ReportIntegrationConfig.get().formatter());
        extractor.extract().ifPresent(suiteEventBuilder::withDescription);
        StartTestItemRQ startSuite = suiteEventBuilder.build();
        Maybe<String> suiteId = suiteStorage.start(out.getUserStory().getId(), () -> launch.startTestItem(startSuite));
        // Start test
        StartEventBuilder testEventBuilder = new StartEventBuilder(ItemType.STEP)
                .withName(out.getName())
                .withStartTime(out.getStartTime())
                .withTags(out.getTags());
        if (RETRIES_COUNT > 0) {
            processRetries(out, testEventBuilder);
        }
        if (out.isDataDriven()) {
            testEventBuilder.withParameters(out.getDataTable().row(0));
        }
        Maybe<String> testId = launch.startTestItem(suiteId, testEventBuilder.build());
        // Steps
        proceedSteps(testId, out.getTestSteps());
        // failed assertions in test itself
        recordNonStepFailure(out);
        // Stop test
        FinishTestItemRQ finishTest = new FinishEventBuilder()
                .withStatus(Status.mapTo(out.getResult()))
                .withEndTime(out.getStartTime(), out.getDuration())
                .build();
        launch.finishTestItem(testId, finishTest);
        // Finish suite
        FinishTestItemRQ finishSuite = new FinishEventBuilder()
                .withStatus(Status.PASSED)
                .withEndTime(out.getStartTime(), out.getDuration())
                .build();
        suiteStorage.suiteFinisher(out.getUserStory().getId(), () -> launch.finishTestItem(suiteId, finishSuite));
    }

    private void recordNonStepFailure(TestOutcome out) {
        Collection<SaveLogRQ> logs = Error.errorInTest().apply(out);
        logs.forEach(log -> ReportPortal.emitLog(id -> {
            log.setItemUuid(id);
            return log;
        }));
    }

    private boolean isTestFailed(TestOutcome out) {
        TestResult testResult = out.getResult();
        return testResult == TestResult.ERROR || testResult == TestResult.FAILURE;
    }

    private void processRetries(TestOutcome out, StartEventBuilder builder) {
        String testId = out.getId();
        String suiteId = out.getUserStory().getId();
        if (suiteStorage.isFailPresent(suiteId, testId)) {
            builder.withRetry();
            if (!isTestFailed(out)) {
                suiteStorage.removeFail(suiteId, testId);
            }
        } else if (isTestFailed(out)) {
            suiteStorage.addFail(suiteId, testId);
        }
    }
}
