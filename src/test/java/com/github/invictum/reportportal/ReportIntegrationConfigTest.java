package com.github.invictum.reportportal;

import net.serenitybdd.annotations.Narrative;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.invictum.reportportal.ReportIntegrationConfig.*;

public class ReportIntegrationConfigTest {

    private ReportIntegrationConfig config;

    @BeforeEach
    public void before() {
        config = new ReportIntegrationConfig();
    }

    @Test
    public void defaultPresetTest() {
        Assertions.assertEquals(LogsPreset.DEFAULT, config.preset());
    }

    @Test
    public void customPresetTest() {
        Assertions.assertEquals(LogsPreset.CUSTOM, config.usePreset(LogsPreset.CUSTOM).preset());
    }


    @Test
    public void nullCustomPresetTest() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            config.usePreset(null);
        });
    }

    @Test
    public void communicationDirectoryNotDefinedTest() {
        System.clearProperty(COMMUNICATION_DIR_KEY);
        Assertions.assertNull(config.communicationDirectory());
    }

    @Test
    public void communicationDirectoryTest() {
        System.setProperty(COMMUNICATION_DIR_KEY, "dir");
        Assertions.assertEquals("dir", config.communicationDirectory());
    }

    @Test
    public void modulesQuantityNotDefinedTest() {
        System.clearProperty(MODULES_COUNT_KEY);
        Assertions.assertEquals(0, config.modulesQuantity());
    }

    @Test
    public void modulesQuantityTest() {
        System.setProperty(MODULES_COUNT_KEY, "42");
        Assertions.assertEquals(42, config.modulesQuantity());
    }

    @Test
    public void defaultClassNarrativeFormatterTest() {
        Narrative narrative = TestInstance.class.getAnnotation(Narrative.class);
        String actual = config.formatter().apply(narrative);
        Assertions.assertEquals("line 1\nline 2", actual);
    }

    @Test
    public void overrideClassNarrativeFormatterTest() {
        config.useClassNarrativeFormatter(n -> n.text()[0]);
        Narrative narrative = TestInstance.class.getAnnotation(Narrative.class);
        String actual = config.formatter().apply(narrative);
        Assertions.assertEquals("line 1", actual);
    }

    @Test
    public void defaultTruncateNamesTest() {
        Assertions.assertFalse(config.truncateNames);
    }

    @Test
    public void truncateNamesTest() {
        Assertions.assertTrue(config.truncateNames(true).truncateNames);
    }


    @Test
    public void retriesCountFailSafeTest() {
        System.clearProperty(SUREFIRE_RERUN_KEY);
        System.setProperty(FAILSAFE_RERUN_KEY, "42");
        Assertions.assertEquals(42, config.retriesCount());
    }

    @Test
    public void retriesCountSurefireTest() {
        System.clearProperty(FAILSAFE_RERUN_KEY);
        System.setProperty(SUREFIRE_RERUN_KEY, "69");
        Assertions.assertEquals(69, config.retriesCount());
    }

    @Test
    public void retriesCountDefaultTest() {
        System.clearProperty(FAILSAFE_RERUN_KEY);
        System.clearProperty(SUREFIRE_RERUN_KEY);
        Assertions.assertEquals(0, config.retriesCount());
    }
}
