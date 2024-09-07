package com.github.invictum.reportportal.recorder;

import net.thucydides.model.domain.TestOutcome;

/**
 * Default implementation of name as seen in Serenity.
 */
public class DefaultBddOutlineTestNameProvider implements TestNameProvider {

    @Override
    public String provideName(TestOutcome testOutcome, int scenarioIndex) {
        return testOutcome.getName();
    }
}
