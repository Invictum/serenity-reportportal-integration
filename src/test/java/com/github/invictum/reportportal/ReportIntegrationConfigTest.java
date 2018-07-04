package com.github.invictum.reportportal;

import com.github.invictum.reportportal.handler.HandlerType;
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
        Assert.assertEquals(config.profile(), StepsSetProfile.DEFAULT);
    }

    @Test(expected = NullPointerException.class)
    public void nullStepsSetProfileTest() {
        config.useProfile(null);
    }

    @Test
    public void defaultHandlerTypeTest() {
        Assert.assertEquals(config.handlerType(), HandlerType.FLAT);
    }

    @Test(expected = NullPointerException.class)
    public void nullHandlerTypeTest() {
        config.useHandler(null);
    }

    @Test
    public void defaultNarrativeFormatterTest() {
        Class actual = config.narrativeFormatter().getClass();
        Assert.assertEquals(NarrativeBulletListFormatter.class, actual);
    }

    @Test(expected = NullPointerException.class)
    public void nullNarrativeFormatterTest() {
        config.useNarrativeFormatter(null);
    }

    @Test
    public void resetToDefaultsTest() {
        config.useHandler(HandlerType.TREE).useProfile(StepsSetProfile.CUSTOM);
        config.resetToDefaults();
        Assert.assertEquals(config.handlerType(), HandlerType.FLAT);
        Assert.assertEquals(config.profile(), StepsSetProfile.DEFAULT);
    }
}
