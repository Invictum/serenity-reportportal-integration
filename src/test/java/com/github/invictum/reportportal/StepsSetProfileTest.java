package com.github.invictum.reportportal;

import com.github.invictum.reportportal.processor.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class StepsSetProfileTest {

    @Test
    public void defaultProfileTest() {
        StepProcessor[] actual = StepsSetProfile.DEFAULT.processors();
        StepProcessor[] expected = new StepProcessor[]{
                new StartStepLogger(),
                new ScreenshotAttacher(),
                new HtmlSourceAttacher(),
                new ErrorLogger(true),
                new FinishStepLogger()
        };
        Assert.assertArrayEquals(actual, expected);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void defaultProfileCustomizationTest() {
        StepsSetProfile.DEFAULT.registerProcessors(new ScreenshotAttacher());
    }

    @Test
    public void customProfileTest() {
        StepsSetProfile profile = StepsSetProfile.CUSTOM.registerProcessors(new ScreenshotAttacher());
        StepProcessor[] expected = new StepProcessor[]{new ScreenshotAttacher()};
        Assert.assertArrayEquals(profile.processors(), expected);
    }
}
