package com.github.invictum.reportportal;

/**
 * Describes available item types and their map to ReportPortal facility.
 */
public enum ItemType {

    SUITE("TEST"),
    TEST("STEP");

    private String key;

    ItemType(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}
