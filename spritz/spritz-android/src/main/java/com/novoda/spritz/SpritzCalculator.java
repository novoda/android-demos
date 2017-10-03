package com.novoda.spritz;

import java.util.List;

class SpritzCalculator {

    private final List<SpritzStepWithOffset> spritzSteps;
    private final long totalAnimationDuration;

    SpritzCalculator(List<SpritzStepWithOffset> spritzSteps, long totalAnimationDuration) {
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
