package com.github.invictum.reportportal;

import com.github.invictum.reportportal.handler.HandlerType;
import com.github.invictum.reportportal.injector.IntegrationInjector;

import java.util.Objects;

/**
 * Configuration entry point for integration.
 * Allows to redefine configuration for integration module. Configuration should be altered only once, before tests invocation.
 */
public class ReportIntegrationConfig {

    private StepsSetProfile profile = StepsSetProfile.DEFAULT;
    private NarrativeFormatter narrativeFormatter = new NarrativeBulletListFormatter();
    private HandlerType handlerType = HandlerType.FLAT;

    /**
     * Provides injected {@link ReportIntegrationConfig} configuration object
     */
    public static ReportIntegrationConfig get() {
        return IntegrationInjector.getInjector().getInstance(ReportIntegrationConfig.class);
    }

    private ReportIntegrationConfig() {
        // Disabled constructor
    }

    /**
     * Defines {@link StepsSetProfile} configuration
     */
    public ReportIntegrationConfig useProfile(StepsSetProfile profile) {
        this.profile = Objects.requireNonNull(profile, "Profile could not be null");
        return this;
    }

    public StepsSetProfile profile() {
        return profile;
    }

    /**
     * Defines {@link NarrativeFormatter} configuration
     */
    public ReportIntegrationConfig useNarrativeFormatter(NarrativeFormatter narrativeFormatter) {
        this.narrativeFormatter = Objects.requireNonNull(narrativeFormatter, "Narrative formatter could not be null");
        return this;
    }

    public NarrativeFormatter narrativeFormatter() {
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
        ReportIntegrationConfig configuration = ReportIntegrationConfig.get();
        configuration.useHandler(HandlerType.TREE).useProfile(StepsSetProfile.TREE_OPTIMIZED);
        return handlerType;
    }
}
