package com.github.invictum.reportportal.injector;

import com.epam.reportportal.listeners.ListenerParameters;
import com.epam.reportportal.service.Launch;
import com.epam.reportportal.service.ReportPortal;
import com.epam.ta.reportportal.ws.model.FinishExecutionRQ;
import com.epam.ta.reportportal.ws.model.launch.MergeLaunchesRQ;
import com.epam.ta.reportportal.ws.model.launch.StartLaunchRQ;
import com.github.invictum.reportportal.FileStorage;
import com.github.invictum.reportportal.ReportIntegrationConfig;
import com.google.inject.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

public class ReportLaunchProvider implements Provider<Launch> {

    private static final Logger LOG = LoggerFactory.getLogger(ReportLaunchProvider.class);
    private static final String DIR = ReportIntegrationConfig.get().communicationDirectory();
    private static final int MODULES_COUNT = ReportIntegrationConfig.get().modulesQuantity();
    private FileStorage fileStorage;

    @Override
    public Launch get() {
        ReportPortal reportPortal = ReportPortal.builder().build();
        StartLaunchRQ startEvent = buildStartLaunchEvent(reportPortal.getParameters());
        Launch launch = reportPortal.newLaunch(startEvent);
        LOG.debug("Report Portal communication is engaged");
        // Record launch ID
        String id = launch.start().blockingGet();
        // Register shutdown hook. RP connection will be closed before VM shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Finish launch
            FinishExecutionRQ finishExecutionRQ = new FinishExecutionRQ();
            finishExecutionRQ.setEndTime(Calendar.getInstance().getTime());
            reportPortal.getClient().finishLaunch(id, finishExecutionRQ).blockingGet();
            // Activate merge if parameters are passed
            if (DIR != null && MODULES_COUNT > 1) {
                fileStorage = new FileStorage(DIR);
                fileStorage.touch(id);
                // Perform merge
                if (fileStorage.count() == MODULES_COUNT) {
                    LOG.debug("Launches merge is requested");
                    MergeLaunchesRQ merge = buildMergeLaunchesEvent(reportPortal.getParameters());
                    reportPortal.getClient().mergeLaunches(merge).blockingGet();
                }
            }
            reportPortal.getClient().close();
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

    private MergeLaunchesRQ buildMergeLaunchesEvent(ListenerParameters parameters) {
        MergeLaunchesRQ merge = new MergeLaunchesRQ();
        merge.setName(parameters.getLaunchName());
        merge.setTags(parameters.getTags());
        merge.setExtendSuitesDescription(true);
        merge.setMergeStrategyType("DEEP");
        merge.setLaunches(fileStorage.loadAndClean());
        return merge;
    }
}
