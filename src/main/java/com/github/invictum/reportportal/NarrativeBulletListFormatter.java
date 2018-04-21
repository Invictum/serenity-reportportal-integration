package com.github.invictum.reportportal;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Format narrative as a bullet list
 */
public class NarrativeBulletListFormatter implements NarrativeFormatter {

    @Override
    public String format(String[] narrative) {
        return Stream.of(narrative).map(item -> "* " + item).collect(Collectors.joining("\n"));
    }
}
