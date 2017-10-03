package com.novoda.spritz;

import android.support.v4.view.ViewPager;
import android.util.Log;

import java.util.List;
import java.util.Locale;

class SpritzOnPageChangeListener implements ViewPager.OnPageChangeListener, SpritzOnPageIdleListener {

    private final static String TAG = SpritzOnPageChangeListener.class.getName();

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
        if (position + positionOffset == spritzPager.getCurrentPosition()) {
            return;
        }

        if (position + positionOffset == spritzPager.getCachedPosition()) {
            return;
        }

        spritzAnimator.cancelCurrentAnimations();

        float initialProgress;
        float finalProgress;
        float realOffset;

        if (swipingForward(position + positionOffset)) {
            initialProgress = spritzCalculator.getAutoPlayEndProgressForPosition(position);
            finalProgress = spritzCalculator.getSwipeEndProgressForPosition(position);
            realOffset = positionOffset;
            log(String.format(Locale.ENGLISH, "Swiping > %d+%f", position, positionOffset));
        } else {
            initialProgress = spritzCalculator.getAutoPlayEndProgressForPosition(position + 1);
            finalProgress = spritzCalculator.getAutoPlayEndProgressForPosition(position);
            realOffset = 1 - positionOffset;
            log(String.format(Locale.ENGLISH, "Swiping < %d+%f", position, positionOffset));
        }

        float progressToAnimate = finalProgress - initialProgress;
        float newProgress = initialProgress + (progressToAnimate * realOffset);

        spritzAnimation.setProgressImmediately(newProgress);

        spritzPager.setCachedPosition(position + positionOffset);
    }

    @Override
    public void onPageSelected(final int position) {
        log(String.format(Locale.ENGLISH, "Page selected, finishing swipe to position %d", position));
        finishSwipeWithAnimation(position);
    }

    private void finishSwipeWithAnimation(int position) {
        float from = spritzAnimation.getCurrentProgress();

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
        float currentProgress = spritzAnimation.getCurrentProgress();
        if (state == ViewPager.SCROLL_STATE_IDLE
                && currentProgress < spritzCalculator.getAutoPlayEndProgressForPosition(position)) {
            onPageIdle(position);
        }
    }

    @Override
    public void onPageIdle(int position) {
        log(String.format(Locale.ENGLISH, "Page idle, autoplaying for position %d", position));
        autoPlay(position);
        spritzPager.setCachedPosition(position);
    }

    private void log(String message) {
        if (Log.isLoggable(TAG, Log.DEBUG) || BuildConfig.DEBUG) {
            Log.d(TAG, message);
        }
    }

    private void autoPlay(int position) {
        float currentProgress = spritzAnimation.getCurrentProgress();
        float autoPlayEndProgress = spritzCalculator.getAutoPlayEndProgressForPosition(position);
        SpritzStepWithOffset currentStep = spritzStepsWithOffset.get(position);

        spritzAnimator.cancelCurrentAnimations();
        spritzAnimator.autoPlay(currentProgress, autoPlayEndProgress, currentStep);
    }

}
