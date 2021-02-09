package com.github.invictum.reportportal.recorder;

import com.epam.reportportal.service.Launch;
import com.epam.ta.reportportal.ws.model.FinishTestItemRQ;
import com.epam.ta.reportportal.ws.model.StartTestItemRQ;
import com.github.invictum.reportportal.*;
import com.google.inject.Inject;
import io.reactivex.Maybe;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.model.TestStep;

import java.util.Arrays;

/**
 * Recorder aware of parameterized BDD style test specific handling
 */
public class BddDataDriven extends TestRecorder {

    @Inject
    public BddDataDriven(SuiteStorage suiteStorage, Launch launch, LogUnitsHolder holder) {
        super(suiteStorage, launch, holder);
    }

    @Override
    public void record(TestOutcome out) {
        int last = out.getTestSteps().size() - 1;
        TestStep test = out.getTestSteps().get(last);
        StartTestItemRQ startStory = new StartEventBuilder(ItemType.TEST)
                .withName(out.getUserStory().getName())
                .withStartTime(test.getStartTime())
                .withDescription(out.getUserStory().getNarrative())
                .build();
        Maybe<String> id = suiteStorage.start(out.getUserStory().getId(), () -> launch.startTestItem(startStory));
        // Start test
        StartTestItemRQ startScenario = new StartEventBuilder(ItemType.STEP)
                .withName(out.getName())
                .withStartTime(test.getStartTime())
                .withParameters(out.getDataTable().row(last))
                .withTags(out.getTags())
                .build();
        Maybe<String> testId = launch.startTestItem(id, startScenario);
        // Steps
        proceedSteps(testId, Arrays.asList(test));
        // Stop test
        FinishTestItemRQ finishScenario = new FinishEventBuilder()
                .withStatus(Status.mapTo(test.getResult()))
                .withEndTime(test.getStartTime(), test.getDuration())
                .build();
        launch.finishTestItem(testId, finishScenario);
        // Finish suite
        FinishTestItemRQ finishStory = new FinishEventBuilder()
                .withStatus(Status.PASSED)
                .withEndTime(test.getStartTime(), test.getDuration())
                .build();
        suiteStorage.suiteFinisher(out.getUserStory().getId(), () -> launch.finishTestItem(id, finishStory));
    }
}
