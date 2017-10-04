package com.novoda.spritz;

import android.support.v4.view.ViewPager;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.List;

public class Spritz {

    private final List<SpritzStepWithOffset> spritzStepsWithOffset;
    private final ProgressCalculator progressCalculator;
    private final Animation animation;
    private final AnimationRunner animationRunner;
    private final SpritzPager spritzPager;

    private SpritzOnPageChangeListener spritzOnPageChangeListener;

    public static Builder with(LottieAnimationView lottieAnimationView) {
        return new Builder(lottieAnimationView);
    }

    private Spritz(List<SpritzStepWithOffset> spritzStepsWithOffset,
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

    public void attachTo(ViewPager viewPager) {
        spritzPager.setViewPager(viewPager);

        this.spritzOnPageChangeListener = new SpritzOnPageChangeListener(
                spritzStepsWithOffset,
                progressCalculator,
                animation,
                animationRunner,
                spritzPager
        );

        viewPager.addOnPageChangeListener(spritzOnPageChangeListener);
    }

    public void startPendingAnimations() {
        spritzOnPageChangeListener.onPageScrollStateChanged(ViewPager.SCROLL_STATE_IDLE);
    }

    public void detachFrom(ViewPager viewPager) {
        viewPager.removeOnPageChangeListener(spritzOnPageChangeListener);
    }

    @SuppressWarnings("WeakerAccess")
    public static class Builder {

        private final LottieAnimationView lottieAnimationView;
        private List<SpritzStepWithOffset> spritzStepsWithOffset;

        private Builder(LottieAnimationView lottieAnimationView) {
            this.lottieAnimationView = lottieAnimationView;
            spritzStepsWithOffset = new ArrayList<>();
        }

        public Builder withSteps(SpritzStep... spritzSteps) {
            this.spritzStepsWithOffset = SpritzStepWithOffset.fromSpritzSteps(spritzSteps);
            return this;
        }

        public Spritz build() {
            return new Spritz(
                    spritzStepsWithOffset,
                    new ProgressCalculator(spritzStepsWithOffset, calculateTotalAnimationDuration()),
                    new Animation(lottieAnimationView),
                    new AnimationRunner(lottieAnimationView),
                    new SpritzPager()
            );
        }

        private long calculateTotalAnimationDuration() {
            if (spritzStepsWithOffset.isEmpty()) {
                return 0;
            }

            int lastIndex = spritzStepsWithOffset.size() - 1;
            return spritzStepsWithOffset.get(lastIndex).swipeEnd();
        }

    }

}
