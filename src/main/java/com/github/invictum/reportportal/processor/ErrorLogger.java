package com.github.invictum.reportportal.processor;

import com.epam.reportportal.service.ReportPortal;
import com.github.invictum.reportportal.Utils;
import net.thucydides.core.model.TestStep;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Emits log with error message if present.
 * If fullLog option is set to true stack trace will be reported, otherwise short exception message provided by Serenity.
 */
public class ErrorLogger implements StepProcessor {

    private boolean full;

    public ErrorLogger(boolean fullLog) {
        this.full = fullLog;
    }

    @Override
    public void proceed(final TestStep step) {
        if (step.getException() != null) {
            String errorMessage = step.getConciseErrorMessage();
            if (full) {
                /* Dump stack trace into String */
                Throwable cause = step.getException().getOriginalCause();
                StringWriter writer = new StringWriter();
                cause.printStackTrace(new PrintWriter(writer));
                errorMessage = writer.toString();
            }
            ReportPortal.emitLog(errorMessage, Utils.logLevel(step.getResult()), Utils.stepEndDate(step));
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ErrorLogger) {
            ErrorLogger errorLogger = (ErrorLogger) obj;
            return errorLogger.full == this.full;
        }
        return false;
    }
}
