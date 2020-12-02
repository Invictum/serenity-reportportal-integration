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
        /*
        We test if storyNarrative is null, in that case we evaluate classNarrative method,
          if this method is empty and storyNarrative null, then feature narrative is empty
         */
        return Optional.ofNullable(storyNarrative == null ? classNarrative() : storyNarrative);
    }

    private String classNarrative() {
        Class testCase = test.getTestCase();
        //We test if test case is null, in that case narrative extracted from TestOutcome should be empty
        if(testCase == null)    return "";
        return NarrativeFinder.forClass(testCase).map(formatter).orElse(null);
    }
}
