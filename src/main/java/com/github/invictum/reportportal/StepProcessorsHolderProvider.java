package com.github.invictum.reportportal;

import com.google.inject.Provider;

public class StepProcessorsHolderProvider implements Provider<StepProcessorsHolder> {

    @Override
    public StepProcessorsHolder get() {
        StepProcessorsHolder holder = new StepProcessorsHolder();
        holder.register(ReportIntegrationConfig.get().profile().processors());
        return holder;
    }
}
