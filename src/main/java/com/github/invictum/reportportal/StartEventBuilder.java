package com.github.invictum.reportportal;

import com.epam.ta.reportportal.ws.model.ParameterResource;
import com.epam.ta.reportportal.ws.model.StartTestItemRQ;
import com.epam.ta.reportportal.ws.model.attribute.ItemAttributesRQ;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import net.thucydides.core.model.DataTable;
import net.thucydides.core.model.TestTag;
import org.apache.commons.lang3.StringUtils;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utils builder used to construct {@link StartTestItemRQ} events in fluent way
 */
public class StartEventBuilder {

    private static final int NAME_LIMIT = 1024;
    private StartTestItemRQ startEvent = new StartTestItemRQ();

    public StartEventBuilder(ItemType type) {
        startEvent.setType(type.name());
    }

    public StartEventBuilder withStartTime(ZonedDateTime time) {
        startEvent.setStartTime(Date.from(time.toInstant()));
        return this;
    }

    public StartEventBuilder withName(String name) {
        startEvent.setName(name);
        return this;
    }

    public StartEventBuilder withDescription(String description) {
        startEvent.setDescription(description);
        return this;
    }

    public StartEventBuilder withRetry() {
        startEvent.setRetry(true);
        return this;
    }

    public StartEventBuilder withParameters(DataTable.RowValueAccessor data) {
        List<ParameterResource> parameters = data.toStringMap().entrySet().stream().map(param -> {
            ParameterResource parameter = new ParameterResource();
            parameter.setKey(param.getKey());
            parameter.setValue(param.getValue());
            return parameter;
        }).collect(Collectors.toList());
        startEvent.setParameters(parameters);
        return this;
    }

    public StartEventBuilder withTags(Set<TestTag> tags) {
        Set<ItemAttributesRQ> result = tags.stream().filter(t -> !t.getType().contentEquals("story"))
                .map(tag -> new ItemAttributesRQ(tag.getType(), tag.getName())).collect(Collectors.toSet());
        startEvent.setAttributes(result);
        return this;
    }

    public StartEventBuilder treeOptimized(boolean isOptimized) {
        startEvent.setHasStats(!isOptimized);
        return this;
    }

    public StartTestItemRQ build() {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(startEvent.getName()), "Event name must not be null or empty");
        if (ReportIntegrationConfig.get().truncateNames) {
            String name = startEvent.getName();
            startEvent.setName(name.length() > NAME_LIMIT ? StringUtils.truncate(name, NAME_LIMIT - 3) + "..." : name);
        }
        Objects.requireNonNull(startEvent.getStartTime(), "Start date must not be null");
        return startEvent;
    }
}
