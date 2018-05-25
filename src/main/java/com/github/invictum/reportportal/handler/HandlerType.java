package com.github.invictum.reportportal.handler;

/**
 * Describes available configuration options for {@link Handler} setup
 */
public enum HandlerType {
    /**
     * Builds steps structure as log entities (default behaviour)
     */
    FLAT,

    /**
     * Builds steps structure as a nested RP entities
     */
    TREE
}
