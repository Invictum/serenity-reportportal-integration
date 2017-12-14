package com.github.invictum.reportportal.handler;


import com.github.invictum.reportportal.EventData;
import com.github.invictum.reportportal.MessageLevel;
import com.github.invictum.reportportal.Status;
import com.github.invictum.reportportal.storage.events.*;

/**
 * Handles steps as a tree of nested entities.
 * Realization supposed to be thread-safe.
 */
public class StepsTreeHandler extends BasicHandler {

    @Override
    public void startStep(EventData stepData) {
        StartStepEvent startStepEvent = new StartStepEvent(stepData.getName());
        storage.get().fire(startStepEvent);
    }

    @Override
    public void finishStep() {
        storage.get().fire(new StopStepEvent());
    }

    @Override
    public void failStep(Throwable throwable) {
        storage.get().fire(new UpdateStatusEvent(Status.FAILED));
        storage.get().fire(new ReportErrorEvent(throwable));
        storage.get().fire(new StopStepEvent());
    }

    @Override
    public void ignoreStep() {
        storage.get().fire(new UpdateStatusEvent(Status.SKIPPED));
        storage.get().fire(new StopStepEvent());
    }

    @Override
    public void ignoreStep(String reason) {
        storage.get().fire(new UpdateStatusEvent(Status.SKIPPED));
        storage.get().fire(new MessageEvent(reason, MessageLevel.WARN));
        storage.get().fire(new StopStepEvent());
    }
}
