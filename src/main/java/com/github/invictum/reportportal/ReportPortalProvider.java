package com.github.invictum.reportportal;

import com.epam.reportportal.guice.Injector;
import com.epam.reportportal.listeners.ListenerParameters;
import com.epam.reportportal.service.ReportPortal;
import com.epam.reportportal.service.ReportPortalClient;
import com.epam.ta.reportportal.ws.model.FinishExecutionRQ;
import com.epam.ta.reportportal.ws.model.launch.StartLaunchRQ;
import com.google.inject.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

public class ReportPortalProvider implements Provider<ReportPortal> {

    private static final Logger LOG = LoggerFactory.getLogger(ReportPortalProvider.class);

    @Override
    public ReportPortal get() {
        /* Load configurations and client setup */
        final Injector injector = Injector.createDefault();
        ListenerParameters parameters = injector.getBean(ListenerParameters.class);
        ReportPortalClient client = injector.getBean(ReportPortalClient.class);
        /* Assemble start launch event */
        StartLaunchRQ startEvent = new StartLaunchRQ();
        startEvent.setName(parameters.getLaunchName());
        startEvent.setStartTime(Calendar.getInstance().getTime());
        startEvent.setMode(parameters.getLaunchRunningMode());
        startEvent.setTags(parameters.getTags());
        startEvent.setDescription(parameters.getDescription());
        /* Start launch session */
        ReportPortal portal = ReportPortal.startLaunch(client, parameters, startEvent);
        LOG.info("Report Portal communication is engaged");
        /* Register shutdown hook. RP connection will be closed before VM shutdown */
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            FinishExecutionRQ finishExecutionRQ = new FinishExecutionRQ();
            finishExecutionRQ.setEndTime(Calendar.getInstance().getTime());
            portal.finishLaunch(finishExecutionRQ);
            LOG.info("Report Portal communication is disengaged");
        }));
        return portal;
    }
}
