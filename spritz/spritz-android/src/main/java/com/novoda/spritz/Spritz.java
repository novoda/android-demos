package com.novoda.spritz;

import android.support.v4.view.ViewPager;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.List;

public class Spritz {

    private final List<SpritzStepWithOffset> spritzStepsWithOffset;
    private final SpritzCalculator spritzCalculator;
    private final SpritzAnimation spritzAnimation;
    private final SpritzAnimator spritzAnimator;
    private final SpritzPager spritzPager;

    private SpritzOnPageChangeListener spritzOnPageChangeListener;

    public static Builder with(LottieAnimationView lottieAnimationView) {
        return new Builder(lottieAnimationView);
    }

    private Spritz(List<SpritzStepWithOffset> spritzStepsWithOffset,
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

    public void attachTo(ViewPager viewPager) {
        spritzPager.setViewPager(viewPager);

        this.spritzOnPageChangeListener = new SpritzOnPageChangeListener(
                spritzStepsWithOffset,
                spritzCalculator,
                spritzAnimation,
                spritzAnimator,
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
                    new SpritzCalculator(spritzStepsWithOffset, calculateTotalAnimationDuration()),
                    new SpritzAnimation(lottieAnimationView),
                    new SpritzAnimator(lottieAnimationView),
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
