package com.github.invictum.reportportal.recorder;

import com.epam.reportportal.service.Launch;
import com.epam.ta.reportportal.ws.model.FinishTestItemRQ;
import com.epam.ta.reportportal.ws.model.StartTestItemRQ;
import com.github.invictum.reportportal.*;
import com.github.invictum.reportportal.injector.IntegrationInjector;
import com.google.inject.Injector;
import io.reactivex.Maybe;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.model.TestStep;

import java.util.List;

/**
 * Defines steps to use to record particular test in context of suite
 */
public abstract class TestRecorder {
    protected SuiteStorage suiteStorage;
    protected Launch launch;
    protected LogUnitsHolder holder;

    public TestRecorder(SuiteStorage suiteStorage, Launch launch, LogUnitsHolder holder) {
        this.suiteStorage = suiteStorage;
        this.launch = launch;
        this.holder = holder;
    }

    public abstract void record(TestOutcome out);

    /**
     * Factory method to discover suitable {@link TestRecorder} approach
     *
     * @param testOutcome to analyze
     * @return recorder instance
     */
    public static TestRecorder forTest(TestOutcome testOutcome) {
        Injector injector = IntegrationInjector.getInjector();
        if (testOutcome.isDataDriven() && testOutcome.getTestSource().toLowerCase().matches("(cucumber|jbehave)")) {
            return injector.getInstance(BddDataDriven.class);
        }
        return injector.getInstance(Regular.class);
    }

    protected void proceedSteps(Maybe<String> parent, List<TestStep> steps) {
        steps.forEach(step -> {
            // Create new step
            StartTestItemRQ startStep = new StartEventBuilder(ItemType.STEP)
                    .withName(step.getDescription())
                    .withStartTime(step.getStartTime())
                    .treeOptimized(true)
                    .build();
            Maybe<String> current = launch.startTestItem(parent, startStep);
            // Proceed current step through log units
            holder.proceed(step);
            // Proceed children if present
            if (step.hasChildren()) {
                proceedSteps(current, step.getChildren());
            }
            // Finish step
            FinishTestItemRQ finishStep = new FinishEventBuilder()
                    .withEndTime(step.getStartTime(), step.getDuration())
                    .withStatus(Status.mapTo(step.getResult()))
                    .build();
            launch.finishTestItem(current, finishStep);
        });
    }
}
