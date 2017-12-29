package com.github.invictum.reportportal;

import com.github.invictum.reportportal.processor.ScreenshotAttacher;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ReportIntegrationConfigTest {

    @Test
    public void defaultConfigTest() {
        Assert.assertEquals(ReportIntegrationConfig.profile, StepsSetProfile.DEFAULT);
    }

    @Test
    public void defaultProfileCustomizationTest() {
        StepsSetProfile profile = StepsSetProfile.CUSTOM.registerProcessors(new ScreenshotAttacher());
        ReportIntegrationConfig.useProfile(profile);
        Assert.assertEquals(ReportIntegrationConfig.profile, profile);
    }
}
