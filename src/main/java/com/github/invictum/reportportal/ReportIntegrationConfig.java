package com.github.invictum.reportportal;

import com.github.invictum.reportportal.handler.HandlerType;
import com.github.invictum.reportportal.injector.IntegrationInjector;
import net.thucydides.core.annotations.Narrative;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Configuration entry point for integration.
 * Allows to redefine configuration for integration module. Configuration should be altered only once, before tests invocation.
 */
public class ReportIntegrationConfig {

    private LogsPreset preset;
    private Function<Narrative, String> narrativeFormatter;
    private HandlerType handlerType;

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
     * Defines narrative format {@link Function}
     */
    public ReportIntegrationConfig useNarrativeFormatter(Function<Narrative, String> narrativeFormatter) {
        this.narrativeFormatter = Objects.requireNonNull(narrativeFormatter, "Narrative formatter could not be null");
        return this;
    }

    public Function<Narrative, String> narrativeFormatter() {
        return narrativeFormatter;
    }

    /**
     * Defines handler type used for Serenity's {@link net.thucydides.core.model.TestOutcome} representation in RP
     */
    public ReportIntegrationConfig useHandler(HandlerType handlerType) {
        this.handlerType = Objects.requireNonNull(handlerType, "Handler type could not be null");
        return this;
    }

    public HandlerType handlerType() {
        return handlerType;
    }

    /**
     * Sets default values for configuration
     */
    public void resetToDefaults() {
        this.preset = LogsPreset.DEFAULT;
        // Returned text is treated by RP as markdown
        this.narrativeFormatter = narrative -> {
            String text = Stream.of(narrative.text()).map(item -> "* " + item).collect(Collectors.joining("\n"));
            return narrative.title().isEmpty() ? text : String.format("**%s**\n", narrative.title()) + text;
        };
        this.handlerType = HandlerType.FLAT;
    }
}
