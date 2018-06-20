package com.github.invictum.reportportal.processor;

import com.epam.reportportal.service.ReportPortal;
import com.github.invictum.reportportal.EnhancedLogEntry;
import com.github.invictum.reportportal.LogLevel;
import com.github.invictum.reportportal.LogStorage;
import com.github.invictum.reportportal.Utils;
import com.github.invictum.reportportal.injector.IntegrationInjector;
import net.thucydides.core.model.TestStep;

import java.util.Date;
import java.util.function.Predicate;

/**
 * Attaches logs provided by Selenium
 */
public class SeleniumLogsAttacher implements StepProcessor {

    private Predicate<EnhancedLogEntry> filter;

    public SeleniumLogsAttacher(Predicate<EnhancedLogEntry> filter) {
        this.filter = filter;
    }

    public SeleniumLogsAttacher() {
        this(log -> true);
    }

    @Override
    public void proceed(final TestStep step) {
        long start = Utils.stepEndDate(step).getTime();
        long end = Utils.stepEndDate(step).getTime();
        LogStorage storage = IntegrationInjector.getInjector().getInstance(LogStorage.class);
        storage.query(filter.and(entry -> start <= entry.getTimestamp() && entry.getTimestamp() >= end))
                .forEach(log -> {
                    String message = String.format("[Selenium-%s] [%s] %s", log.getType(), log.getLevel(), log.getMessage());
                    ReportPortal.emitLog(message, LogLevel.DEBUG.toString(), new Date(log.getTimestamp()));
                });
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SeleniumLogsAttacher;
    }
}
