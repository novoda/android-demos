package com.novoda.spritz;

import java.util.List;

class ProgressCalculator {

    private final List<SpritzStepWithOffset> spritzSteps;
    private final long totalAnimationDuration;

    ProgressCalculator(List<SpritzStepWithOffset> spritzSteps, long totalAnimationDuration) {
        this.spritzSteps = spritzSteps;
        this.totalAnimationDuration = totalAnimationDuration;
    }

    float getSwipeEndProgressForPosition(int position) {
        return ((float) spritzSteps.get(position).swipeEnd()) / totalAnimationDuration;
    }

    float getAutoPlayEndProgressForPosition(int position) {
        return ((float) spritzSteps.get(position).autoPlayEnd()) / totalAnimationDuration;
    }

}
