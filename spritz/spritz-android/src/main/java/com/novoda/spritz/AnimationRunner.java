package com.novoda.spritz;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;

import com.airbnb.lottie.LottieAnimationView;

class AnimationRunner {

    private final LottieAnimationView lottieAnimationView;

    private AnimatorSet animatorSet;

    AnimationRunner(LottieAnimationView lottieAnimationView) {
        this.lottieAnimationView = lottieAnimationView;
        this.animatorSet = new AnimatorSet();
    }

    void cancelCurrentAnimations() {
        animatorSet.cancel();
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
