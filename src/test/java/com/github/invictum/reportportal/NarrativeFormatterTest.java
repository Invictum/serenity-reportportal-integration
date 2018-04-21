package com.github.invictum.reportportal;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class NarrativeFormatterTest {

    @Test
    public void narrativeBulletListFormatter() {
        NarrativeFormatter narrativeFormatter = new NarrativeBulletListFormatter();
        String actual = narrativeFormatter.format(new String[]{"line 1", "line 2", "line 3"});
        String expected = "* line 1\n* line 2\n* line 3";
        Assert.assertEquals("Narrative formatted wrong", expected, actual);
    }
}
