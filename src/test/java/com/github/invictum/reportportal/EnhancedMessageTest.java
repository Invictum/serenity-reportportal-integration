package com.github.invictum.reportportal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class EnhancedMessageTest {

    @Test(expected = NullPointerException.class)
    public void setDateWithNull() {
        new EnhancedMessage("Text").withDate(null);
    }

    @Test(expected = NullPointerException.class)
    public void setLevelWithNull() {
        new EnhancedMessage("Text").withLevel(null);
    }
}
