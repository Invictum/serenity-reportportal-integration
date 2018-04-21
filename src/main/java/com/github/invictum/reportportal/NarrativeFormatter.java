package com.github.invictum.reportportal;

/**
 * Allows to alter narrative lines before passing it to test description
 */
public interface NarrativeFormatter {
    /**
     * Formats a list of narrative text lines to description
     * Returned {@link String} should be formatted as Markdown because Report Portal expects it
     *
     * @param narrative lines extracted from test or story
     * @return Markdown formatted text
     */
    String format(String[] narrative);
}
