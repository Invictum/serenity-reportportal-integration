package com.github.invictum.reportportal;

import com.github.invictum.reportportal.Utils;
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
    public String status;

    @Test
    public void logLevelTest() {
        Assert.assertEquals("Log level is wrong.", Utils.logLevel(testResult), status);
    }

    @Parameterized.Parameters(name = "{index}: {0} - {1}")
    public static Object[][] data() {
        return new Object[][]{
                {TestResult.SUCCESS, "info"},
                {TestResult.ERROR, "error"},
                {TestResult.FAILURE, "error"},
                {TestResult.PENDING, "debug"},
                {TestResult.SKIPPED, "debug"},
                {TestResult.IGNORED, "debug"},
                {TestResult.COMPROMISED, "warn"},
                {TestResult.UNDEFINED, "fatal"}
        };
    }
}
