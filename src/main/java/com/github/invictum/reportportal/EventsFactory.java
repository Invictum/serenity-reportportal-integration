package com.github.invictum.reportportal;

import com.epam.reportportal.listeners.ListenerParameters;
import com.epam.ta.reportportal.ws.model.FinishExecutionRQ;
import com.epam.ta.reportportal.ws.model.FinishTestItemRQ;
import com.epam.ta.reportportal.ws.model.StartTestItemRQ;
import com.epam.ta.reportportal.ws.model.launch.StartLaunchRQ;
import net.thucydides.core.model.TestOutcome;

import java.util.Date;

public interface EventsFactory {
    StartLaunchRQ buildStartLaunch(ListenerParameters parameters, Date startTime);
    FinishExecutionRQ buildFinishLaunch(Date endTime);
    StartTestItemRQ buildStartSuite(TestOutcome outcome);
    FinishTestItemRQ buildFinishSuite(TestOutcome outcome);
}
