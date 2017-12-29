package com.github.invictum.reportportal;

import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.model.TestStep;
import net.thucydides.core.model.TestTag;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RunWith(JUnit4.class)
public class UtilsTest {

    @Test
    public void refineTagsTest() {
        TestOutcome testOutcome = TestOutcome.forTest("refineTagsTest", UtilsTest.class);
        TestTag tagOne = TestTag.withName("valueOne").andType("tagOne");
        TestTag tagTwo = TestTag.withName("valueTwo").andType("tagTwo");
        TestTag storyTag = TestTag.withName("value").andType("story");
        Set<TestTag> tags = new HashSet<>();
        tags.add(tagOne);
        tags.add(tagTwo);
        tags.add(storyTag);
        testOutcome = testOutcome.withTags(tags);
        Set<String> portalTags = Utils.refineTags(testOutcome);
        Assert.assertEquals("Tags are refined wrong.", portalTags, tags.stream()
                .filter(t -> !t.getType().contentEquals("story")).map(t -> t.getType() + ":" + t.getName())
                .collect(Collectors.toSet()));
    }

    @Test
    public void stepEndDateTest() {
        ZonedDateTime startTime = ZonedDateTime.now();
        TestStep step = new TestStep(startTime, "Step description");
        step.setDuration(60000);
        Date expected = Date.from(startTime.plus(Duration.ofMillis(60000)).toInstant());
        Assert.assertEquals("End date is wrong.", Utils.stepEndDate(step), expected);
    }
}
