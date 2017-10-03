package com.novoda.spritz;

import android.animation.TimeInterpolator;
import android.support.v4.view.ViewPager;
import android.view.animation.LinearInterpolator;

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
    private ViewPager.OnPageChangeListener onPageChangeListener;

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

        this.onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                float currentProgress = spritzAnimation.getProgress();
                float finalProgress;
                float realOffset;

                if (position + positionOffset == spritzPager.getCurrentPosition()) {
                    return;
                }

                spritzAnimator.cancelCurrentAnimations();

                if (swipingForward(position + positionOffset)) {
                    finalProgress = spritzCalculator.getSwipeEndProgressForPosition(position);
                    realOffset = positionOffset;
                } else {
                    finalProgress = spritzCalculator.getAutoPlayEndProgressForPosition(position);
                    realOffset = 1 - positionOffset;
                }

                float progressToAnimate = finalProgress - currentProgress;
                float newProgress = currentProgress + (progressToAnimate * realOffset);

                spritzAnimation.setProgress(newProgress);

                spritzPager.setCachedPosition(position + positionOffset);
            }

            @Override
            public void onPageSelected(final int position) {
                finishSwipeWithAnimation(position);
                spritzPager.setCachedPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                int position = spritzPager.getCurrentPosition();
                if (state == ViewPager.SCROLL_STATE_IDLE
                        && spritzAnimation.getProgress() < spritzCalculator.getAutoPlayEndProgressForPosition(position)) {
                    autoPlay(position);
                }
            }

        };

        viewPager.addOnPageChangeListener(onPageChangeListener);
    }

    private void autoPlay(int position) {
        float currentProgress = spritzAnimation.getProgress();
        float autoPlayEndProgress = spritzCalculator.getAutoPlayEndProgressForPosition(position);
        SpritzStepWithOffset currentStep = spritzStepsWithOffset.get(position);

        spritzAnimator.cancelCurrentAnimations();
        spritzAnimator.autoPlay(currentProgress, autoPlayEndProgress, currentStep);
    }

    private boolean swipingForward(float newPosition) {
        return newPosition >= spritzPager.getCachedPosition();
    }

    public void startPendingAnimations() {
        int position = spritzPager.getCurrentPosition();
        autoPlay(position);
    }

    private void finishSwipeWithAnimation(int position) {
        float from = spritzAnimation.getProgress();

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

    public void detachFrom(ViewPager viewPager) {
        viewPager.removeOnPageChangeListener(onPageChangeListener);
    }

    @SuppressWarnings("WeakerAccess")
    public static class Builder {

        private static final long DEFAULT_SWIPE_ANIMATION_DURATION = TimeUnit.MILLISECONDS.toMillis(250);

        private final LottieAnimationView lottieAnimationView;

        private long defaultSwipeAnimationDuration = DEFAULT_SWIPE_ANIMATION_DURATION;
        private TimeInterpolator defaultSwipeForwardInterpolator = new LinearInterpolator();
        private TimeInterpolator defaultSwipeBackwardsInterpolator = new LinearInterpolator();
        private List<SpritzStepWithOffset> spritzStepsWithOffset;

        private Builder(LottieAnimationView lottieAnimationView) {
            this.lottieAnimationView = lottieAnimationView;
            spritzStepsWithOffset = new ArrayList<>();
        }

        public Builder withDefaultSwipeAnimationDuration(long defaultSwipeAnimationDuration, TimeUnit timeUnit) {
            this.defaultSwipeAnimationDuration = timeUnit.toMillis(defaultSwipeAnimationDuration);
            return this;
        }

        public Builder withDefaultSwipeForwardInterpolator(TimeInterpolator swipeForwardInterpolator) {
            this.defaultSwipeForwardInterpolator = swipeForwardInterpolator;
            return this;
        }

        public Builder withDefaultSwipeBackwardsInterpolator(TimeInterpolator swipeBackwardsInterpolator) {
            this.defaultSwipeBackwardsInterpolator = swipeBackwardsInterpolator;
            return this;
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
                            lottieAnimationView,
                            defaultSwipeForwardInterpolator,
                            defaultSwipeAnimationDuration,
                            defaultSwipeBackwardsInterpolator
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
