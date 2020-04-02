package com.github.invictum.reportportal;

import com.epam.reportportal.service.ReportPortal;
import com.epam.ta.reportportal.ws.model.log.SaveLogRQ;
import net.thucydides.core.model.TestStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Holds all registered log units represented as {@link Function} that is able to produce a {@link Collection} of
 * {@link SaveLogRQ} based on passed {@link TestStep} object
 * Entry point to pass {@link TestStep} through sequence of log units
 */
public class LogUnitsHolder {

    private static final Logger LOG = LoggerFactory.getLogger(LogUnitsHolder.class);

    private Set<Function<TestStep, Collection<SaveLogRQ>>> units = new HashSet<>();

    @SafeVarargs
    public final void register(Function<TestStep, Collection<SaveLogRQ>>... units) {
        checkArgument(units != null, "Passed units must not be null");
        if (units.length == 0) {
            LOG.warn("Empty list of log units is passed. No logs will be emitted to Report Portal");
        }
        this.units = new HashSet<>(Arrays.asList(units));
    }

    public void proceed(TestStep step) {
        units.forEach(item -> {
            Collection<SaveLogRQ> logs = item.apply(step);
            logs.forEach(log -> ReportPortal.emitLog(id -> {
                log.setItemUuid(id);
                return log;
            }));
        });
    }
}
