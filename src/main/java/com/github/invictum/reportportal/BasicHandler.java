package com.github.invictum.reportportal;

import com.epam.reportportal.guice.Injector;
import com.epam.reportportal.listeners.ListenerParameters;
import com.epam.reportportal.service.ReportPortal;
import com.epam.reportportal.service.ReportPortalClient;
import com.epam.ta.reportportal.ws.model.FinishExecutionRQ;
import com.epam.ta.reportportal.ws.model.FinishTestItemRQ;
import com.epam.ta.reportportal.ws.model.StartTestItemRQ;
import com.epam.ta.reportportal.ws.model.launch.StartLaunchRQ;
import io.reactivex.Maybe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

/**
 * Basic handler realization. Designed as thread-safe.
 */
public abstract class BasicHandler implements Handler {

    private static final Logger LOG = LoggerFactory.getLogger(BasicHandler.class);

    protected ReportPortal portal;
    protected ThreadLocal<Maybe<String>> activeTest = new ThreadLocal<>();
    private ThreadLocal<Maybe<String>> activeSuite = new ThreadLocal<>();

    @Override
    public void init() {
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
        portal = ReportPortal.startLaunch(client, parameters, startEvent);
        LOG.debug("Report Portal communication engaged");
        registerShutDownHook();
    }

    @Override
    public void startSuite(StartTestItemRQ suiteDetails) {
        if (activeSuite.get() == null) {
            suiteDetails.setType(ItemType.TEST.toString());
            suiteDetails.setStartTime(Calendar.getInstance().getTime());
            activeSuite.set(portal.startTestItem(suiteDetails));
        }
    }

    @Override
    public void finishSuite() {
        if (activeSuite.get() != null) {
            FinishTestItemRQ finishTestItem = new FinishTestItemRQ();
            finishTestItem.setEndTime(Calendar.getInstance().getTime());
            portal.finishTestItem(activeSuite.get(), finishTestItem);
            activeSuite.remove();
        }
    }

    @Override
    public void startTest(StartTestItemRQ testDetails) {
        if (activeSuite.get() != null && activeTest.get() == null) {
            testDetails.setType(ItemType.STEP.toString());
            testDetails.setStartTime(Calendar.getInstance().getTime());
            activeTest.set(portal.startTestItem(activeSuite.get(), testDetails));
        }
    }

    @Override
    public void finishTest() {
        if (activeTest.get() != null) {
            FinishTestItemRQ finishTest = new FinishTestItemRQ();
            finishTest.setEndTime(Calendar.getInstance().getTime());
            finishTest.setStatus("PASSED");
            portal.finishTestItem(activeTest.get(), finishTest);
            activeTest.remove();
        }
    }

    @Override
    public void failTest(Throwable cause) {
        if (activeTest.get() != null) {
            ReportPortal.emitLog(Utils.verboseError(cause), "ERROR", Calendar.getInstance().getTime());
            FinishTestItemRQ finishTest = new FinishTestItemRQ();
            finishTest.setEndTime(Calendar.getInstance().getTime());
            finishTest.setStatus("FAILED");
            portal.finishTestItem(activeTest.get(), finishTest);
            activeTest.remove();
        }
    }

    @Override
    public void ignoreTest() {
        if (activeTest.get() != null) {
            FinishTestItemRQ finishTest = new FinishTestItemRQ();
            finishTest.setEndTime(Calendar.getInstance().getTime());
            finishTest.setStatus("SKIPPED");
            portal.finishTestItem(activeTest.get(), finishTest);
            activeTest.remove();
        }
    }

    private void registerShutDownHook() {
        /* TODO: Possible lock in case of non-closed entities. There should be more safe solution */
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            FinishExecutionRQ finishExecutionRQ = new FinishExecutionRQ();
            finishExecutionRQ.setEndTime(Calendar.getInstance().getTime());
            portal.finishLaunch(finishExecutionRQ);
            LOG.debug("Report Portal communication disengaged");
        }));
    }
}
