package com.github.invictum.reportportal.handler;

import com.epam.reportportal.service.ReportPortal;
import com.epam.ta.reportportal.ws.model.FinishTestItemRQ;
import com.epam.ta.reportportal.ws.model.StartTestItemRQ;
import com.github.invictum.reportportal.*;
import com.google.inject.Inject;
import io.reactivex.Maybe;

import java.util.Calendar;

public abstract class BaseHandler implements Handler {

    protected ThreadLocal<Test> test = ThreadLocal.withInitial(Test::new);
    private ThreadLocal<Maybe<String>> suite = new ThreadLocal<>();

    @Inject
    protected ReportPortal portal;

    @Override
    public void startSuite(EventData suiteData) {
        if (suite.get() == null) {
            StartTestItemRQ startSuite = new StartTestItemRQ();
            startSuite.setType(ItemType.SUITE.key());
            startSuite.setName(suiteData.getName());
            startSuite.setStartTime(Calendar.getInstance().getTime());
            startSuite.setDescription(suiteData.getDescription());
            startSuite.setTags(suiteData.getTags());
            suite.set(portal.startTestItem(startSuite));
        }
    }

    @Override
    public void finishSuite() {
        if (suite.get() != null) {
            FinishTestItemRQ finishSuite = new FinishTestItemRQ();
            finishSuite.setEndTime(Calendar.getInstance().getTime());
            finishSuite.setStatus(Status.PASSED.toString());
            portal.finishTestItem(suite.get(), finishSuite);
            suite.remove();
        }
    }

    @Override
    public void startTest(EventData testData) {
        if (test.get().getId() == null) {
            StartTestItemRQ startTest = new StartTestItemRQ();
            startTest.setType(ItemType.TEST.key());
            startTest.setName(testData.getName());
            startTest.setStartTime(Calendar.getInstance().getTime());
            startTest.setDescription(testData.getDescription());
            startTest.setTags(testData.getTags());
            test.get().setId(portal.startTestItem(suite.get(), startTest));
        }
    }

    @Override
    public void finishTest() {
        if (test.get().getId() != null) {
            FinishTestItemRQ finishTest = new FinishTestItemRQ();
            finishTest.setEndTime(Calendar.getInstance().getTime());
            finishTest.setStatus(test.get().getStatus().toString());
            portal.finishTestItem(test.get().getId(), finishTest);
            test.remove();
        }
    }

    @Override
    public void failTest(Throwable cause) {
        test.get().setStatus(Status.FAILED);
        String errorMessage = Utils.verboseError(cause);
        ReportPortal.emitLog(errorMessage, MessageLevel.ERROR.toString(), Calendar.getInstance().getTime());
    }

    @Override
    public void ignoreTest() {
        test.get().setStatus(Status.SKIPPED);
    }
}
