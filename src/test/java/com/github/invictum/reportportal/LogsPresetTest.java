package com.github.invictum.reportportal;

import com.github.invictum.reportportal.log.unit.Error;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class LogsPresetTest {

    @Test
    public void fullPreset() {
        int actual = LogsPreset.FULL.logUnits().length;
        Assert.assertEquals(6, actual);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void fullPresetCustomization() {
        LogsPreset.FULL.register(Error.basic());
    }

    @Test
    public void defaultPreset() {
        int actual = LogsPreset.DEFAULT.logUnits().length;
        Assert.assertEquals(4, actual);
    }

    @Test
    public void customProfileCustomization() {
        LogsPreset preset = LogsPreset.CUSTOM.register(Error.basic());
        Assert.assertEquals(1, preset.logUnits().length);
    }
}
