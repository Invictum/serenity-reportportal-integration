package com.github.invictum.reportportal;

import org.openqa.selenium.logging.LogEntry;

import java.util.Map;

/**
 * Enhanced version of {@link LogEntry} with added type field
 */
public class EnhancedLogEntry extends LogEntry {

    private String type;

    public EnhancedLogEntry(String type, LogEntry logEntry) {
        super(logEntry.getLevel(), logEntry.getTimestamp(), logEntry.getMessage());
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public Map<String, Object> toJson() {
        Map<String, Object> json = super.toJson();
        json.put("type", type);
        return json;
    }
}
