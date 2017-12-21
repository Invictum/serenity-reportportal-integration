package com.github.invictum.reportportal;

import net.thucydides.core.model.TestResult;
import net.thucydides.core.model.TestStep;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Date;

public class Utils {
    /**
     * Defines log level based on step result status.
     *
     * @param testResult used to define log level
     * @return log level as a {@link String} representation
     */
    public static String logLevel(TestResult testResult) {
        return Status.mapTo(testResult).logLevel().toString();
    }

    /**
     * Calculates step's end time.
     *
     * @param step to calculate time on
     * @return step end time in {@link Date} format
     */
    public static Date stepEndDate(TestStep step) {
        ZonedDateTime endTimeZoned = step.getStartTime().plus(Duration.ofMillis(step.getDuration()));
        return Date.from(endTimeZoned.toInstant());
    }
}
