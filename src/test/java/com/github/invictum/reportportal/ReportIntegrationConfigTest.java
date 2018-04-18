package com.github.invictum.reportportal;

import com.github.invictum.reportportal.processor.ScreenshotAttacher;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ReportIntegrationConfigTest {

    @Test
    public void defaultStepsSetProfileTest() {
        Assert.assertEquals(StepsSetProfile.DEFAULT, ReportIntegrationConfig.profile);
    }

    @Test
    public void stepsSetProfileCustomizationTest() {
        StepsSetProfile profile = StepsSetProfile.CUSTOM.registerProcessors(new ScreenshotAttacher());
        ReportIntegrationConfig.profile = profile;
        Assert.assertEquals(ReportIntegrationConfig.profile, profile);
    }

    @Test
    public void defaultNarrativeFormatterTest() {
        Class expected = ReportIntegrationConfig.narrativeFormatter.getClass();
        Assert.assertEquals("Default narrative formatter is wrong", expected, NarrativeBulletListFormatter.class);
    }
}
