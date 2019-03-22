package com.github.invictum.reportportal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ReportIntegrationConfigTest {

    private ReportIntegrationConfig config;

    @Before
    public void beforeTest() {
        config = ReportIntegrationConfig.get();
        config.resetToDefaults();
    }

    @Test
    public void defaultStepsSetProfileTest() {
        Assert.assertEquals(config.preset(), LogsPreset.DEFAULT);
    }

    @Test(expected = NullPointerException.class)
    public void nullStepsSetProfileTest() {
        config.usePreset(null);
    }
}
