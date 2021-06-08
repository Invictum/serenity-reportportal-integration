package com.github.invictum.reportportal;

import io.reactivex.Maybe;

import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Abstraction used to control active suites lifecycle
 */
public class SuiteStorage {

    private ConcurrentHashMap<String, SuiteMetadata> suites = new ConcurrentHashMap<>();

    /**
     * Starts a new suite entity
     *
     * @param id    defined by caller
     * @param start function to start suite
     * @return internal RP identifier associated with created suite
     */
    public Maybe<String> start(String id, Supplier<Maybe<String>> start) {
        suites.computeIfAbsent(id, old -> {
            SuiteMetadata metadata = new SuiteMetadata();
            metadata.id = start.get();
            return metadata;
        });
        SuiteMetadata meta = suites.get(id);
        return meta.id;
    }

    /**
     * Registers a suite finisher, a that should be executed to complete suite
     *
     * @param id       defined by user to finish suite for
     * @param finisher execution logic
     */
    public void suiteFinisher(String id, Runnable finisher) {
        suites.computeIfPresent(id, (key, meta) -> {
            meta.finisher = finisher;
            return meta;
        });
    }

    /**
     * Finishes all the active suite that belongs to current thread
     * Executes finishers registered by suiteFinisher method
     */
    public void finalizeActive() {
        suites.forEachKey(Runtime.getRuntime().availableProcessors(), id -> {
            SuiteMetadata meta = suites.get(id);
            if (meta.failedTests.isEmpty()) {
                suites.remove(id);
                meta.finisher.run();
            }
        });
    }

    /**
     * Register id of failed test for specific suite by id. Should be called if test failed first time.
     * Part of retries logic.
     *
     * @param suiteId id of suite where fail test was detected
     * @param testId  id of failed test
     */
    public void addFail(String suiteId, String testId) {
        SuiteMetadata meta = suites.get(suiteId);
        meta.failedTests.add(testId);
    }

    /**
     * This method need for check is test is retry. Part of retries logic.
     *
     * @param suiteId id of suite where fail test was detected
     * @param testId  id of failed test
     */
    public boolean isFailPresent(String suiteId, String testId) {
        SuiteMetadata meta = suites.get(suiteId);
        return meta.failedTests.contains(testId);
    }

    /**
     * Should be called if failed test start passed after retry. Part of retries logic.
     *
     * @param suiteId id of suite where fail test was detected
     * @param testId  id of failed test
     */
    public void removeFail(String suiteId, String testId) {
        SuiteMetadata meta = suites.get(suiteId);
        meta.failedTests.remove(testId);
    }

    /**
     * Node class that holds suite metadata
     */
    private static class SuiteMetadata {
        private Maybe<String> id;
        private Runnable finisher;
        private final HashSet<String> failedTests = new HashSet<>();
    }
}
