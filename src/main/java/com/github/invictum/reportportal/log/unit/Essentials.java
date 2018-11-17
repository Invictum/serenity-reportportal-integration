package com.github.invictum.reportportal.log.unit;

import com.epam.ta.reportportal.ws.model.log.SaveLogRQ;
import com.github.invictum.reportportal.Utils;
import net.thucydides.core.model.TestStep;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

public class Essentials {

    /**
     * Logs started step event
     */
    public static Function<TestStep, Collection<SaveLogRQ>> startStep() {
        return step -> {
            SaveLogRQ log = new SaveLogRQ();
            log.setLogTime(Utils.stepStartDate(step));
            log.setMessage("[STARTED] " + step.getDescription());
            log.setLevel(Utils.logLevel(step.getResult()));
            return Collections.singleton(log);
        };
    }

    /**
     * Logs step finish event
     */
    public static Function<TestStep, Collection<SaveLogRQ>> finishStep() {
        return step -> {
            SaveLogRQ log = new SaveLogRQ();
            String message = String.format("[%s] %s", step.getResult().name(), step.getDescription());
            log.setMessage(message);
            log.setLevel(Utils.logLevel(step.getResult()));
            log.setLogTime(Utils.stepEndDate(step));
            return Collections.singleton(log);
        };
    }
}
