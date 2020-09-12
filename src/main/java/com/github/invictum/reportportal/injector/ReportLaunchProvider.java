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
import io.reactivex.Maybe;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.HashSet;

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
        //We should run launch immediately to avoid problem with rp.client.join functionality
        Maybe<String> launchId = launch.start();
        // Register shutdown hook. RP connection will be closed before VM shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Finish launch
            FinishExecutionRQ finishExecutionRQ = new FinishExecutionRQ();
            finishExecutionRQ.setEndTime(Calendar.getInstance().getTime());
            launch.finish(finishExecutionRQ);
            // Activate merge if parameters are passed
            if (DIR != null && MODULES_COUNT > 1) {
                //Record launch ID and UUID.
                String uuid = launchId.blockingGet();
                Long id = reportPortal.getClient().getLaunchByUuid(uuid).blockingGet().getLaunchId();
                //Init fileStorage
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
        event.setAttributes(parameters.getAttributes());
        event.setDescription(parameters.getDescription());
        event.setRerun(parameters.isRerun());
        event.setRerunOf(parameters.getRerunOf());
        return event;
    }

    private MergeLaunchesRQ buildMergeLaunchesEvent(ListenerParameters parameters) {
        MergeLaunchesRQ merge = new MergeLaunchesRQ();
        merge.setName(parameters.getLaunchName());
        merge.setAttributes(new HashSet<>(parameters.getAttributes()));
        merge.setExtendSuitesDescription(true);
        merge.setMergeStrategyType("DEEP");
        merge.setDescription(parameters.getDescription() == null ? StringUtils.EMPTY : parameters.getDescription());
        merge.setLaunches(fileStorage.loadAndClean());
        return merge;
    }
}
