package com.github.invictum.reportportal.injector;

import com.epam.reportportal.service.Launch;
import com.github.invictum.reportportal.*;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class SerenityPortalModule extends AbstractModule {

    protected void configure() {
        bind(Launch.class).toProvider(ReportLaunchProvider.class).asEagerSingleton();
        bind(LogUnitsHolder.class).toProvider(LogUnitsHolderProvider.class).in(Scopes.SINGLETON);
        bind(LogStorage.class).in(Scopes.SINGLETON);
        bind(ReportIntegrationConfig.class).in(Scopes.SINGLETON);
        bind(SuiteStorage.class).in(Scopes.SINGLETON);
    }
}
