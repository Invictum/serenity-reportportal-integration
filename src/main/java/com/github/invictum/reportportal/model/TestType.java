package com.github.invictum.reportportal.model;

import org.apache.commons.lang3.NotImplementedException;

import java.util.Arrays;

public enum TestType {
    BDD("(cucumber|jbehave)"),
    JUNIT5("JUnit5");

    private final String regexMatcher;

    TestType(String regexMatcher) {
        this.regexMatcher = regexMatcher;
    }

    public static TestType byTestSource(String testSource) {
        return Arrays.stream(values())
                .filter(type -> testSource.toLowerCase().matches(type.regexMatcher))
                .findFirst()
                .orElseThrow(() -> new NotImplementedException(
                        String.format("Test source %s is not supported", testSource)));
    }
}
