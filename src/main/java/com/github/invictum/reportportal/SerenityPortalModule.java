package com.github.invictum.reportportal;

import com.epam.reportportal.service.ReportPortal;
import com.github.invictum.reportportal.handler.Handler;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class SerenityPortalModule extends AbstractModule {

    protected void configure() {
        bind(Handler.class).to(ReportIntegrationConfig.handlerClass).asEagerSingleton();
        bind(ReportPortal.class).toProvider(ReportPortalProvider.class).in(Scopes.SINGLETON);
    }
}
