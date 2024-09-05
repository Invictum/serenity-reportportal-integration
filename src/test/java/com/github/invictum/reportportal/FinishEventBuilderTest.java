package com.github.invictum.reportportal;

import com.epam.ta.reportportal.ws.model.FinishTestItemRQ;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class FinishEventBuilderTest {

    @Test
    public void withStatusTest() {
        FinishTestItemRQ event = new FinishEventBuilder()
                .withStatus(Status.CANCELLED)
                .withEndTime(ZonedDateTime.now(), 5)
                .build();
        Assertions.assertEquals("CANCELLED", event.getStatus());
    }

    @Test
    public void withNullStatusTest() {
        Assertions.assertThrows(NullPointerException.class,
                () -> new FinishEventBuilder().withEndTime(ZonedDateTime.now(), 5)
                        .build());
    }

    @Test
    public void withEndDateTest() {
        ZonedDateTime time = ZonedDateTime.now();
        FinishTestItemRQ event = new FinishEventBuilder()
                .withStatus(Status.PASSED)
                .withEndTime(time, 5)
                .build();
        Date expected = Date.from(time.plus(5, ChronoUnit.MILLIS).toInstant());
        Assertions.assertEquals(expected, event.getEndTime());
    }

    @Test
    public void withNullEndDateTest() {
        Assertions.assertThrows(NullPointerException.class,
                () -> new FinishEventBuilder().withStatus(Status.PASSED)
                        .build());
    }
}
