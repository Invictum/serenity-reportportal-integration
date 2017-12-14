package com.github.invictum.reportportal.handler;

import com.github.invictum.reportportal.EventData;
import com.github.invictum.reportportal.ItemType;
import com.github.invictum.reportportal.Status;
import com.github.invictum.reportportal.storage.Storage;
import com.github.invictum.reportportal.storage.events.*;

/**
 * Basic handler realization. Designed as thread-safe.
 */
public abstract class BasicHandler implements Handler {

    protected ThreadLocal<Storage> storage = ThreadLocal.withInitial(Storage::new);

    @Override
    public void init() {

    }

    @Override
    public void startSuite(EventData suiteData) {
        StartSuiteEvent startSuiteEvent = new StartSuiteEvent(suiteData.getName());
        storage.get().fire(startSuiteEvent);
    }

    @Override
    public void finishSuite() {
        StopSuiteEvent stopSuiteEvent = new StopSuiteEvent();
        storage.get().fire(stopSuiteEvent);
        storage.get().dump();
        storage.remove();
    }

    @Override
    public void startTest(EventData testData) {
        StartTestEvent startTestEvent = new StartTestEvent(testData.getName());
        storage.get().fire(startTestEvent);
    }

    @Override
    public void finishTest() {
        storage.get().fire(new StopTestEvent());
    }

    @Override
    public void failTest(Throwable cause) {
        storage.get().fire(new UpdateStatusEvent(ItemType.TEST, Status.FAILED));
        storage.get().fire(new ReportErrorEvent("Test failed", cause));
    }

    @Override
    public void ignoreTest() {
        storage.get().fire(new UpdateStatusEvent(ItemType.TEST, Status.SKIPPED));
    }
}
