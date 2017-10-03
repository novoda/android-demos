package com.novoda.spritz;

import java.util.ArrayList;
import java.util.List;

class SpritzStepWithOffset {

    private final long autoPlayDuration;
    private final long autoPlayEnd;
    private final long swipeEnd;

    private SpritzStepWithOffset(long autoPlayDuration,
                                 long autoPlayEnd,
                                 long swipeEnd) {

        this.autoPlayDuration = autoPlayDuration;
        this.autoPlayEnd = autoPlayEnd;
        this.swipeEnd = swipeEnd;
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
                            swipeEnd
                    )
            );

            totalAnimationTime = swipeEnd;
        }

        return spritzStepWithOffsetList;
    }
}
