package com.github.invictum.reportportal.log.unit;

import com.epam.ta.reportportal.ws.model.log.SaveLogRQ;
import com.github.invictum.reportportal.Utils;

import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.model.TestStep;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

public class Error {

    private static final Function<TestStep, String> STEP_FORMATTER = step -> {
        Throwable cause = step.getException().getOriginalCause();
        return stringifyStackTrace(cause);
    };

    private static final Function<TestOutcome, String> TEST_FORMATTER = testOutcome -> {
        Throwable cause = testOutcome.getTestFailureCause().toException();
        return stringifyStackTrace(cause);
    };

    private static String stringifyStackTrace(Throwable cause){
        StringWriter writer = new StringWriter();
        cause.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }

    /**
     * Logs error using preconfigured {@link Function} representation of formatter
     */
    public static Function<TestStep, Collection<SaveLogRQ>> configuredStepError(Function<TestStep, String> errorFormatter) {
        return step -> {
            if (step.getException() != null) {
                SaveLogRQ log = new SaveLogRQ();
                log.setMessage(errorFormatter.apply(step));
                log.setLevel(Utils.logLevel(step.getResult()));
                log.setLogTime(Utils.stepEndDate(step));
                return Collections.singleton(log);
            }
            return Collections.emptySet();
        };
    }

    /**
     * Logs error using preconfigured {@link Function} representation of formatter
     */
    public static Function<TestOutcome, Collection<SaveLogRQ>> configuredTestError(Function<TestOutcome, String> errorFormatter) {
        return testOutcome -> {
            if (!testOutcome.getFailingStep().isPresent() && testOutcome.getTestFailureCause() != null) {
                SaveLogRQ log = new SaveLogRQ();
                log.setMessage(errorFormatter.apply(testOutcome));
                log.setLevel(Utils.logLevel(testOutcome.getResult()));
                log.setLogTime(Utils.testEndDate(testOutcome));
                return Collections.singleton(log);
            }
            return Collections.emptySet();
        };
    }

    /**
     * Logs error in step using default error formatter - print error with full stack trace
     */
    public static Function<TestStep, Collection<SaveLogRQ>> basic() {
        return configuredStepError(STEP_FORMATTER);
    }

    /**
     * Logs error in test (outside of a step) using default error formatter - print error with full stack trace
     */
    public static Function<TestOutcome, Collection<SaveLogRQ>> errorInTest() {
        return configuredTestError(TEST_FORMATTER);
    }
}
