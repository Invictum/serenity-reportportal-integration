package com.github.invictum.reportportal;

import net.thucydides.core.annotations.Narrative;
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

    @Test
    public void defaultClassNarrativeFormatter() {
        Narrative narrative = TestInstance.class.getAnnotation(Narrative.class);
        String actual = config.formatter().apply(narrative);
        Assert.assertEquals("line 1\nline 2", actual);
    }

    @Test
    public void overrideClassNarrativeFormatter() {
        config.useClassNarrativeFormatter(n -> n.text()[0]);
        Narrative narrative = TestInstance.class.getAnnotation(Narrative.class);
        String actual = config.formatter().apply(narrative);
        Assert.assertEquals("line 1", actual);
    }

    @Test
    public void defaultTruncateNames() {
        Assert.assertFalse(config.truncateNames);
    }

    @Test
    public void truncateNames() {
        Assert.assertTrue(config.truncateNames(true).truncateNames);
    }
}
