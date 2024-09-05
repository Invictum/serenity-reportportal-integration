package com.github.invictum.reportportal;

import net.thucydides.model.domain.TestResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class UtilsLogLevelTest {

    @ParameterizedTest
    @MethodSource("data")
    public void logLevelTest(TestResult testResult, LogLevel status) {
        Assertions.assertEquals(status.toString(), Utils.logLevel(testResult), "Log level is wrong.");
    }

    private static Stream<Arguments> data() {
        return Stream.of(
                Arguments.of(TestResult.SUCCESS, LogLevel.INFO),
                Arguments.of(TestResult.ERROR, LogLevel.ERROR),
                Arguments.of(TestResult.FAILURE, LogLevel.ERROR),
                Arguments.of(TestResult.PENDING, LogLevel.DEBUG),
                Arguments.of(TestResult.SKIPPED, LogLevel.DEBUG),
                Arguments.of(TestResult.IGNORED, LogLevel.DEBUG),
                Arguments.of(TestResult.COMPROMISED, LogLevel.DEBUG),
                Arguments.of(TestResult.UNDEFINED, LogLevel.FATAL),
                Arguments.of(TestResult.UNSUCCESSFUL, LogLevel.FATAL)
        );
    }
}
