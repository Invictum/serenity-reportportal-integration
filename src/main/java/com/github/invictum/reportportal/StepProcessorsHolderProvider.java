package com.github.invictum.reportportal;

import com.google.inject.Provider;

public class StepProcessorsHolderProvider implements Provider<StepDataExtractorsHolder> {

    @Override
    public StepDataExtractorsHolder get() {
        StepDataExtractorsHolder holder = new StepDataExtractorsHolder();
        holder.register(ReportIntegrationConfig.get().profile().extractors());
        return holder;
    }
}
