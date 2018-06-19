package com.github.invictum.reportportal;

import org.openqa.selenium.logging.Logs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Central storage for collected logs provided by Selenium
 */
public class LogStorage {

    private ThreadLocal<List<EnhancedLogEntry>> logs = ThreadLocal.withInitial(ArrayList::new);
    private ThreadLocal<Optional<Set<String>>> types = ThreadLocal.withInitial(Optional::empty);

    /**
     * Collects {@link org.openqa.selenium.WebDriver} logs if available and stores its to internal storage
     *
     * @param seleniumLogs to collect from
     */
    public void collect(Logs seleniumLogs) {
        // Discover available log types
        if (!types.get().isPresent()) {
            Set<String> discoveredTypes = seleniumLogs.getAvailableLogTypes();
            types.set(Optional.of(discoveredTypes));
        }
        // Collect all available logs
        types.get().ifPresent(types -> types.forEach(type -> {
            List<EnhancedLogEntry> typedLogs = seleniumLogs.get(type).getAll().stream()
                    .map(log -> new EnhancedLogEntry(type, log.getLevel(), log.getTimestamp(), log.getMessage()))
                    .collect(Collectors.toList());
            logs.get().addAll(typedLogs);
        }));
    }

    /**
     * Clears all logs related to current thread
     */
    public void clean() {
        logs.remove();
        types.remove();
    }

    /**
     * Returns logs that meets defined condition and removes them from storage.
     * Logs could be queried only once
     *
     * @param predicate condition to use to find logs
     * @return a list of queried logs
     */
    public List<EnhancedLogEntry> query(Predicate<EnhancedLogEntry> predicate) {
        List<EnhancedLogEntry> result = logs.get().stream().filter(predicate).collect(Collectors.toList());
        logs.get().removeAll(result);
        return result;
    }
}
