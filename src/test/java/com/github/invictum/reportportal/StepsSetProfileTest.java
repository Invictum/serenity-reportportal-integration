package com.github.invictum.reportportal;

import com.github.invictum.reportportal.extractor.HtmlSources;
import com.github.invictum.reportportal.extractor.StepDataExtractor;
import com.github.invictum.reportportal.extractor.StepScreenshots;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class StepsSetProfileTest {

    @Test
    public void fullProfile() {
        StepDataExtractor[] actual = StepsSetProfile.FULL.extractors();
        Assert.assertEquals(6, actual.length);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void fullProfileCustomization() {
        StepsSetProfile.FULL.registerExtractors(new StepScreenshots());
    }

    @Test
    public void defaultProfile() {
        StepDataExtractor[] actual = StepsSetProfile.DEFAULT.extractors();
        Assert.assertEquals(3, actual.length);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void defaultProfileCustomization() {
        StepsSetProfile.DEFAULT.registerExtractors(new StepScreenshots());
    }

    @Test
    public void customProfileCustomization() {
        StepsSetProfile profile = StepsSetProfile.CUSTOM.registerExtractors(new StepScreenshots());
        Assert.assertEquals(profile.extractors().length, 1);
    }

    @Test
    public void treeOptimizedProfile() {
        StepDataExtractor[] actual = StepsSetProfile.TREE_OPTIMIZED.extractors();
        Assert.assertEquals(2, actual.length);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void treeOptimizedProfileCustomization() {
        StepsSetProfile.TREE_OPTIMIZED.registerExtractors(new HtmlSources());
    }
}
