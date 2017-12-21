package com.github.invictum.reportportal.processor;

import net.thucydides.core.model.TestStep;

/**
 * Single unit of sequence used to proceed @{@link TestStep}. Each @{@link TestStep} is invoked by registered processors.
 */
public interface StepProcessor {
    /**
     * Method invoked to proceed @{@link TestStep} details
     *
     * @param step to proceed
     */
    void proceed(final TestStep step);
}
