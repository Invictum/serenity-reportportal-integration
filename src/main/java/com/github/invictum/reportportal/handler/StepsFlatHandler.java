package com.github.invictum.reportportal.handler;

import com.github.invictum.reportportal.EventData;
import com.github.invictum.reportportal.MessageLevel;
import com.github.invictum.reportportal.storage.events.MessageEvent;
import com.github.invictum.reportportal.storage.events.ReportErrorEvent;

/**
 * Handles steps as a flat list of entities.
 * Realization supposed to be thread-safe.
 * Uploads execution results lazy by suite chunks.
 */
public class StepsFlatHandler extends BasicHandler {

    @Override
    public void startStep(EventData stepData) {
        storage.get().fire(new MessageEvent("STEP: " + stepData.getName()));
    }

    @Override
    public void finishStep() {
    }

    @Override
    public void failStep(Throwable throwable) {
        storage.get().fire(new ReportErrorEvent("Step failed", throwable));
    }

    @Override
    public void ignoreStep() {
    }

    @Override
    public void ignoreStep(String reason) {
        storage.get().fire(new MessageEvent("Step ig" + "nored: " + reason, MessageLevel.WARN));
    }
}
