package com.github.invictum.reportportal.handler;

import com.epam.reportportal.service.Launch;
import com.epam.ta.reportportal.ws.model.FinishTestItemRQ;
import com.epam.ta.reportportal.ws.model.StartTestItemRQ;
import com.github.invictum.reportportal.*;
import com.google.common.base.Suppliers;
import com.google.inject.Inject;
import io.reactivex.Maybe;
import net.thucydides.core.annotations.Narrative;
import net.thucydides.core.model.Story;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.requirements.annotations.NarrativeFinder;

import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Handler builds Serenity's {@link TestOutcome} in Report Portal as flat sequence of logs
 */
public class FlatHandler implements Handler {

    @Inject
    Launch launch;

    @Inject
    LogUnitsHolder holder;

    Maybe<String> testId;
    private Supplier<Maybe<String>> suite;

    @Override
    public void startSuite(Class<?> storyClass) {
        Date date = Calendar.getInstance().getTime();
        suite = Suppliers.memoize(() -> {
            StartTestItemRQ startSuite = new StartTestItemRQ();
            startSuite.setType(ItemType.SUITE.key());
            startSuite.setName(storyClass.getSimpleName());
            startSuite.setStartTime(date);
            /* Add narrative to description if present */
            NarrativeFinder.forClass(storyClass).ifPresent(narrative -> {
                Function<Narrative, String> narrativeFormatter = ReportIntegrationConfig.get().narrativeFormatter();
                startSuite.setDescription(narrativeFormatter.apply(narrative));
            });
            return launch.startTestItem(startSuite);
        });
    }

    @Override
    public void startSuite(Story story) {
        Date date = Calendar.getInstance().getTime();
        suite = Suppliers.memoize(() -> {
            StartTestItemRQ startSuite = new StartTestItemRQ();
            startSuite.setType(ItemType.SUITE.key());
            startSuite.setName(story.getDisplayName());
            startSuite.setStartTime(date);
            startSuite.setDescription(story.getNarrative());
            return launch.startTestItem(startSuite);
        });
    }

    @Override
    public void finishSuite() {
        FinishTestItemRQ finishSuite = new FinishTestItemRQ();
        finishSuite.setEndTime(Calendar.getInstance().getTime());
        finishSuite.setStatus(Status.PASSED.toString());
        launch.finishTestItem(suite.get(), finishSuite);
    }

    @Override
    public void startTest(String description) {
        if (testId == null) {
            StartTestItemRQ startTest = new StartTestItemRQ();
            startTest.setType(ItemType.TEST.key());
            startTest.setName(description);
            startTest.setStartTime(Calendar.getInstance().getTime());
            testId = launch.startTestItem(suite.get(), startTest);
        }
    }

    @Override
    public void finishTest(TestOutcome result) {
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
}
