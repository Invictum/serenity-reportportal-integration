package com.github.invictum.reportportal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

public class ItemTypeTest {

    @Test
    public void itemsContent() {
        String[] items = Stream.of(ItemType.values()).map(ItemType::name).toArray(String[]::new);
        Assertions.assertArrayEquals(new String[]{"TEST", "STEP"}, items);
    }
}
