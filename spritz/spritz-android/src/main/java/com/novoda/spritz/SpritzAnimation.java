package com.novoda.spritz;

import com.airbnb.lottie.LottieAnimationView;

class SpritzAnimation {

    private final LottieAnimationView lottieAnimationView;

    SpritzAnimation(LottieAnimationView lottieAnimationView) {
        this.lottieAnimationView = lottieAnimationView;
    }

    float getProgress() {
        return lottieAnimationView.getProgress();
    }

    void setProgress(float progress) {
        lottieAnimationView.setProgress(progress);
    }

}
