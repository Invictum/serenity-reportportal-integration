package com.github.invictum.reportportal;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.stream.Stream;

@RunWith(JUnit4.class)
public class ItemTypeTest {

    @Test
    public void itemsContent() {
        String[] items = Stream.of(ItemType.values()).map(ItemType::name).toArray(String[]::new);
        Assert.assertArrayEquals(new String[]{"TEST", "STEP"}, items);
    }
}
