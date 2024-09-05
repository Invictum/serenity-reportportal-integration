package com.github.invictum.reportportal;

import net.thucydides.model.domain.TestStep;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Date;

public class UtilsTest {

    @Test
    public void stepEndDateTest() {
        ZonedDateTime startTime = ZonedDateTime.now();
        TestStep step = new TestStep(startTime, "Step description");
        step.setDuration(60000);
        Date expected = Date.from(startTime.plus(Duration.ofMillis(60000)).toInstant());
        Assertions.assertEquals(expected, Utils.stepEndDate(step), "End date is wrong.");
    }

    @Test
    public void stepStartDateTest() {
        ZonedDateTime startTime = ZonedDateTime.now();
        TestStep step = new TestStep(startTime, "Step description");
        Date expected = Date.from(step.getStartTime().toInstant());
        Assertions.assertEquals(expected, Utils.stepStartDate(step), "Start date is wrong.");
    }
}
