package com.github.invictum.reportportal;

import java.util.Map;
import java.util.Set;

/**
 * Technical object used to transfer context details between {@link net.thucydides.core.steps.StepListener} and {@link com.github.invictum.reportportal.handler.Handler}
 */
public class EventData {

    private String name;
    private String description;
    private Set<String> tags;
    private Map<String, String> parameters;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
}
