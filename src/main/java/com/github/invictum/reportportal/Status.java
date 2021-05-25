package com.github.invictum.reportportal;

import net.thucydides.core.model.TestResult;

import java.util.Arrays;
import java.util.List;

/**
 * Describes available items statuses.
 * The same statuses a defined in Report Portal level.
 */
public enum Status {

    PASSED(LogLevel.INFO, TestResult.SUCCESS),
    FAILED(LogLevel.ERROR, TestResult.ERROR, TestResult.FAILURE),
    SKIPPED(LogLevel.DEBUG, TestResult.IGNORED, TestResult.SKIPPED, TestResult.PENDING, TestResult.COMPROMISED),
    CANCELLED(LogLevel.FATAL, TestResult.UNDEFINED, TestResult.UNSUCCESSFUL);

    private final List<TestResult> map;
    private final LogLevel level;

    Status(LogLevel level, TestResult... statuses) {
        map = Arrays.asList(statuses);
        this.level = level;
    }

    public LogLevel logLevel() {
        return level;
    }

    public static Status mapTo(TestResult result) {
        return Arrays.stream(values()).filter(item -> item.map.contains(result)).findFirst().orElse(Status.CANCELLED);
    }
}
