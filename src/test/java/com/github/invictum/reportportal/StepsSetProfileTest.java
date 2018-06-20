package com.github.invictum.reportportal;

import com.github.invictum.reportportal.processor.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class StepsSetProfileTest {

    @Test
    public void fullProfileTest() {
        StepProcessor[] actual = StepsSetProfile.FULL.processors();
        StepProcessor[] expected = new StepProcessor[]{
                new StartStepLogger(),
                new ScreenshotAttacher(),
                new FinishStepLogger(),
                new ErrorLogger(true),
                new HtmlSourceAttacher(),
                new SeleniumLogsAttacher()
        };
        Assert.assertArrayEquals(actual, expected);
    }

    @Test
    public void defaultProfileTest() {
        StepProcessor[] actual = StepsSetProfile.DEFAULT.processors();
        StepProcessor[] expected = new StepProcessor[]{
                new FinishStepLogger(),
                new ScreenshotAttacher(),
                new ErrorLogger(true)
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
