package com.github.invictum.reportportal;

import com.epam.ta.reportportal.ws.model.ParameterResource;
import com.epam.ta.reportportal.ws.model.StartTestItemRQ;
import com.epam.ta.reportportal.ws.model.attribute.ItemAttributesRQ;
import net.thucydides.core.model.DataTable;
import net.thucydides.core.model.TestTag;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RunWith(JUnit4.class)
public class StartEventBuilderTest {

    @Test
    public void itemTypeTest() {
        StartTestItemRQ event = new StartEventBuilder(ItemType.TEST)
                .withStartTime(ZonedDateTime.now())
                .withName("name")
                .build();
        Assert.assertEquals("TEST", event.getType());
    }

    @Test
    public void withStartTimeTest() {
        ZonedDateTime time = ZonedDateTime.now();
        StartTestItemRQ event = new StartEventBuilder(ItemType.TEST)
                .withStartTime(time)
                .withName("name")
                .build();
        Assert.assertEquals(Date.from(time.toInstant()), event.getStartTime());
    }

    @Test(expected = NullPointerException.class)
    public void withNullStartTimeTest() {
        new StartEventBuilder(ItemType.TEST).withStartTime(null).withName("name").build();
    }

    @Test
    public void withNameTest() {
        StartTestItemRQ event = new StartEventBuilder(ItemType.TEST)
                .withStartTime(ZonedDateTime.now())
                .withName("name")
                .build();
        Assert.assertEquals("name", event.getName());
    }

    @Test
    public void withTruncatedNameTest() {
        ReportIntegrationConfig.get().truncateNames(true);
        String name = IntStream.range(0, 1024).mapToObj(i -> "0").collect(Collectors.joining()) + "extra";
        StartTestItemRQ event = new StartEventBuilder(ItemType.TEST)
                .withStartTime(ZonedDateTime.now())
                .withName(name)
                .build();
        String expected = name.substring(0, 1021) + "...";
        Assert.assertEquals(expected, event.getName());
    }

    @Test
    public void noTruncationIfDisabledTest() {
        ReportIntegrationConfig.get().truncateNames(false);
        String name = IntStream.range(0, 1024).mapToObj(i -> "0").collect(Collectors.joining()) + "extra";
        StartTestItemRQ event = new StartEventBuilder(ItemType.TEST)
                .withStartTime(ZonedDateTime.now())
                .withName(name)
                .build();
        Assert.assertEquals(name, event.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void withNullNameTest() {
        new StartEventBuilder(ItemType.TEST).withStartTime(ZonedDateTime.now()).withName(null).build();
    }

    @Test
    public void withDescriptionTest() {
        StartTestItemRQ event = new StartEventBuilder(ItemType.TEST)
                .withStartTime(ZonedDateTime.now())
                .withName("name")
                .withDescription("description")
                .build();
        Assert.assertEquals("description", event.getDescription());
    }

    @Test
    public void withParametersTest() {
        DataTable.RowValueAccessor rowMock = Mockito.mock(DataTable.RowValueAccessor.class);
        Mockito.when(rowMock.toStringMap()).thenReturn(Collections.singletonMap("one", "two"));
        StartTestItemRQ event = new StartEventBuilder(ItemType.TEST)
                .withStartTime(ZonedDateTime.now())
                .withName("name")
                .withParameters(rowMock)
                .build();
        ParameterResource expected = new ParameterResource();
        expected.setKey("one");
        expected.setValue("two");
        Assert.assertEquals(Collections.singletonList(expected), event.getParameters());
    }

    @Test
    public void withTagsTest() {
        Set<TestTag> tags = new HashSet<>();
        tags.add(TestTag.withName("name").andType("type"));
        tags.add(TestTag.withName("name").andType("story"));
        StartTestItemRQ event = new StartEventBuilder(ItemType.TEST)
                .withStartTime(ZonedDateTime.now())
                .withName("name")
                .withTags(tags)
                .build();
        Assert.assertEquals(Collections.singleton(new ItemAttributesRQ("type", "name")), event.getAttributes());
    }

    @Test
    public void withRetryTest() {
        StartTestItemRQ event = new StartEventBuilder(ItemType.TEST)
                .withStartTime(ZonedDateTime.now())
                .withName("name")
                .withRetry()
                .build();
        Assert.assertTrue(event.isRetry());
    }
}
