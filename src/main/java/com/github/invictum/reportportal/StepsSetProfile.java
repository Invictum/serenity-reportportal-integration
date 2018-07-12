package com.github.invictum.reportportal;

import com.github.invictum.reportportal.processor.*;

/**
 * Describes @{@link StepDataExtractor} sets as a unions.
 * It is possible to specify CUSTOM profile and register to it any @{@link StepDataExtractor}. Order of processor is matters.
 */
public enum StepsSetProfile {

    /**
     * Default profile with pre-defined minimal set of step processors.
     */
    DEFAULT() {
        @Override
        public StepDataExtractor[] processors() {
            return new StepDataExtractor[]{
                    new FinishStep(),
                    new StepScreenshots(),
                    new StepError()
            };
        }

        @Override
        public StepsSetProfile registerProcessors(StepDataExtractor... steps) {
            throw new UnsupportedOperationException("Unable to register processors for DEFAULT profile");
        }
    },

    /**
     * Profile configured with all available step processors.
     */
    FULL() {
        @Override
        public StepDataExtractor[] processors() {
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
        public StepsSetProfile registerProcessors(StepDataExtractor... steps) {
            throw new UnsupportedOperationException("Unable to register processors for FULL profile");
        }
    },

    /**
     * Custom profile for custom steps set. By default returns an empty array of step processors.
     */
    CUSTOM() {

        private StepDataExtractor[] steps;

        @Override
        public StepDataExtractor[] processors() {
            return this.steps;
        }

        @Override
        public StepsSetProfile registerProcessors(StepDataExtractor... steps) {
            this.steps = steps;
            return this;
        }
    },

    /**
     * Processors set designed to use in TREE handler mode.
     */
    TREE_OPTIMIZED() {
        @Override
        StepDataExtractor[] processors() {
            return new StepDataExtractor[]{
                    new StepScreenshots(),
                    new StepError()
            };
        }

        @Override
        public StepsSetProfile registerProcessors(StepDataExtractor... steps) {
            throw new UnsupportedOperationException("Unable to register processors for TREE_OPTIMIZED profile");
        }
    };

    /**
     * Returns an array of associated processors.
     *
     * @return array of processors
     */
    abstract StepDataExtractor[] processors();

    public abstract StepsSetProfile registerProcessors(StepDataExtractor... steps);
}
