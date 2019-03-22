package com.github.invictum.reportportal;

import com.epam.ta.reportportal.ws.model.FinishTestItemRQ;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@RunWith(JUnit4.class)
public class FinishEventBuilderTest {

    @Test
    public void withStatusTest() {
        FinishTestItemRQ event = new FinishEventBuilder()
                .withStatus(Status.CANCELLED)
                .withEndTime(ZonedDateTime.now(), 5)
                .build();
        Assert.assertEquals("CANCELLED", event.getStatus());
    }

    @Test(expected = NullPointerException.class)
    public void withNullStatusTest() {
        new FinishEventBuilder().withEndTime(ZonedDateTime.now(), 5).build();
    }

    @Test
    public void withEndDateTest() {
        ZonedDateTime time = ZonedDateTime.now();
        FinishTestItemRQ event = new FinishEventBuilder()
                .withStatus(Status.PASSED)
                .withEndTime(time, 5)
                .build();
        Date expected = Date.from(time.plus(5, ChronoUnit.MILLIS).toInstant());
        Assert.assertEquals(expected, event.getEndTime());
    }

    @Test(expected = NullPointerException.class)
    public void withNullEndDateTest() {
        new FinishEventBuilder().withStatus(Status.PASSED).build();
    }
}
