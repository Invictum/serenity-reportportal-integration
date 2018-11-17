package com.github.invictum.reportportal;

import com.google.inject.Provider;

public class LogUnitsHolderProvider implements Provider<LogUnitsHolder> {

    @Override
    public LogUnitsHolder get() {
        LogUnitsHolder holder = new LogUnitsHolder();
        holder.register(ReportIntegrationConfig.get().preset().logUnits());
        return holder;
    }
}
