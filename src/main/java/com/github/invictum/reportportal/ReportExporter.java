package com.github.invictum.reportportal;

import com.epam.reportportal.service.Launch;
import com.epam.reportportal.service.ReportPortal;
import com.epam.ta.reportportal.ws.model.launch.StartLaunchRQ;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Collectors;

public class ReportExporter implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportExporter.class);

    private final Context context;

    public ReportExporter(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        Date endTime = Calendar.getInstance().getTime();
        EventsFactory factory = ReportIntegrationConfig.get().eventsFactory;
        // Start launch
        StartLaunchRQ startLaunchEvent = factory.buildStartLaunch(context.rpParams, context.startTime);
        final Launch launch = ReportPortal.builder().build().newLaunch(startLaunchEvent);
        // Proceed suits
        ExecutorService service = prepareService();
        List<Callable<Boolean>> tasks = context.suites.entrySet().stream()
                .map(e -> new SuiteExporter(e.getKey(), e.getValue(), launch))
                .collect(Collectors.toList());
        try {
            service.invokeAll(tasks);
        } catch (InterruptedException e) {
            LOGGER.error("Export batch was interrupted. RP launch might be corrupted");
        }
        service.shutdown();
        // Finish launch
        launch.finish(factory.buildFinishLaunch(endTime));
    }

    private static ExecutorService prepareService() {
        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("ExporterThread-%d").setDaemon(true).build();
        return Executors.newFixedThreadPool(ReportIntegrationConfig.get().exportThreadsQuantity, factory);
    }
}
