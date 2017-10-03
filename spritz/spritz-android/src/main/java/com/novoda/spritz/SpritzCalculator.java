package com.novoda.spritz;

import java.util.List;

class SpritzCalculator {

    private final List<SpritzStepWithOffset> spritzSteps;
    private final long totalAnimationDuration;

    SpritzCalculator(List<SpritzStepWithOffset> spritzSteps, long totalAnimationDuration) {
        this.spritzSteps = spritzSteps;
        this.totalAnimationDuration = totalAnimationDuration;
    }

    float getSwipeEndForPreviousPositionOrZero(int position) {
        float swipeEndProgress = 0;
        if (position > 0) {
            swipeEndProgress = getSwipeEndProgressForPosition(position - 1);
        }
        return swipeEndProgress;
    }

    float getSwipeEndProgressForPosition(int position) {
        return ((float) spritzSteps.get(position).swipeEnd()) / totalAnimationDuration;
    }

    float getAutoPlayEndProgressForPosition(int position) {
        return ((float) spritzSteps.get(position).autoPlayEnd()) / totalAnimationDuration;
    }

}
