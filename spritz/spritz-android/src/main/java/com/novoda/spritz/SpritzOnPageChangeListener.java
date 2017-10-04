package com.novoda.spritz;

import android.support.v4.view.ViewPager;
import android.util.Log;

import java.util.List;
import java.util.Locale;

class SpritzOnPageChangeListener implements ViewPager.OnPageChangeListener {

    private final static String TAG = "Spritz";

    private final List<SpritzStepWithOffset> spritzStepsWithOffset;
    private final ProgressCalculator progressCalculator;
    private final Animation animation;
    private final AnimationRunner animationRunner;
    private final SpritzPager spritzPager;

    SpritzOnPageChangeListener(List<SpritzStepWithOffset> spritzStepsWithOffset,
                               ProgressCalculator progressCalculator,
                               Animation animation,
                               AnimationRunner animationRunner,
                               SpritzPager spritzPager) {

        this.spritzStepsWithOffset = spritzStepsWithOffset;
        this.progressCalculator = progressCalculator;
        this.animation = animation;
        this.animationRunner = animationRunner;
        this.spritzPager = spritzPager;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        float newPosition = position + positionOffset;

        if (newPosition == spritzPager.getCurrentPosition() || newPosition == spritzPager.getCachedPosition()) {
            return;
        }

        animationRunner.cancelCurrentAnimations();

        float finalProgress;
        float realOffset;

        if (swipingForward(newPosition)) {
            finalProgress = progressCalculator.getSwipeEndProgressForPosition(position);
            realOffset = positionOffset;
            log(String.format(Locale.ENGLISH, "Swiping > %d+%f", position, positionOffset));
        } else {
            finalProgress = progressCalculator.getAutoPlayEndProgressForPosition(position);
            realOffset = 1 - positionOffset;
            log(String.format(Locale.ENGLISH, "Swiping < %d+%f", position, positionOffset));
        }

        float currentProgress = animation.getCurrentProgress();
        float progressToAnimate = finalProgress - currentProgress;
        float newProgress = currentProgress + (progressToAnimate * realOffset);

        animation.setProgressImmediately(newProgress);

        spritzPager.setCachedPosition(newPosition);
    }

    @Override
    public void onPageSelected(final int position) {
        // do nothing
    }

    private boolean swipingForward(float newPosition) {
        return newPosition > spritzPager.getCachedPosition();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        int position = spritzPager.getCurrentPosition();
        float currentProgress = animation.getCurrentProgress();
        if (state == ViewPager.SCROLL_STATE_IDLE
                && currentProgress < progressCalculator.getAutoPlayEndProgressForPosition(position)) {
            onPageIdle(position);
        }
    }

    private void onPageIdle(int position) {
        log(String.format(Locale.ENGLISH, "Page idle, autoplaying for position %d", position));
        autoPlay(position);
        spritzPager.setCachedPosition(position);
    }

    private void log(String message) {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, message);
        }
    }

    private void autoPlay(int position) {
        float currentProgress = animation.getCurrentProgress();
        float autoPlayEndProgress = progressCalculator.getAutoPlayEndProgressForPosition(position);
        SpritzStepWithOffset currentStep = spritzStepsWithOffset.get(position);

        animationRunner.cancelCurrentAnimations();
        animationRunner.autoPlay(currentProgress, autoPlayEndProgress, currentStep);
    }

}
