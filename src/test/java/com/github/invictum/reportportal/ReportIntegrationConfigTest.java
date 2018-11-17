package com.github.invictum.reportportal;

import com.github.invictum.reportportal.handler.HandlerType;
import net.thucydides.core.annotations.Narrative;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

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
        Narrative narrativeMock = Mockito.mock(Narrative.class);
        Mockito.when(narrativeMock.title()).thenReturn("Title");
        Mockito.when(narrativeMock.text()).thenReturn(new String[]{"line 1", "line 2"});
        config.resetToDefaults();
        String actual = config.narrativeFormatter().apply(narrativeMock);
        Assert.assertEquals("**Title**\n* line 1\n* line 2", actual);
    }

    @Test(expected = NullPointerException.class)
    public void nullNarrativeFormatterTest() {
        config.useNarrativeFormatter(null);
    }

    @Test
    public void resetToDefaultsTest() {
        config.useHandler(HandlerType.TREE).usePreset(LogsPreset.CUSTOM);
        config.resetToDefaults();
        Assert.assertEquals(config.handlerType(), HandlerType.FLAT);
        Assert.assertEquals(config.preset(), LogsPreset.DEFAULT);
    }
}
