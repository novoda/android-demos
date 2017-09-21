package com.novoda.spritz;

import android.animation.TimeInterpolator;

import java.util.ArrayList;
import java.util.List;

class SpritzStepWithOffset {

    private final long autoPlayDuration;
    private final long autoPlayEnd;
    private final long swipeEnd;
    private final TimeInterpolator swipeForwardInterpolator;
    private final TimeInterpolator swipeBackwardsInterpolator;

    private SpritzStepWithOffset(long autoPlayDuration,
                                 long autoPlayEnd,
                                 long swipeEnd,
                                 TimeInterpolator swipeForwardInterpolator,
                                 TimeInterpolator swipeBackwardsInterpolator) {

        this.autoPlayDuration = autoPlayDuration;
        this.autoPlayEnd = autoPlayEnd;
        this.swipeEnd = swipeEnd;
        this.swipeForwardInterpolator = swipeForwardInterpolator;
        this.swipeBackwardsInterpolator = swipeBackwardsInterpolator;
    }

    long autoPlayDuration() {
        return autoPlayDuration;
    }

    long autoPlayEnd() {
        return autoPlayEnd;
    }

    long swipeEnd() {
        return swipeEnd;
    }

    TimeInterpolator swipeForwardInterpolator() {
        return swipeForwardInterpolator;
    }

    TimeInterpolator swipeBackwardsInterpolator() {
        return swipeBackwardsInterpolator;
    }

    static List<SpritzStepWithOffset> fromSpritzSteps(SpritzStep... spritzSteps) {
        List<SpritzStepWithOffset> spritzStepWithOffsetList = new ArrayList<>(spritzSteps.length);
        long totalAnimationTime = 0;

        for (SpritzStep spritzStep : spritzSteps) {
            long autoPlayEnd = totalAnimationTime + spritzStep.autoPlayDuration();
            long swipeEnd = totalAnimationTime + spritzStep.autoPlayDuration() + spritzStep.swipeDuration();

            spritzStepWithOffsetList.add(
                    new SpritzStepWithOffset(
                            spritzStep.autoPlayDuration(),
                            autoPlayEnd,
                            swipeEnd,
                            spritzStep.swipeForwardInterpolator(),
                            spritzStep.swipeBackwardsInterpolator()
                    )
            );

            totalAnimationTime = swipeEnd;
        }

        return spritzStepWithOffsetList;
    }
}
