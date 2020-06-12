package com.github.invictum.reportportal.injector;

import com.epam.reportportal.service.Launch;
import com.github.invictum.reportportal.LogStorage;
import com.github.invictum.reportportal.LogUnitsHolder;
import com.github.invictum.reportportal.SuiteStorage;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class SerenityPortalModule extends AbstractModule {

    protected void configure() {
        bind(Launch.class).toProvider(ReportLaunchProvider.class).asEagerSingleton();
        bind(LogUnitsHolder.class).toProvider(LogUnitsHolderProvider.class).in(Scopes.SINGLETON);
        bind(LogStorage.class).in(Scopes.SINGLETON);
        bind(SuiteStorage.class).in(Scopes.SINGLETON);
    }
}
