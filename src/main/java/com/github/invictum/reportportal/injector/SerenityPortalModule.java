package com.github.invictum.reportportal.injector;

import com.epam.reportportal.service.Launch;
import com.github.invictum.reportportal.*;
import com.github.invictum.reportportal.handler.FlatHandler;
import com.github.invictum.reportportal.handler.Handler;
import com.github.invictum.reportportal.handler.HandlerType;
import com.github.invictum.reportportal.handler.TreeHandler;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;

public class SerenityPortalModule extends AbstractModule {

    protected void configure() {
        bind(Launch.class).toProvider(ReportLaunchProvider.class).in(Scopes.SINGLETON);
        bind(LogUnitsHolder.class).toProvider(LogUnitsHolderProvider.class).in(Scopes.SINGLETON);
        bind(LogStorage.class).in(Scopes.SINGLETON);
        bind(ReportIntegrationConfig.class).in(Scopes.SINGLETON);
        bind(SuiteStorage.class).in(Scopes.SINGLETON);
    }

    @Provides
    @Singleton
    private Handler getHandler(ReportIntegrationConfig config) {
        Handler handler = config.handlerType() == HandlerType.FLAT ? new FlatHandler() : new TreeHandler();
        IntegrationInjector.getInjector().injectMembers(handler);
        return handler;
    }
}
