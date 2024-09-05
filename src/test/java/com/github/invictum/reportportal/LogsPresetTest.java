package com.github.invictum.reportportal;

import com.github.invictum.reportportal.log.unit.Error;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LogsPresetTest {

    @Test
    public void fullPreset() {
        int actual = LogsPreset.FULL.logUnits().length;
        Assertions.assertEquals(6, actual);
    }

    @Test
    public void fullPresetCustomization() {
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            LogsPreset.FULL.register(Error.basic());
        });
    }

    @Test
    public void defaultPreset() {
        int actual = LogsPreset.DEFAULT.logUnits().length;
        Assertions.assertEquals(4, actual);
    }

    @Test
    public void customProfileCustomization() {
        LogsPreset preset = LogsPreset.CUSTOM.register(Error.basic());
        Assertions.assertEquals(1, preset.logUnits().length);
    }
}
