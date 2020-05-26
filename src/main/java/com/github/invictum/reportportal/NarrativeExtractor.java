package com.github.invictum.reportportal;

import net.thucydides.core.annotations.Narrative;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.requirements.annotations.NarrativeFinder;

import java.util.Optional;
import java.util.function.Function;

/**
 * Narrative extractor used to discover narrative for different types of tests
 */
public class NarrativeExtractor {

    private TestOutcome test;
    private Function<Narrative, String> formatter;

    public NarrativeExtractor(TestOutcome test, Function<Narrative, String> formatter) {
        this.test = test;
        this.formatter = formatter;
    }

    public Optional<String> extract() {
        String storyNarrative = test.getUserStory().getNarrative();
        return Optional.ofNullable(storyNarrative == null ? classNarrative() : storyNarrative);
    }

    private String classNarrative() {
        return NarrativeFinder.forClass(test.getTestCase()).map(formatter).orElse(null);
    }
}
