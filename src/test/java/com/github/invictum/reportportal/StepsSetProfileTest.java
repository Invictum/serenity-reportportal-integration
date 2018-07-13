package com.github.invictum.reportportal;

import com.github.invictum.reportportal.extractor.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class StepsSetProfileTest {

    @Test
    public void fullProfile() {
        StepDataExtractor[] actual = StepsSetProfile.FULL.processors();
        StepDataExtractor[] expected = new StepDataExtractor[]{
                new StartStep(),
                new StepScreenshots(),
                new FinishStep(),
                new StepError(),
                new HtmlSources(),
                new SeleniumLogs()
        };
        Assert.assertArrayEquals(actual, expected);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void fullProfileCustomization() {
        StepsSetProfile.FULL.registerProcessors(new StepScreenshots());
    }

    @Test
    public void defaultProfile() {
        StepDataExtractor[] actual = StepsSetProfile.DEFAULT.processors();
        StepDataExtractor[] expected = new StepDataExtractor[]{
                new FinishStep(),
                new StepScreenshots(),
                new StepError()
        };
        Assert.assertArrayEquals(actual, expected);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void defaultProfileCustomization() {
        StepsSetProfile.DEFAULT.registerProcessors(new StepScreenshots());
    }

    @Test
    public void customProfileCustomization() {
        StepsSetProfile profile = StepsSetProfile.CUSTOM.registerProcessors(new StepScreenshots());
        StepDataExtractor[] expected = new StepDataExtractor[]{new StepScreenshots()};
        Assert.assertArrayEquals(profile.processors(), expected);
    }

    @Test
    public void treeOptimizedProfile() {
        StepDataExtractor[] actual = StepsSetProfile.TREE_OPTIMIZED.processors();
        StepDataExtractor[] expected = new StepDataExtractor[]{
                new StepScreenshots(),
                new StepError()
        };
        Assert.assertArrayEquals(actual, expected);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void treeOptimizedProfileCustomization() {
        StepsSetProfile.TREE_OPTIMIZED.registerProcessors(new HtmlSources());
    }
}
