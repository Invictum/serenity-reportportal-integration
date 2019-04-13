package com.github.invictum.reportportal.recorder;

import com.github.invictum.reportportal.injector.IntegrationInjector;
import com.google.inject.Injector;
import net.thucydides.core.model.TestOutcome;

/**
 * Defines steps to use to record particular test in context of suite
 */
public interface TestRecorder {

    void record(TestOutcome out);

    /**
     * Factory method to discover suitable {@link TestRecorder} approach
     *
     * @param testOutcome to analyze
     * @return recorder instance
     */
    static TestRecorder forTest(TestOutcome testOutcome) {
        Injector injector = IntegrationInjector.getInjector();
        if (testOutcome.isDataDriven() && testOutcome.getTestSource().toLowerCase().matches("(cucumber|jbehave)")) {
            return injector.getInstance(BddDataDriven.class);
        }
        return injector.getInstance(Regular.class);
    }
}
