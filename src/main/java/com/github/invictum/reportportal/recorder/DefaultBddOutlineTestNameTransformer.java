package com.github.invictum.reportportal.recorder;

import net.thucydides.model.domain.TestOutcome;

/**
 * Default implementation of name as seen in Serenity.
 */
public class DefaultBddOutlineTestNameTransformer implements TestNameTransformer {

    @Override
    public String transformName(TestOutcome testOutcome, int scenarioIndex) {
        return testOutcome.getName();
    }
}
