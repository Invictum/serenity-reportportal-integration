package com.github.invictum.reportportal.handler;

import com.epam.reportportal.service.Launch;
import com.github.invictum.reportportal.LogUnitsHolder;
import com.google.inject.AbstractModule;

public class TestHandlerModule extends AbstractModule {

    private Launch launch;
    private LogUnitsHolder holder;

    public TestHandlerModule(Launch launch, LogUnitsHolder holder) {
        this.launch = launch;
        this.holder = holder;
    }

    @Override
    protected void configure() {
        bind(Launch.class).toInstance(launch);
        bind(LogUnitsHolder.class).toInstance(holder);
    }
}
