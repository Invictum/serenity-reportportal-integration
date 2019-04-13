package com.github.invictum.reportportal.recorder;

import com.epam.reportportal.service.Launch;
import com.epam.ta.reportportal.ws.model.FinishTestItemRQ;
import com.epam.ta.reportportal.ws.model.StartTestItemRQ;
import com.github.invictum.reportportal.*;
import com.google.inject.Inject;
import io.reactivex.Maybe;
import net.thucydides.core.model.TestOutcome;

/**
 * Common test recorder suitable for most cases
 */
public class Regular implements TestRecorder {

    private SuiteStorage suiteStorage;
    private Launch launch;
    private LogUnitsHolder holder;

    @Inject
    public Regular(SuiteStorage suiteStorage, Launch launch, LogUnitsHolder holder) {
        this.suiteStorage = suiteStorage;
        this.launch = launch;
        this.holder = holder;
    }

    @Override
    public void record(TestOutcome out) {
        StartTestItemRQ startSuite = new StartEventBuilder(ItemType.SUITE)
                .withName(out.getUserStory().getDisplayName())
                .withStartTime(out.getStartTime())
                .withDescription(out.getUserStory().getNarrative())
                .build();
        Maybe<String> id = suiteStorage.start(out.getUserStory().getId(), () -> launch.startTestItem(startSuite));
        StartEventBuilder builder = new StartEventBuilder(ItemType.TEST);
        builder.withName(out.getName()).withStartTime(out.getStartTime()).withTags(out.getTags());
        if (out.isDataDriven()) {
            builder.withParameters(out.getDataTable().row(0));
        }
        Maybe<String> testId = launch.startTestItem(id, builder.build());
        // Steps
        out.getFlattenedTestSteps().forEach(holder::proceed);
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
}
