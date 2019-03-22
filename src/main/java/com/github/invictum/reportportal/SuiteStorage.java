package com.github.invictum.reportportal;

import io.reactivex.Maybe;

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
            SuiteMetadata meta = suites.remove(id);
            meta.finisher.run();
        });
    }

    /**
     * Node class that holds suite metadata
     */
    private class SuiteMetadata {
        private Maybe<String> id;
        private Runnable finisher;
    }
}
