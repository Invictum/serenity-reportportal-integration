package com.github.invictum.reportportal;

import com.github.invictum.reportportal.processor.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class StepsSetProfileTest {

    @Test
    public void fullProfile() {
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

    @Test(expected = UnsupportedOperationException.class)
    public void fullProfileCustomization() {
        StepsSetProfile.FULL.registerProcessors(new ScreenshotAttacher());
    }

    @Test
    public void defaultProfile() {
        StepProcessor[] actual = StepsSetProfile.DEFAULT.processors();
        StepProcessor[] expected = new StepProcessor[]{
                new FinishStepLogger(),
                new ScreenshotAttacher(),
                new ErrorLogger(true)
        };
        Assert.assertArrayEquals(actual, expected);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void defaultProfileCustomization() {
        StepsSetProfile.DEFAULT.registerProcessors(new ScreenshotAttacher());
    }

    @Test
    public void customProfileCustomization() {
        StepsSetProfile profile = StepsSetProfile.CUSTOM.registerProcessors(new ScreenshotAttacher());
        StepProcessor[] expected = new StepProcessor[]{new ScreenshotAttacher()};
        Assert.assertArrayEquals(profile.processors(), expected);
    }

    @Test
    public void treeOptimizedProfile() {
        StepProcessor[] actual = StepsSetProfile.TREE_OPTIMIZED.processors();
        StepProcessor[] expected = new StepProcessor[]{
                new ScreenshotAttacher(),
                new ErrorLogger(true)
        };
        Assert.assertArrayEquals(actual, expected);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void treeOptimizedProfileCustomization() {
        StepsSetProfile.TREE_OPTIMIZED.registerProcessors(new HtmlSourceAttacher());
    }
}
