package com.github.invictum.reportportal;

import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.Logs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOG = LoggerFactory.getLogger(LogStorage.class);

    private ThreadLocal<List<EnhancedLogEntry>> logs = ThreadLocal.withInitial(ArrayList::new);
    private ThreadLocal<Optional<Set<String>>> types = ThreadLocal.withInitial(Optional::empty);
    private boolean enabled = true;

    /**
     * Collects {@link org.openqa.selenium.WebDriver} logs if available and stores its to internal storage
     * If error is occurred during log collection all future calls will be cancelled
     *
     * @param seleniumLogs to collect from
     */
    public void collect(Logs seleniumLogs) {
        // Skip log collection if previous attempt is failed
        if (!enabled) {
            return;
        }
        // Try to collect some logs
        try {
            if (!types.get().isPresent()) {
                Set<String> discoveredTypes = seleniumLogs.getAvailableLogTypes();
                types.set(Optional.of(discoveredTypes));
            }
            types.get().ifPresent(types -> types.forEach(type -> {
                LogEntries logEntries = seleniumLogs.get(type);
                if (logEntries != null) {
                    List<EnhancedLogEntry> typedLogs = logEntries.getAll().stream()
                            .map(log -> new EnhancedLogEntry(type, log))
                            .collect(Collectors.toList());
                    logs.get().addAll(typedLogs);
                }
            }));
        } catch (WebDriverException e) {
            enabled = false;
            LOG.warn("Attempt to collect Selenium logs has been failed. Logs won't be collected");
            LOG.debug("Root cause", e);
        }
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
