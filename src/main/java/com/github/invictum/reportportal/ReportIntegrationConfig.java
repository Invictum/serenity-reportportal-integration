package com.github.invictum.reportportal;

import net.thucydides.core.annotations.Narrative;

import java.util.Objects;
import java.util.function.Function;

/**
 * Configuration entry point for integration.
 * Allows to redefine configuration for integration module. Configuration should be altered only once, before tests invocation.
 */
public class ReportIntegrationConfig {

    public static final String COMMUNICATION_DIR_KEY = "serenity.rp.communication.dir";
    public static final String MODULES_COUNT_KEY = "serenity.rp.modules.count";
    public static final String FAILSAFE_RERUN_KEY = "failsafe.rerunFailingTestsCount";
    public static final String SUREFIRE_RERUN_KEY = "surefire.rerunFailingTestsCount";
    private static volatile ReportIntegrationConfig instance;

    private LogsPreset preset = LogsPreset.DEFAULT;
    private Function<Narrative, String> classNarrativeFormatter = n -> String.join("\n", n.text());
    boolean harvestSeleniumLogs = false;
    boolean truncateNames = false;

    /**
     * Access to shared configuration instance
     */
    public static ReportIntegrationConfig get() {
        if (instance == null) {
            synchronized (ReportIntegrationConfig.class) {
                if (instance == null) {
                    instance = new ReportIntegrationConfig();
                }
            }
        }
        return instance;
    }

    /**
     * Defines {@link LogsPreset} configuration
     */
    public ReportIntegrationConfig usePreset(LogsPreset preset) {
        this.preset = Objects.requireNonNull(preset, "Profile could not be null");
        return this;
    }

    public LogsPreset preset() {
        return preset;
    }

    /**
     * Overrides class level narrative formatter with custom implementation
     */
    public ReportIntegrationConfig useClassNarrativeFormatter(Function<Narrative, String> formatter) {
        classNarrativeFormatter = Objects.requireNonNull(formatter, "Formatter must not be null");
        return this;
    }

    /**
     * Option allows to enable or disable selenium based logs harvesting
     * Designed to be used in conjunction with {@link com.github.invictum.reportportal.log.unit.Selenium} log unit
     * Disabled by default that means selenium logs won't be collected even if selenium log unit was added to preset
     */
    public ReportIntegrationConfig harvestSeleniumLogs(boolean harvestLogs) {
        harvestSeleniumLogs = harvestLogs;
        return this;
    }

    public Function<Narrative, String> formatter() {
        return classNarrativeFormatter;
    }

    /**
     * Option sets names truncation feature, that allows to avoid RP errors with long entities creation
     */
    public ReportIntegrationConfig truncateNames(boolean setting) {
        truncateNames = setting;
        return this;
    }

    public String communicationDirectory() {
        return System.getProperty(COMMUNICATION_DIR_KEY);
    }

    public int retriesCount() {
        String value = System.getProperty(FAILSAFE_RERUN_KEY, System.getProperty(SUREFIRE_RERUN_KEY));
        try {
            return value == null ? 0 : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Wrong retires count", e);
        }
    }

    public int modulesQuantity() {
        String value = System.getProperty(MODULES_COUNT_KEY);
        return value == null ? 0 : Integer.parseInt(value);
    }
}
