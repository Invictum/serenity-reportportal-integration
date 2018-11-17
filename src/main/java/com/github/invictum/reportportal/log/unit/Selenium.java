package com.github.invictum.reportportal.log.unit;

import com.epam.ta.reportportal.ws.model.log.SaveLogRQ;
import com.github.invictum.reportportal.EnhancedLogEntry;
import com.github.invictum.reportportal.LogLevel;
import com.github.invictum.reportportal.LogStorage;
import com.github.invictum.reportportal.Utils;
import com.github.invictum.reportportal.injector.IntegrationInjector;
import net.thucydides.core.model.TestStep;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class Selenium {

    /**
     * Extracts all available logs
     */
    public static Function<TestStep, Collection<SaveLogRQ>> allLogs() {
        return filteredLogs(log -> true);
    }

    /**
     * Replicate all Selenium logs that matches passed filter represented as {@link Predicate}
     */
    public static Function<TestStep, Collection<SaveLogRQ>> filteredLogs(Predicate<EnhancedLogEntry> filter) {
        return step -> {
            long start = Utils.stepStartDate(step).getTime();
            long end = Utils.stepEndDate(step).getTime();
            LogStorage storage = IntegrationInjector.getInjector().getInstance(LogStorage.class);
            Set<SaveLogRQ> logs = new HashSet<>();
            storage.query(filter.and(entry -> start >= entry.getTimestamp() && entry.getTimestamp() <= end))
                    .forEach(log -> {
                        String message = String.format("[Selenium-%s] [%s] %s", log.getType(), log.getLevel(), log.getMessage());
                        SaveLogRQ entry = new SaveLogRQ();
                        entry.setMessage(message);
                        entry.setLevel(LogLevel.DEBUG.toString());
                        entry.setLogTime(new Date(log.getTimestamp()));
                        logs.add(entry);
                    });
            return logs;
        };
    }
}
