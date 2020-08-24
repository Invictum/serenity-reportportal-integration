package com.github.invictum.reportportal;

import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.model.TestResult;
import net.thucydides.core.model.TestStep;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Date;

public class Utils {

    private Utils() {
    }

    /**
     * Finds log level based on step result status.
     *
     * @param testResult used to define log level
     * @return discovered log level
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

    /**
     * Calculates test's end time.
     *
     * @param test to calculate time on
     * @return test end time in {@link Date} format
     */
    public static Date testEndDate(TestOutcome test) {
        ZonedDateTime endTimeZoned = test.getStartTime().plus(Duration.ofMillis(test.getDuration()));
        return Date.from(endTimeZoned.toInstant());
    }

    /**
     * Calculates step's start time
     *
     * @param step to calculate time for
     * @return step start time in {@link Date} format
     */
    public static Date stepStartDate(TestStep step) {
        return Date.from(step.getStartTime().toInstant());
    }
}
