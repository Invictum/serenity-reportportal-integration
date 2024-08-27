package com.github.invictum.reportportal;

import net.thucydides.model.domain.TestOutcome;
import net.thucydides.model.domain.TestResult;
import net.thucydides.model.domain.TestStep;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /**
     * Replaces entries of parameters in string with its values,
     * e.g. "Add two numbers <number1> & <number2>" => "Add two numbers -2 & 3".
     *
     * @return modified string
     */
    public static String replacePlaceholders(String originalString, List<String> replacements) {
        // Regular expression to match content within < >
        final String parameterRegexp = "<([^>]+)>";
        final Pattern pattern = Pattern.compile(parameterRegexp);
        final Matcher matcher = pattern.matcher(originalString);
        final StringBuilder result = new StringBuilder();
        int index = 0;
        // Iterate through the matches and replace with values from the ArrayList
        while (matcher.find()) {
            if (index < replacements.size()) {
                matcher.appendReplacement(result, replacements.get(index));
                index++;
            }
        }
        // Append the rest of the string
        matcher.appendTail(result);
        return result.toString();
    }
}
