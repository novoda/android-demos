package com.novoda.spritz;

import com.airbnb.lottie.LottieAnimationView;

class SpritzAnimation {

    private final LottieAnimationView lottieAnimationView;

    SpritzAnimation(LottieAnimationView lottieAnimationView) {
        this.lottieAnimationView = lottieAnimationView;
    }

    float getCurrentProgress() {
        return lottieAnimationView.getProgress();
    }

    void setProgressImmediately(float progress) {
        lottieAnimationView.setProgress(progress);
    }

}
