package com.github.invictum.reportportal;

import com.google.inject.AbstractModule;

public class SerenityPortalModule extends AbstractModule {

    protected void configure() {
        bind(Handler.class).to(StepsTreeHandler.class).asEagerSingleton();
    }
}
