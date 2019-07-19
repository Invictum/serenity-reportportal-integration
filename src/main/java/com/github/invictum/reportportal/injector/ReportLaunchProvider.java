package com.github.invictum.reportportal.injector;

import com.epam.reportportal.listeners.ListenerParameters;
import com.epam.reportportal.service.Launch;
import com.epam.reportportal.service.ReportPortal;
import com.epam.ta.reportportal.ws.model.FinishExecutionRQ;
import com.epam.ta.reportportal.ws.model.launch.StartLaunchRQ;
import com.google.inject.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

public class ReportLaunchProvider implements Provider<Launch> {

    private static final Logger LOG = LoggerFactory.getLogger(ReportLaunchProvider.class);

    @Override
    public Launch get() {
        ReportPortal reportPortal = ReportPortal.builder().build();
        StartLaunchRQ startEvent = buildStartLaunchEvent(reportPortal.getParameters());
        Launch launch = reportPortal.newLaunch(startEvent);
        LOG.debug("Report Portal communication is engaged");
        /* Register shutdown hook. RP connection will be closed before VM shutdown */
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            FinishExecutionRQ finishExecutionRQ = new FinishExecutionRQ();
            finishExecutionRQ.setEndTime(Calendar.getInstance().getTime());
            launch.finish(finishExecutionRQ);
            LOG.debug("Report Portal communication is disengaged");
        }));
        return launch;
    }

    private StartLaunchRQ buildStartLaunchEvent(ListenerParameters parameters) {
        StartLaunchRQ event = new StartLaunchRQ();
        event.setName(parameters.getLaunchName());
        event.setStartTime(Calendar.getInstance().getTime());
        event.setMode(parameters.getLaunchRunningMode());
        event.setTags(parameters.getTags());
        event.setDescription(parameters.getDescription());
        return event;
    }
}
