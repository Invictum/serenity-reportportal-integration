package com.github.invictum.reportportal;

/**
 * Describes available message levels.
 * Similar to log facility.
 */
public enum LogLevel {

    ERROR, WARN, INFO, DEBUG, TRACE, FATAL, UNKNOWN;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
