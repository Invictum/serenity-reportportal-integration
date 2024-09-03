package com.github.invictum.reportportal.recorder;

import net.thucydides.model.domain.TestOutcome;

/**
 * Allows to transform particular test name in ReportPortal according to specific user requirements.
 */
public interface TestNameTransformer {

    /**
     * @param testOutcome   original test data
     * @param scenarioIndex index of scenario which is being reported
     * @return transformed test name
     */
    String transformName(TestOutcome testOutcome, int scenarioIndex);
}
