package com.github.invictum.reportportal.extractor;

import com.github.invictum.reportportal.*;
import com.github.invictum.reportportal.injector.IntegrationInjector;
import net.thucydides.core.model.TestStep;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Extracts logs provided by Selenium, if possible
 */
public class SeleniumLogs implements StepDataExtractor {

    private Predicate<EnhancedLogEntry> filter;

    /**
     * Pre-filter only logs that matches predicate
     */
    public SeleniumLogs(Predicate<EnhancedLogEntry> filter) {
        this.filter = filter;
    }

    /**
     * Extracts all available logs
     */
    public SeleniumLogs() {
        this(log -> true);
    }

    @Override
    public Collection<EnhancedMessage> extract(final TestStep step) {
        long start = Utils.stepStartDate(step).getTime();
        long end = Utils.stepEndDate(step).getTime();
        LogStorage storage = IntegrationInjector.getInjector().getInstance(LogStorage.class);
        Set<EnhancedMessage> logs = new HashSet<>();
        storage.query(filter.and(entry -> start >= entry.getTimestamp() && entry.getTimestamp() <= end))
                .forEach(log -> {
                    String message = String.format("[Selenium-%s] [%s] %s", log.getType(), log.getLevel(), log.getMessage());
                    EnhancedMessage entry = new EnhancedMessage(message).withLevel(LogLevel.DEBUG).withDate(new Date(log.getTimestamp()));
                    logs.add(entry);
                });
        return logs;
    }
}
