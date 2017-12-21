package com.github.invictum.reportportal;

import com.github.invictum.reportportal.processor.StepProcessor;
import net.thucydides.core.model.TestStep;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Abstraction used to hold all registered step processors.
 * Entry point to pass {@link TestStep} through sequence of ordered {@link StepProcessor}
 */
public class StepProcessorsHolder {

    private List<StepProcessor> processors = new ArrayList<>();

    public void register(StepProcessor... processors) {
        this.processors = Arrays.asList(processors);
    }

    public void proceed(TestStep step) {
        processors.forEach(processor -> processor.proceed(step));
    }
}
