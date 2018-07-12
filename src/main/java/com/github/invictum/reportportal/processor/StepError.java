package com.github.invictum.reportportal.processor;

import com.github.invictum.reportportal.EnhancedMessage;
import com.github.invictum.reportportal.Utils;
import net.thucydides.core.model.TestStep;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

/**
 * Extracts error if present
 */
public class StepError implements StepDataExtractor {

    private Function<TestStep, String> messageFormatter;

    /**
     * Default constructor collects full stack trace
     */
    public StepError() {
        this(step -> {
            Throwable cause = step.getException().getOriginalCause();
            StringWriter writer = new StringWriter();
            cause.printStackTrace(new PrintWriter(writer));
            return writer.toString();
        });
    }

    /**
     * Constructor allows to pass custom function to format error message
     *
     * @param messageFormatter to use for message formatting
     */
    public StepError(Function<TestStep, String> messageFormatter) {
        this.messageFormatter = messageFormatter;
    }

    @Override
    public Collection<EnhancedMessage> extract(final TestStep step) {
        if (step.getException() != null) {
            EnhancedMessage message = new EnhancedMessage(messageFormatter.apply(step));
            message.withLevel(Utils.logLevel(step.getResult())).withDate(Utils.stepEndDate(step));
            return Collections.singleton(message);
        }
        return Collections.emptySet();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof StepError;
    }
}
