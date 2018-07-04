package com.github.invictum.reportportal.injector;

import com.epam.reportportal.service.Launch;
import com.github.invictum.reportportal.LogStorage;
import com.github.invictum.reportportal.ReportIntegrationConfig;
import com.github.invictum.reportportal.StepProcessorsHolder;
import com.github.invictum.reportportal.StepProcessorsHolderProvider;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class SerenityPortalModule extends AbstractModule {

    protected void configure() {
        bind(Launch.class).toProvider(ReportLaunchProvider.class).in(Scopes.SINGLETON);
        bind(StepProcessorsHolder.class).toProvider(StepProcessorsHolderProvider.class).in(Scopes.SINGLETON);
        bind(LogStorage.class).in(Scopes.SINGLETON);
        bind(ReportIntegrationConfig.class).in(Scopes.SINGLETON);
    }
}
