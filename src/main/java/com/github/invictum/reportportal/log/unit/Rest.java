package com.github.invictum.reportportal.log.unit;

import com.epam.ta.reportportal.ws.model.log.SaveLogRQ;
import com.github.invictum.reportportal.LogLevel;
import com.github.invictum.reportportal.Utils;
import net.serenitybdd.core.rest.RestQuery;
import net.thucydides.core.model.TestStep;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

public class Rest {

    /**
     * Logs REST api call
     */
    public static Function<TestStep, Collection<SaveLogRQ>> restQuery() {
        return step -> {
            if (step.hasRestQuery()) {
                RestQuery query = step.getRestQuery();
                StringBuilder builder = new StringBuilder();
                // Request
                builder.append("## Request\n\n").append(query.getFormattedQuery()).append("\n\n");
                if (query.getRequestHeaders() != null) {
                    builder.append("***Headers***\n").append(query.getRequestHeaders()).append("\n\n");
                }
                if (query.getRequestCookies() != null && !query.getRequestCookies().isEmpty()) {
                    builder.append("***Cookies***\n").append(query.getRequestCookies()).append("\n\n");
                }
                if (query.getContent() != null && !query.getContent().isEmpty()) {
                    builder.append("***Body***\n```\n").append(query.getContent()).append("\n```\n");
                }
                // Response
                builder.append("\n## Response\n\n").append("***Code*** ").append(query.getStatusCode()).append("\n\n");
                if (query.getResponseHeaders() != null && !query.getResponseHeaders().isEmpty()) {
                    builder.append("***Headers***\n").append(query.getResponseHeaders()).append("\n\n");
                }
                if (query.getResponseBody() != null && !query.getResponseBody().isEmpty()) {
                    builder.append("***Body***\n```\n").append(query.getResponseBody()).append("\n```\n");
                }
                // Log entity
                SaveLogRQ log = new SaveLogRQ();
                log.setLogTime(Utils.stepStartDate(step));
                log.setLevel(LogLevel.INFO.toString());
                log.setMessage(builder.toString());
                return Collections.singleton(log);
            }
            return Collections.emptySet();
        };
    }
}
