package com.github.invictum.reportportal.storage.events;

/**
 * Describes available node search modes.
 */
public enum SearchMode {
    /**
     * If node is open and compatible with {@link Event}.
     * It will be returned as a search result.
     */
    PARENT_FIRST,
    /**
     * If node has an open children, one of them will be returned as a search result.
     * Even if parent node is compatible with {@link Event}.
     */
    CHILDREN_FIRST
}
