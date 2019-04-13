package com.github.invictum.reportportal.recorder;

import com.epam.reportportal.service.Launch;
import com.epam.ta.reportportal.ws.model.FinishTestItemRQ;
import com.epam.ta.reportportal.ws.model.StartTestItemRQ;
import com.github.invictum.reportportal.*;
import com.google.inject.Inject;
import io.reactivex.Maybe;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.model.TestStep;

/**
 * Recorder aware of parameterized BDD style test specific handling
 */
public class BddDataDriven implements TestRecorder {

    private SuiteStorage suiteStorage;
    private Launch launch;
    private LogUnitsHolder holder;

    @Inject
    public BddDataDriven(SuiteStorage suiteStorage, Launch launch, LogUnitsHolder holder) {
        this.suiteStorage = suiteStorage;
        this.launch = launch;
        this.holder = holder;
    }

    @Override
    public void record(TestOutcome out) {
        int last = out.getTestSteps().size() - 1;
        TestStep test = out.getTestSteps().get(last);
        StartTestItemRQ startSuite = new StartEventBuilder(ItemType.SUITE)
                .withName(out.getUserStory().getName())
                .withStartTime(test.getStartTime())
                .withDescription(out.getUserStory().getNarrative())
                .build();
        Maybe<String> id = suiteStorage.start(out.getUserStory().getId(), () -> launch.startTestItem(startSuite));
        // Start test
        StartTestItemRQ startTest = new StartEventBuilder(ItemType.TEST)
                .withName(out.getName())
                .withStartTime(test.getStartTime())
                .withParameters(out.getDataTable().row(last))
                .withTags(out.getTags())
                .build();
        Maybe<String> testId = launch.startTestItem(id, startTest);
        // Steps
        test.getFlattenedSteps().forEach(holder::proceed);
        // Stop test
        FinishTestItemRQ finishTest = new FinishEventBuilder()
                .withStatus(Status.mapTo(test.getResult()))
                .withEndTime(test.getStartTime(), test.getDuration())
                .build();
        launch.finishTestItem(testId, finishTest);
        // Finish suite
        FinishTestItemRQ finishSuite = new FinishEventBuilder()
                .withStatus(Status.PASSED)
                .withEndTime(test.getStartTime(), test.getDuration())
                .build();
        suiteStorage.suiteFinisher(out.getUserStory().getId(), () -> launch.finishTestItem(id, finishSuite));
    }
}
