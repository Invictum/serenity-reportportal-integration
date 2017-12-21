package com.github.invictum.reportportal;

/**
 * Static configuration entry point. Allows to redefine a profile for integration module.
 * Profile should be defined only once, before tests invocation. If classes graph was build, useProfile method has no any affect.
 */
public class ReportIntegrationConfig {

    static StepsSetProfile profile = StepsSetProfile.DEFAULT;

    public static void useProfile(StepsSetProfile profile) {
        ReportIntegrationConfig.profile = profile;
    }
}
