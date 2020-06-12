package com.github.invictum.reportportal.log.unit;

import com.epam.ta.reportportal.ws.model.log.SaveLogRQ;
import net.serenitybdd.core.rest.RestMethod;
import net.serenitybdd.core.rest.RestQuery;
import net.thucydides.core.model.TestStep;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.ZonedDateTime;
import java.util.Collection;

@RunWith(MockitoJUnitRunner.StrictStubs.class)
public class RestTest {

    private static String MESSAGE = "## Request\n" +
            "\n" +
            "GET path\n" +
            "\n" +
            "***Headers***\n" +
            "req-headers\n" +
            "\n" +
            "***Cookies***\n" +
            "req-cookies\n" +
            "\n" +
            "***Body***\n" +
            "```\n" +
            "body\n" +
            "```\n" +
            "\n" +
            "## Response\n" +
            "\n" +
            "***Code*** 200\n" +
            "\n" +
            "***Headers***\n" +
            "res-headers\n" +
            "\n" +
            "***Body***\n" +
            "```\n" +
            "res-body\n" +
            "```\n";

    @Mock
    private TestStep stepMock;

    @Test
    public void noQueryTest() {
        Mockito.when(stepMock.hasRestQuery()).thenReturn(false);
        Collection<SaveLogRQ> logs = Rest.restQuery().apply(stepMock);
        Assert.assertTrue(logs.isEmpty());
    }

    @Test
    public void queryTest() {
        RestQuery query = new RestQuery.RestQueryBuilder(RestMethod.GET).andPath("path")
                .withContent("body")
                .withRequestCookies("req-cookies")
                .withRequestHeaders("req-headers")
                .withStatusCode(200)
                .withResponse("res-body")
                .withResponseCookies("res-cookies")
                .withResponseHeaders("res-headers");
        Mockito.when(stepMock.getStartTime()).thenReturn(ZonedDateTime.now());
        Mockito.when(stepMock.hasRestQuery()).thenReturn(true);
        Mockito.when(stepMock.getRestQuery()).thenReturn(query);
        Collection<SaveLogRQ> logs = Rest.restQuery().apply(stepMock);
        Assert.assertEquals(MESSAGE, logs.iterator().next().getMessage());
    }
}
