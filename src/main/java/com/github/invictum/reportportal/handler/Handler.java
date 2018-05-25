package com.github.invictum.reportportal.handler;

import net.thucydides.core.model.Story;
import net.thucydides.core.model.TestOutcome;

/**
 * Defines strategy used for processing Serenity test related events
 */
public interface Handler {
    /**
     * Starts a new suite for class
     *
     * @param storyClass to start suite for
     */
    void startSuite(Class<?> storyClass);

    /**
     * Starts a new suite for story
     *
     * @param story to start suite for
     */
    void startSuite(Story story);

    /**
     * Finish currently active suite
     */
    void finishSuite();

    /**
     * Start a new test
     *
     * @param description to use for test
     */
    void startTest(String description);

    /**
     * Finalizes currently active test
     *
     * @param result used to build a test outcome
     */
    void finishTest(TestOutcome result);
}
