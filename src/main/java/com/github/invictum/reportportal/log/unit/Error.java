package com.github.invictum.reportportal.log.unit;

import com.epam.ta.reportportal.ws.model.log.SaveLogRQ;
import com.github.invictum.reportportal.Utils;
import net.thucydides.core.model.TestStep;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

public class Error {

    private static final Function<TestStep, String> DEFAULT_FORMATTER = step -> {
        Throwable cause = step.getException().getOriginalCause();
        StringWriter writer = new StringWriter();
        cause.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    };

    /**
     * Logs error using preconfigured {@link Function} representation of formatter
     */
    public static Function<TestStep, Collection<SaveLogRQ>> configuredError(Function<TestStep, String> errorFormatter) {
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
     * Logs error using default error formatter - print error with full stack trace
     */
    public static Function<TestStep, Collection<SaveLogRQ>> basic() {
        return configuredError(DEFAULT_FORMATTER);
    }
}
