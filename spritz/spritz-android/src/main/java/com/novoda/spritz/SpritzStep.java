package com.novoda.spritz;

import java.util.concurrent.TimeUnit;

public class SpritzStep {

    private final long autoPlayDuration;
    private final long swipeDuration;

    private SpritzStep(long autoPlayDuration,
                       long swipeDuration) {

        this.autoPlayDuration = autoPlayDuration;
        this.swipeDuration = swipeDuration;
    }

    long autoPlayDuration() {
        return autoPlayDuration;
    }

    long swipeDuration() {
        return swipeDuration;
    }

    public static class Builder {

        private long autoPlayDuration = 0;
        private long swipeDuration = 0;

        public Builder withAutoPlayDuration(long autoPlayDuration, TimeUnit timeUnit) {
            this.autoPlayDuration = timeUnit.toMillis(autoPlayDuration);
            return this;
        }

        public Builder withSwipeDuration(long swipeDuration, TimeUnit timeUnit) {
            this.swipeDuration = timeUnit.toMillis(swipeDuration);
            return this;
        }

        public SpritzStep build() {
            return new SpritzStep(
                    autoPlayDuration,
                    swipeDuration
            );
        }

    }

}
