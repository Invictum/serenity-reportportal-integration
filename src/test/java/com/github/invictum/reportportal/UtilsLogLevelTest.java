package com.github.invictum.reportportal;

import net.thucydides.core.model.TestResult;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class UtilsLogLevelTest {

    @Parameterized.Parameter()
    public TestResult testResult;

    @Parameterized.Parameter(1)
    public LogLevel status;

    @Test
    public void logLevelTest() {
        Assert.assertEquals("Log level is wrong.", Utils.logLevel(testResult), status.toString());
    }

    @Parameterized.Parameters(name = "{index}: {0} - {1}")
    public static Object[][] data() {
        return new Object[][]{
                {TestResult.SUCCESS, LogLevel.INFO},
                {TestResult.ERROR, LogLevel.ERROR},
                {TestResult.FAILURE, LogLevel.ERROR},
                {TestResult.PENDING, LogLevel.DEBUG},
                {TestResult.SKIPPED, LogLevel.DEBUG},
                {TestResult.IGNORED, LogLevel.DEBUG},
                {TestResult.COMPROMISED, LogLevel.DEBUG},
                {TestResult.UNDEFINED, LogLevel.FATAL},
                {TestResult.UNSUCCESSFUL, LogLevel.FATAL}
        };
    }
}
