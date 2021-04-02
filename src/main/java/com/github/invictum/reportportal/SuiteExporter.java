package com.github.invictum.reportportal;

import com.epam.reportportal.service.Launch;
import com.epam.ta.reportportal.ws.model.FinishTestItemRQ;
import com.epam.ta.reportportal.ws.model.StartTestItemRQ;
import io.reactivex.Maybe;
import net.thucydides.core.guice.Injectors;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.reports.json.JSONConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

public class SuiteExporter implements Callable<Boolean> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SuiteExporter.class);

    private final String id;
    private final LinkedHashSet<String> files;
    private final Launch launch;

    public SuiteExporter(String id, LinkedHashSet<String> files, Launch launch) {
        this.id = id;
        this.files = files;
        this.launch = launch;
    }

    @Override
    public Boolean call() {
        EventsFactory factory = ReportIntegrationConfig.get().eventsFactory;
        Maybe<String> suiteId = null;
        TestOutcome last = null;
        for (String fileName : files) {
            Optional<TestOutcome> outcome = loadFromFile(fileName);
            if (!outcome.isPresent()) {
                continue;
            }
            for (TestOutcome test : normalize(outcome.get())) {
                if (suiteId == null) {
                    StartTestItemRQ startSuiteEvent = factory.buildStartSuite(outcome.get());
                    suiteId = launch.startTestItem(startSuiteEvent);
                }
                last = outcome.get();
                // Proceed test
                proceedTest(suiteId, test);
            }
        }
        if (suiteId != null) {
            FinishTestItemRQ finishSuiteEvent = factory.buildFinishSuite(last);
            launch.finishTestItem(suiteId, finishSuiteEvent);
        }
        return true;
    }

    private Optional<TestOutcome> loadFromFile(String name) {
        JSONConverter converter = Injectors.getInjector().getInstance(JSONConverter.class);
        try {
            InputStream inputStream = new FileInputStream(name);
            return converter.fromJson(inputStream);
        } catch (IOException e) {
            LOGGER.error("Unable to load test outcome for {}", name);
            return Optional.empty();
        }
    }

    private List<TestOutcome> normalize(TestOutcome outcome) {
        if (!outcome.isDataDriven()) {
            return Collections.singletonList(outcome);
        }
        // TODO: Implement split
        return Collections.singletonList(outcome);
    }

    private void proceedTest(Maybe<String> parentId, TestOutcome outcome) {

    }
}
