package com.novoda.spritz;

import android.support.v4.view.ViewPager;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Spritz {

    private final List<SpritzStepWithOffset> spritzStepsWithOffset;
    private final SpritzCalculator spritzCalculator;
    private final SpritzAnimation spritzAnimation;
    private final SpritzAnimator spritzAnimator;

    private SpritzPager spritzPager;
    private SpritzOnPageChangeListener spritzOnPageChangeListener;

    public static Builder with(LottieAnimationView lottieAnimationView) {
        return new Builder(lottieAnimationView);
    }

    private Spritz(List<SpritzStepWithOffset> spritzStepsWithOffset,
                   SpritzCalculator spritzCalculator,
                   SpritzAnimation spritzAnimation,
                   SpritzAnimator spritzAnimator) {

        this.spritzStepsWithOffset = spritzStepsWithOffset;
        this.spritzCalculator = spritzCalculator;
        this.spritzAnimation = spritzAnimation;
        this.spritzAnimator = spritzAnimator;
    }

    public void attachTo(ViewPager viewPager) {
        this.spritzPager = new SpritzPager(viewPager);

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
        int position = spritzPager.getCurrentPosition();
        spritzOnPageChangeListener.onPageIdle(position);
    }

    public void detachFrom(ViewPager viewPager) {
        viewPager.removeOnPageChangeListener(spritzOnPageChangeListener);
    }

    @SuppressWarnings("WeakerAccess")
    public static class Builder {

        private static final long DEFAULT_SWIPE_ANIMATION_DURATION = TimeUnit.MILLISECONDS.toMillis(250);

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
                    new SpritzAnimator(
                            lottieAnimationView
                    )
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
