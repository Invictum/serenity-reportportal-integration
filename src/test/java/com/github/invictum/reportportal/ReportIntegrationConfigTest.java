package com.github.invictum.reportportal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.github.invictum.reportportal.ReportIntegrationConfig.COMMUNICATION_DIR_KEY;
import static com.github.invictum.reportportal.ReportIntegrationConfig.MODULES_COUNT_KEY;

@RunWith(JUnit4.class)
public class ReportIntegrationConfigTest {

    private ReportIntegrationConfig config;

    @Before
    public void before() {
        config = new ReportIntegrationConfig();
    }

    @Test
    public void defaultPreset() {
        Assert.assertEquals(LogsPreset.DEFAULT, config.preset());
    }

    @Test
    public void customPreset() {
        Assert.assertEquals(LogsPreset.CUSTOM, config.usePreset(LogsPreset.CUSTOM).preset());
    }


    @Test(expected = NullPointerException.class)
    public void nullCustomPreset() {
        config.usePreset(null);
    }

    @Test
    public void communicationDirectoryNotDefined() {
        System.clearProperty(COMMUNICATION_DIR_KEY);
        Assert.assertNull(config.communicationDirectory());
    }

    @Test
    public void communicationDirectory() {
        System.setProperty(COMMUNICATION_DIR_KEY, "dir");
        Assert.assertEquals(config.communicationDirectory(), "dir");
    }

    @Test
    public void modulesQuantityNotDefined() {
        System.clearProperty(MODULES_COUNT_KEY);
        Assert.assertEquals(0, config.modulesQuantity());
    }

    @Test
    public void modulesQuantity() {
        System.setProperty(MODULES_COUNT_KEY, "42");
        Assert.assertEquals(42, config.modulesQuantity());
    }
}
