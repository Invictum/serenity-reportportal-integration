package com.github.invictum.reportportal.processor;

import com.github.invictum.reportportal.EnhancedMessage;
import com.github.invictum.reportportal.Utils;
import net.thucydides.core.model.TestStep;

import java.util.Collection;
import java.util.Collections;

/**
 * Extracts details about finished step
 */
public class FinishStep implements StepDataExtractor {

    @Override
    public Collection<EnhancedMessage> extract(final TestStep step) {
        String text = String.format("[%s] %s", step.getResult().name(), step.getDescription());
        EnhancedMessage message = new EnhancedMessage(text);
        message.withDate(Utils.stepEndDate(step)).withLevel(Utils.logLevel(step.getResult()));
        return Collections.singleton(message);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FinishStep;
    }
}
