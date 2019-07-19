package com.github.invictum.reportportal;

import com.github.invictum.reportportal.injector.IntegrationInjector;

import java.util.Objects;

/**
 * Configuration entry point for integration.
 * Allows to redefine configuration for integration module. Configuration should be altered only once, before tests invocation.
 */
public class ReportIntegrationConfig {

    private LogsPreset preset;

    /**
     * Provides injected {@link ReportIntegrationConfig} configuration object
     */
    public static ReportIntegrationConfig get() {
        return IntegrationInjector.getInjector().getInstance(ReportIntegrationConfig.class);
    }

    public ReportIntegrationConfig() {
        resetToDefaults();
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
     * Sets default values for configuration
     */
    public void resetToDefaults() {
        this.preset = LogsPreset.DEFAULT;
    }
}
