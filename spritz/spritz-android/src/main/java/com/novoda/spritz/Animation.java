package com.novoda.spritz;

import com.airbnb.lottie.LottieAnimationView;

class Animation {

    private final LottieAnimationView lottieAnimationView;

    Animation(LottieAnimationView lottieAnimationView) {
        this.lottieAnimationView = lottieAnimationView;
    }

    float getCurrentProgress() {
        return lottieAnimationView.getProgress();
    }

    void setProgressImmediately(float progress) {
        lottieAnimationView.setProgress(progress);
    }

}
