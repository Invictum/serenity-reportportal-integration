package com.github.invictum.reportportal;

import com.github.invictum.reportportal.processor.*;

/**
 * Describes @{@link StepProcessor} sets as a unions.
 * It is possible to specify CUSTOM profile and register to it any @{@link StepProcessor}. Order of processor is matters.
 */
public enum StepsSetProfile {

    /**
     * Default profile with pre-defined minimal set of step processors.
     */
    DEFAULT() {
        @Override
        public StepProcessor[] processors() {
            return new StepProcessor[]{
                    new FinishStepLogger(),
                    new ScreenshotAttacher(),
                    new ErrorLogger(true)
            };
        }

        @Override
        public StepsSetProfile registerProcessors(StepProcessor... steps) {
            throw new UnsupportedOperationException("Unable to register processors for DEFAULT profile");
        }
    },

    /**
     * Profile configured with all available step processors.
     */
    FULL() {
        @Override
        public StepProcessor[] processors() {
            return new StepProcessor[]{
                    new StartStepLogger(),
                    new ScreenshotAttacher(),
                    new FinishStepLogger(),
                    new ErrorLogger(true),
                    new HtmlSourceAttacher(),
                    new SeleniumLogsAttacher()
            };
        }

        @Override
        public StepsSetProfile registerProcessors(StepProcessor... steps) {
            throw new UnsupportedOperationException("Unable to register processors for FULL profile");
        }
    },

    /**
     * Custom profile for custom steps set. By default returns an empty array of step processors.
     */
    CUSTOM() {

        private StepProcessor[] steps;

        @Override
        public StepProcessor[] processors() {
            return this.steps;
        }

        @Override
        public StepsSetProfile registerProcessors(StepProcessor... steps) {
            this.steps = steps;
            return this;
        }
    };

    /**
     * Returns an array of associated processors.
     *
     * @return array of processors
     */
    abstract StepProcessor[] processors();

    public abstract StepsSetProfile registerProcessors(StepProcessor... steps);
}
