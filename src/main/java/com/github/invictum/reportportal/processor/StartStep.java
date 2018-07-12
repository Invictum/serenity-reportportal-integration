package com.github.invictum.reportportal.processor;

import com.github.invictum.reportportal.EnhancedMessage;
import com.github.invictum.reportportal.Utils;
import net.thucydides.core.model.TestStep;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

/**
 * Extracts data about started step
 */
public class StartStep implements StepDataExtractor {

    @Override
    public Collection<EnhancedMessage> extract(final TestStep step) {
        Date startDate = Utils.stepStartDate(step);
        EnhancedMessage message = new EnhancedMessage("[STARTED] " + step.getDescription());
        return Collections.singleton(message.withDate(startDate).withLevel(Utils.logLevel(step.getResult())));
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof StartStep;
    }
}
