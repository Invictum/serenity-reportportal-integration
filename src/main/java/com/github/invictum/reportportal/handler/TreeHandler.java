package com.github.invictum.reportportal.handler;

import com.epam.ta.reportportal.ws.model.FinishTestItemRQ;
import com.epam.ta.reportportal.ws.model.StartTestItemRQ;
import com.github.invictum.reportportal.ItemType;
import com.github.invictum.reportportal.Status;
import com.github.invictum.reportportal.Utils;
import io.reactivex.Maybe;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.model.TestStep;

import java.time.Duration;
import java.util.Date;
import java.util.List;

/**
 * Handler builds Serenity's {@link TestOutcome} in Report Portal as tree representation.
 * Nested steps structure is respected accordingly.
 */
public class TreeHandler extends FlatHandler {

    @Override
    public void finishTest(TestOutcome result) {
        if (testId != null) {
            /* Proceed all steps */
            push(testId, result.getTestSteps());
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

    private void push(Maybe<String> parent, List<TestStep> steps) {
        steps.forEach(testStep -> {
            /* Create new step */
            StartTestItemRQ startTest = new StartTestItemRQ();
            startTest.setName(testStep.getDescription());
            startTest.setType(ItemType.TEST.key());
            startTest.setStartTime(Utils.stepStartDate(testStep));
            Maybe<String> current = launch.startTestItem(parent, startTest);
            /* Proceed children if present */
            if (testStep.hasChildren()) {
                push(current, testStep.getChildren());
            }
            /* Finish step */
            FinishTestItemRQ finishTest = new FinishTestItemRQ();
            finishTest.setEndTime(Utils.stepEndDate(testStep));
            finishTest.setStatus(Status.mapTo(testStep.getResult()).toString());
            launch.finishTestItem(current, finishTest);
        });
    }
}
