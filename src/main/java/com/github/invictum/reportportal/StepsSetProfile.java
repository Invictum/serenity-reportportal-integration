package com.github.invictum.reportportal;

import com.github.invictum.reportportal.extractor.*;

/**
 * Describes @{@link StepDataExtractor} sets as a unions.
 * It is possible to specify CUSTOM profile and register to it any @{@link StepDataExtractor}.
 */
public enum StepsSetProfile {

    /**
     * Default profile with pre-defined minimal set of step extractors.
     */
    DEFAULT() {
        @Override
        public StepDataExtractor[] extractors() {
            return new StepDataExtractor[]{
                    new FinishStep(),
                    new StepScreenshots(),
                    new StepError()
            };
        }

        @Override
        public StepsSetProfile registerExtractors(StepDataExtractor... steps) {
            throw new UnsupportedOperationException("Unable to register extractors for DEFAULT profile");
        }
    },

    /**
     * Profile configured with all available step extractors.
     */
    FULL() {
        @Override
        public StepDataExtractor[] extractors() {
            return new StepDataExtractor[]{
                    new StartStep(),
                    new StepScreenshots(),
                    new FinishStep(),
                    new StepError(),
                    new HtmlSources(),
                    new SeleniumLogs()
            };
        }

        @Override
        public StepsSetProfile registerExtractors(StepDataExtractor... steps) {
            throw new UnsupportedOperationException("Unable to register extractors for FULL profile");
        }
    },

    /**
     * Custom profile for custom steps set. By default returns an empty array of step extractors.
     */
    CUSTOM() {

        private StepDataExtractor[] steps;

        @Override
        public StepDataExtractor[] extractors() {
            return this.steps;
        }

        @Override
        public StepsSetProfile registerExtractors(StepDataExtractor... steps) {
            this.steps = steps;
            return this;
        }
    },

    /**
     * Processors set designed to use in TREE handler mode.
     */
    TREE_OPTIMIZED() {
        @Override
        StepDataExtractor[] extractors() {
            return new StepDataExtractor[]{
                    new StepScreenshots(),
                    new StepError()
            };
        }

        @Override
        public StepsSetProfile registerExtractors(StepDataExtractor... steps) {
            throw new UnsupportedOperationException("Unable to register extractors for TREE_OPTIMIZED profile");
        }
    };

    /**
     * Returns an array of associated extractors.
     *
     * @return array of extractors
     */
    abstract StepDataExtractor[] extractors();

    public abstract StepsSetProfile registerExtractors(StepDataExtractor... steps);
}
