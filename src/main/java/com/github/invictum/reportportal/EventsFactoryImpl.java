package com.github.invictum.reportportal;

import com.epam.reportportal.listeners.ListenerParameters;
import com.epam.ta.reportportal.ws.model.FinishExecutionRQ;
import com.epam.ta.reportportal.ws.model.FinishTestItemRQ;
import com.epam.ta.reportportal.ws.model.StartTestItemRQ;
import com.epam.ta.reportportal.ws.model.launch.StartLaunchRQ;
import net.thucydides.core.model.TestOutcome;

import java.util.Date;

public class EventsFactoryImpl implements EventsFactory {

    @Override
    public StartLaunchRQ buildStartLaunch(ListenerParameters parameters, Date startTime) {
        StartLaunchRQ event = new StartLaunchRQ();
        event.setName(parameters.getLaunchName());
        event.setStartTime(startTime);
        event.setMode(parameters.getLaunchRunningMode());
        event.setAttributes(parameters.getAttributes());
        event.setDescription(parameters.getDescription());
        event.setRerun(parameters.isRerun());
        event.setRerunOf(parameters.getRerunOf());
        return event;
    }

    @Override
    public FinishExecutionRQ buildFinishLaunch(Date endTime) {
        FinishExecutionRQ event = new FinishExecutionRQ();
        event.setEndTime(endTime);
        return event;
    }

    @Override
    public StartTestItemRQ buildStartSuite(TestOutcome outcome) {
        return null;
    }

    @Override
    public FinishTestItemRQ buildFinishSuite(TestOutcome outcome) {
        return null;
    }
}
