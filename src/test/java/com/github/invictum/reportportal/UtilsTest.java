package com.github.invictum.reportportal;

import net.thucydides.core.model.TestStep;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Date;

@RunWith(JUnit4.class)
public class UtilsTest {

    @Test
    public void stepEndDateTest() {
        ZonedDateTime startTime = ZonedDateTime.now();
        TestStep step = new TestStep(startTime, "Step description");
        step.setDuration(60000);
        Date expected = Date.from(startTime.plus(Duration.ofMillis(60000)).toInstant());
        Assert.assertEquals("End date is wrong.", Utils.stepEndDate(step), expected);
    }

    @Test
    public void stepStartDateTest() {
        ZonedDateTime startTime = ZonedDateTime.now();
        TestStep step = new TestStep(startTime, "Step description");
        Date expected = Date.from(step.getStartTime().toInstant());
        Assert.assertEquals("Start date is wrong.", Utils.stepStartDate(step), expected);
    }
}
