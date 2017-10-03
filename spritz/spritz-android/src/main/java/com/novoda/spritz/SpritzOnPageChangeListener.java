package com.novoda.spritz;

import android.support.v4.view.ViewPager;

import java.util.List;

class SpritzOnPageChangeListener implements ViewPager.OnPageChangeListener, SpritzOnPageIdleListener {

    private final List<SpritzStepWithOffset> spritzStepsWithOffset;
    private final SpritzCalculator spritzCalculator;
    private final SpritzAnimation spritzAnimation;
    private final SpritzAnimator spritzAnimator;
    private final SpritzPager spritzPager;

    SpritzOnPageChangeListener(List<SpritzStepWithOffset> spritzStepsWithOffset,
                               SpritzCalculator spritzCalculator,
                               SpritzAnimation spritzAnimation,
                               SpritzAnimator spritzAnimator,
                               SpritzPager spritzPager) {

        this.spritzStepsWithOffset = spritzStepsWithOffset;
        this.spritzCalculator = spritzCalculator;
        this.spritzAnimation = spritzAnimation;
        this.spritzAnimator = spritzAnimator;
        this.spritzPager = spritzPager;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        float currentProgress = spritzAnimation.getProgress();
        float finalProgress;
        float realOffset;

        if (position + positionOffset == spritzPager.getCurrentPosition()) {
            return;
        }

        spritzAnimator.cancelCurrentAnimations();

        if (swipingForward(position + positionOffset)) {
            finalProgress = spritzCalculator.getSwipeEndProgressForPosition(position);
            realOffset = positionOffset;
        } else {
            finalProgress = spritzCalculator.getAutoPlayEndProgressForPosition(position);
            realOffset = 1 - positionOffset;
        }

        float progressToAnimate = finalProgress - currentProgress;
        float newProgress = currentProgress + (progressToAnimate * realOffset);

        spritzAnimation.setProgress(newProgress);

        spritzPager.setCachedPosition(position + positionOffset);
    }

    @Override
    public void onPageSelected(final int position) {
        finishSwipeWithAnimation(position);
        spritzPager.setCachedPosition(position);
    }

    private void finishSwipeWithAnimation(int position) {
        float from = spritzAnimation.getProgress();

        if (swipingForward(position)) {
            float to = spritzCalculator.getSwipeEndForPreviousPositionOrZero(position);
            SpritzStepWithOffset currentStep = spritzStepsWithOffset.get(position);
            spritzAnimator.finishSwipeForward(from, to, currentStep);
        } else {
            float to = spritzCalculator.getAutoPlayEndProgressForPosition(position);
            SpritzStepWithOffset currentStep = spritzStepsWithOffset.get(position);
            spritzAnimator.finishSwipeBackwards(from, to, currentStep);
        }
    }

    private boolean swipingForward(float newPosition) {
        return newPosition >= spritzPager.getCachedPosition();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        int position = spritzPager.getCurrentPosition();
        if (state == ViewPager.SCROLL_STATE_IDLE
                && spritzAnimation.getProgress() < spritzCalculator.getAutoPlayEndProgressForPosition(position)) {
            onPageIdle(position);
        }
    }

    @Override
    public void onPageIdle(int position) {
        autoPlay(position);
    }

    private void autoPlay(int position) {
        float currentProgress = spritzAnimation.getProgress();
        float autoPlayEndProgress = spritzCalculator.getAutoPlayEndProgressForPosition(position);
        SpritzStepWithOffset currentStep = spritzStepsWithOffset.get(position);

        spritzAnimator.cancelCurrentAnimations();
        spritzAnimator.autoPlay(currentProgress, autoPlayEndProgress, currentStep);
    }

}
