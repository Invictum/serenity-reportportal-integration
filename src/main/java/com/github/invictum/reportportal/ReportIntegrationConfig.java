package com.github.invictum.reportportal;

import com.github.invictum.reportportal.handler.Handler;
import com.github.invictum.reportportal.handler.StepTreeHandler;

/**
 * Static configuration entry point. Allows to redefine a handler class for integration module.
 * Handler should be defined only once, before tests invocation. If classes graph was build, setHandlerClass method has no any affect.
 */
public class ReportIntegrationConfig {

    static Class<? extends Handler> handlerClass = StepTreeHandler.class;

    public static void setHandlerClass(Class<? extends Handler> handlerClass) {
        ReportIntegrationConfig.handlerClass = handlerClass;
    }
}
