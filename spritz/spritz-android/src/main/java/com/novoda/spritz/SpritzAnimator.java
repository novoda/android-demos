package com.novoda.spritz;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;

import com.airbnb.lottie.LottieAnimationView;

class SpritzAnimator {

    private final LottieAnimationView lottieAnimationView;
    private final TimeInterpolator defaultSwipeForwardInterpolator;
    private final long defaultSwipeAnimationDuration;
    private final TimeInterpolator defaultSwipeBackwardsInterpolator;

    private AnimatorSet animatorSet;

    SpritzAnimator(LottieAnimationView lottieAnimationView,
                   TimeInterpolator defaultSwipeForwardInterpolator,
                   long defaultSwipeAnimationDuration,
                   TimeInterpolator defaultSwipeBackwardsInterpolator) {

        this.lottieAnimationView = lottieAnimationView;
        this.defaultSwipeForwardInterpolator = defaultSwipeForwardInterpolator;
        this.defaultSwipeAnimationDuration = defaultSwipeAnimationDuration;
        this.defaultSwipeBackwardsInterpolator = defaultSwipeBackwardsInterpolator;
        this.animatorSet = new AnimatorSet();
    }

    void cancelCurrentAnimations() {
        animatorSet.cancel();
    }

    void finishSwipeForward(float from, float to, SpritzStepWithOffset spritzStep) {
        ValueAnimator finishSwipeAnimation = ValueAnimator
                .ofFloat(from, to)
                .setDuration(defaultSwipeAnimationDuration);
        finishSwipeAnimation.setInterpolator(getSwipeForwardInterpolatorFor(spritzStep));
        finishSwipeAnimation.addUpdateListener(defaultAnimatorUpdateListener());

        playAnimations(finishSwipeAnimation);
    }

    private TimeInterpolator getSwipeForwardInterpolatorFor(SpritzStepWithOffset currentStep) {
        TimeInterpolator stepSwipeForwardInterpolator = currentStep.swipeForwardInterpolator();
        if (stepSwipeForwardInterpolator != null) {
            return stepSwipeForwardInterpolator;
        }
        return this.defaultSwipeForwardInterpolator;
    }

    void finishSwipeBackwards(float from, float to, SpritzStepWithOffset spritzStep) {
        ValueAnimator finishSwipeAnimation = ValueAnimator
                .ofFloat(from, to)
                .setDuration(defaultSwipeAnimationDuration);
        finishSwipeAnimation.setInterpolator(getSwipeBackwardsInterpolatorFor(spritzStep));
        finishSwipeAnimation.addUpdateListener(defaultAnimatorUpdateListener());

        playAnimations(finishSwipeAnimation);
    }

    private TimeInterpolator getSwipeBackwardsInterpolatorFor(SpritzStepWithOffset currentStep) {
        TimeInterpolator stepSwipeBackwardsInterpolator = currentStep.swipeBackwardsInterpolator();
        if (stepSwipeBackwardsInterpolator != null) {
            return stepSwipeBackwardsInterpolator;
        }
        return this.defaultSwipeBackwardsInterpolator;
    }

    void autoPlay(float from, float to, SpritzStepWithOffset spritzStep) {
        ValueAnimator autoPlayAnimation = ValueAnimator
                .ofFloat(from, to)
                .setDuration(spritzStep.autoPlayDuration());
        autoPlayAnimation.addUpdateListener(defaultAnimatorUpdateListener());

        playAnimations(autoPlayAnimation);
    }

    private ValueAnimator.AnimatorUpdateListener defaultAnimatorUpdateListener() {
        return new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lottieAnimationView.setProgress((Float) animation.getAnimatedValue());
            }
        };
    }

    private void playAnimations(Animator... animators) {
        animatorSet = new AnimatorSet();
        animatorSet.playSequentially(animators);
        animatorSet.start();
    }

}
