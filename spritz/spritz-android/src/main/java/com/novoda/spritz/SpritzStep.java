package com.novoda.spritz;

import android.animation.TimeInterpolator;

import java.util.concurrent.TimeUnit;

public class SpritzStep {

    private final long autoPlayDuration;
    private final long swipeDuration;
    private final TimeInterpolator swipeForwardInterpolator;
    private final TimeInterpolator swipeBackwardsInterpolator;

    private SpritzStep(long autoPlayDuration,
                       long swipeDuration,
                       TimeInterpolator swipeForwardInterpolator,
                       TimeInterpolator swipeBackwardsInterpolator) {

        this.autoPlayDuration = autoPlayDuration;
        this.swipeDuration = swipeDuration;
        this.swipeForwardInterpolator = swipeForwardInterpolator;
        this.swipeBackwardsInterpolator = swipeBackwardsInterpolator;
    }

    long autoPlayDuration() {
        return autoPlayDuration;
    }

    long swipeDuration() {
        return swipeDuration;
    }

    TimeInterpolator swipeForwardInterpolator() {
        return swipeForwardInterpolator;
    }

    TimeInterpolator swipeBackwardsInterpolator() {
        return swipeBackwardsInterpolator;
    }

    public static class Builder {

        private long autoPlayDuration = 0;
        private long swipeDuration = 0;
        private TimeInterpolator swipeForwardInterpolator;
        private TimeInterpolator swipeBackwardsInterpolator;

        public Builder withAutoPlayDuration(long autoPlayDuration, TimeUnit timeUnit) {
            this.autoPlayDuration = timeUnit.toMillis(autoPlayDuration);
            return this;
        }

        public Builder withSwipeDuration(long swipeDuration, TimeUnit timeUnit) {
            this.swipeDuration = timeUnit.toMillis(swipeDuration);
            return this;
        }

        public Builder withSwipeForwardInterpolator(TimeInterpolator swipeForwardInterpolator) {
            this.swipeForwardInterpolator = swipeForwardInterpolator;
            return this;
        }

        public Builder withSwipeBackwardsInterpolator(TimeInterpolator swipeBackwardsInterpolator) {
            this.swipeBackwardsInterpolator = swipeBackwardsInterpolator;
            return this;
        }

        public SpritzStep build() {
            return new SpritzStep(
                    autoPlayDuration,
                    swipeDuration,
                    swipeForwardInterpolator,
                    swipeBackwardsInterpolator
            );
        }

    }

}
