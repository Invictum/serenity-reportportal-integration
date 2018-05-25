package com.github.invictum.reportportal;

import com.github.invictum.reportportal.handler.HandlerType;

/**
 * Static configuration entry point for integration.
 * Allows to redefine configuration for integration module. Configuration should be defined only once, before tests invocation.
 */
public class ReportIntegrationConfig {

    private ReportIntegrationConfig() {
        /* Disabled constructor */
    }

    /**
     * Defines default {@link StepsSetProfile} configuration
     * Profile could be redefined with custom value
     */
    public static StepsSetProfile profile = StepsSetProfile.DEFAULT;

    /**
     * Defines default {@link NarrativeFormatter} configuration
     * Transformer could be redefined with custom value
     */
    public static NarrativeFormatter narrativeFormatter = new NarrativeBulletListFormatter();

    /**
     * Defines handler type used for Serenity's {@link net.thucydides.core.model.TestOutcome} representation in RP
     */
    public static HandlerType handlerType = HandlerType.FLAT;

    @Deprecated
    /* Use profile variable to set config directly instead */
    public static void useProfile(StepsSetProfile profile) {
        ReportIntegrationConfig.profile = profile;
    }
}
