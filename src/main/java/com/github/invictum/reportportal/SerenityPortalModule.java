package com.github.invictum.reportportal;

import com.epam.reportportal.service.ReportPortal;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class SerenityPortalModule extends AbstractModule {

    protected void configure() {
        bind(ReportPortal.class).toProvider(ReportPortalProvider.class).in(Scopes.SINGLETON);
        bind(StepProcessorsHolder.class).toProvider(StepProcessorsHolderProvider.class).in(Scopes.SINGLETON);
    }
}
