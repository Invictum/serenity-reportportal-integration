package com.github.invictum.reportportal.recorder;

import net.thucydides.model.domain.TestOutcome;

/**
 * Allows to transform particular test name in ReportPortal according to specific user requirements.
 */
public interface TestNameProvider {

    /**
     * @param testOutcome   original test data
     * @param scenarioIndex index of scenario which is being reported
     * @return transformed test name
     */
    String provideName(TestOutcome testOutcome, int scenarioIndex);
}
