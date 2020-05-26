package com.github.invictum.reportportal;

import net.thucydides.core.annotations.Narrative;

import java.util.Objects;
import java.util.function.Function;

/**
 * Configuration entry point for integration.
 * Allows to redefine configuration for integration module. Configuration should be altered only once, before tests invocation.
 */
public class ReportIntegrationConfig {

    static final String COMMUNICATION_DIR_KEY = "serenity.rp.communication.dir";
    static final String MODULES_COUNT_KEY = "serenity.rp.modules.count";
    private static volatile ReportIntegrationConfig instance;

    private LogsPreset preset = LogsPreset.DEFAULT;
    private Function<Narrative, String> classNarrativeFormatter = n -> String.join("\n", n.text());

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

    public Function<Narrative, String> formatter() {
        return classNarrativeFormatter;
    }

    public String communicationDirectory() {
        return System.getProperty(COMMUNICATION_DIR_KEY);
    }

    public int modulesQuantity() {
        String value = System.getProperty(MODULES_COUNT_KEY);
        return value == null ? 0 : Integer.parseInt(value);
    }
}
