package com.github.invictum.reportportal.processor;

import com.epam.reportportal.service.ReportPortal;
import com.github.invictum.reportportal.Utils;
import net.thucydides.core.model.TestStep;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.function.Function;

/**
 * Emits log with error message if present
 */
public class ErrorLogger implements StepProcessor {

    private Function<TestStep, String> messageFormatter = step -> {
        Throwable cause = step.getException().getOriginalCause();
        StringWriter writer = new StringWriter();
        cause.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    };

    @Deprecated
    // Use constructor with function instead
    public ErrorLogger(boolean fullLog) {
        if (!fullLog) {
            messageFormatter = TestStep::getConciseErrorMessage;
        }
    }

    /**
     * Default constructor collects full stack trace
     */
    public ErrorLogger() {

    }

    /**
     * Constructor allows to pass custom function to format error message
     *
     * @param messageFormatter to use for message formatting
     */
    public ErrorLogger(Function<TestStep, String> messageFormatter) {
        this.messageFormatter = messageFormatter;
    }

    @Override
    public void proceed(final TestStep step) {
        if (step.getException() != null) {
            String errorMessage = messageFormatter.apply(step);
            ReportPortal.emitLog(errorMessage, Utils.logLevel(step.getResult()), Utils.stepEndDate(step));
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ErrorLogger;
    }
}
