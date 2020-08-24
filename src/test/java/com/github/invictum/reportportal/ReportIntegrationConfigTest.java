package com.github.invictum.reportportal;

import net.thucydides.core.annotations.Narrative;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.github.invictum.reportportal.ReportIntegrationConfig.*;

@RunWith(JUnit4.class)
public class ReportIntegrationConfigTest {

    private ReportIntegrationConfig config;

    @Before
    public void before() {
        config = new ReportIntegrationConfig();
    }

    @Test
    public void defaultPresetTest() {
        Assert.assertEquals(LogsPreset.DEFAULT, config.preset());
    }

    @Test
    public void customPresetTest() {
        Assert.assertEquals(LogsPreset.CUSTOM, config.usePreset(LogsPreset.CUSTOM).preset());
    }


    @Test(expected = NullPointerException.class)
    public void nullCustomPresetTest() {
        config.usePreset(null);
    }

    @Test
    public void communicationDirectoryNotDefinedTest() {
        System.clearProperty(COMMUNICATION_DIR_KEY);
        Assert.assertNull(config.communicationDirectory());
    }

    @Test
    public void communicationDirectoryTest() {
        System.setProperty(COMMUNICATION_DIR_KEY, "dir");
        Assert.assertEquals("dir", config.communicationDirectory());
    }

    @Test
    public void modulesQuantityNotDefinedTest() {
        System.clearProperty(MODULES_COUNT_KEY);
        Assert.assertEquals(0, config.modulesQuantity());
    }

    @Test
    public void modulesQuantityTest() {
        System.setProperty(MODULES_COUNT_KEY, "42");
        Assert.assertEquals(42, config.modulesQuantity());
    }

    @Test
    public void defaultClassNarrativeFormatterTest() {
        Narrative narrative = TestInstance.class.getAnnotation(Narrative.class);
        String actual = config.formatter().apply(narrative);
        Assert.assertEquals("line 1\nline 2", actual);
    }

    @Test
    public void overrideClassNarrativeFormatterTest() {
        config.useClassNarrativeFormatter(n -> n.text()[0]);
        Narrative narrative = TestInstance.class.getAnnotation(Narrative.class);
        String actual = config.formatter().apply(narrative);
        Assert.assertEquals("line 1", actual);
    }

    @Test
    public void defaultTruncateNamesTest() {
        Assert.assertFalse(config.truncateNames);
    }

    @Test
    public void truncateNamesTest() {
        Assert.assertTrue(config.truncateNames(true).truncateNames);
    }


    @Test
    public void retriesCountFailSafeTest() {
        System.clearProperty(SUREFIRE_RERUN_KEY);
        System.setProperty(FAILSAFE_RERUN_KEY, "42");
        Assert.assertEquals(42, config.retriesCount());
    }

    @Test
    public void retriesCountSurefireTest() {
        System.clearProperty(FAILSAFE_RERUN_KEY);
        System.setProperty(SUREFIRE_RERUN_KEY, "69");
        Assert.assertEquals(69, config.retriesCount());
    }

    @Test
    public void retriesCountDefaultTest() {
        System.clearProperty(FAILSAFE_RERUN_KEY);
        System.clearProperty(SUREFIRE_RERUN_KEY);
        Assert.assertEquals(0, config.retriesCount());
    }
}
