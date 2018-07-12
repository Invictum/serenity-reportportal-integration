package com.github.invictum.reportportal.processor;

import com.github.invictum.reportportal.EnhancedMessage;
import net.thucydides.core.model.TestStep;

import java.util.Collection;

/**
 * Single unit of sequence used to extract a set of {@link EnhancedMessage} from {@link TestStep}.
 */
public interface StepDataExtractor {
    /**
     * Method invoked to extract data from {@link TestStep}
     *
     * @param step used to extract data
     * @return step data represented as collection of {@link EnhancedMessage}. If extractor is unable to find any suitable data empty collection should be returned
     */
    Collection<EnhancedMessage> extract(final TestStep step);
}
