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

import java.util.Collection;

/**
 * Common test recorder suitable for most cases
 */
public class Regular extends TestRecorder {

    @Inject
    public Regular(SuiteStorage suiteStorage, Launch launch, LogUnitsHolder holder) {
        super(suiteStorage, launch, holder);
    }

    @Override
    public void record(TestOutcome out) {
        StartTestItemRQ startSuite = new StartEventBuilder(ItemType.TEST)
                .withName(out.getUserStory().getDisplayName())
                .withStartTime(out.getStartTime())
                .withDescription(out.getUserStory().getNarrative())
                .build();
        Maybe<String> id = suiteStorage.start(out.getUserStory().getId(), () -> launch.startTestItem(startSuite));
        StartEventBuilder builder = new StartEventBuilder(ItemType.STEP);
        builder.withName(out.getName()).withStartTime(out.getStartTime()).withTags(out.getTags());
        if (out.isDataDriven()) {
            builder.withParameters(out.getDataTable().row(0));
        }
        Maybe<String> testId = launch.startTestItem(id, builder.build());
        // Steps
        proceedSteps(testId, out.getTestSteps());
        // failed assertions in test itself
        recordNonStepFailure(out);
        FinishTestItemRQ finishTest = new FinishEventBuilder()
                .withStatus(Status.mapTo(out.getResult()))
                .withEndTime(out.getStartTime(), out.getDuration())
                .build();
        launch.finishTestItem(testId, finishTest);
        FinishTestItemRQ finishSuite = new FinishEventBuilder()
                .withStatus(Status.PASSED)
                .withEndTime(out.getStartTime(), out.getDuration())
                .build();
        suiteStorage.suiteFinisher(out.getUserStory().getId(), () -> launch.finishTestItem(id, finishSuite));
    }

    private void recordNonStepFailure(TestOutcome out) {
        Collection<SaveLogRQ> logs = Error.errorInTest().apply(out);
        logs.forEach(log -> ReportPortal.emitLog(id -> {
            log.setItemUuid(id);
            return log;
        }));
    }
}
