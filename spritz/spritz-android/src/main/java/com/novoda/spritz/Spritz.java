package com.novoda.spritz;

import android.animation.TimeInterpolator;
import android.support.v4.view.ViewPager;
import android.view.animation.LinearInterpolator;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Spritz {

    private final LottieAnimationView lottieAnimationView;
    private final List<SpritzStepWithOffset> spritzStepsWithOffset;
    private final long totalAnimationDuration;

    private ViewPager viewPager;
    private float currentPosition;
    private SpritzAnimator spritzAnimator;
    private ViewPager.OnPageChangeListener onPageChangeListener;

    public static Builder with(LottieAnimationView lottieAnimationView) {
        return new Builder(lottieAnimationView);
    }

    private Spritz(LottieAnimationView lottieAnimationView,
                   List<SpritzStepWithOffset> spritzStepsWithOffset,
                   long totalAnimationDuration,
                   long defaultSwipeAnimationDuration,
                   TimeInterpolator defaultSwipeForwardInterpolator,
                   TimeInterpolator defaultSwipeBackwardsInterpolator) {

        this.lottieAnimationView = lottieAnimationView;
        this.spritzStepsWithOffset = spritzStepsWithOffset;
        this.totalAnimationDuration = totalAnimationDuration;
        this.spritzAnimator = new SpritzAnimator(
                lottieAnimationView,
                defaultSwipeForwardInterpolator,
                defaultSwipeAnimationDuration,
                defaultSwipeBackwardsInterpolator
        );
    }

    public void attachTo(final ViewPager viewPager) {
        this.viewPager = viewPager;

        this.currentPosition = viewPager.getCurrentItem();

        this.onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                float currentProgress = lottieAnimationView.getProgress();
                float finalProgress;
                float realOffset;

                if (position + positionOffset == currentPosition) {
                    return;
                }

                spritzAnimator.cancelCurrentAnimations();

                if (swipingForward(position + positionOffset)) {
                    finalProgress = getSwipeEndProgressForPosition(position);
                    realOffset = positionOffset;
                } else {
                    finalProgress = getAutoPlayEndProgressForPosition(position);
                    realOffset = 1 - positionOffset;
                }

                float progressToAnimate = finalProgress - currentProgress;
                float newProgress = currentProgress + (progressToAnimate * realOffset);

                lottieAnimationView.setProgress(newProgress);

                currentPosition = position + positionOffset;
            }

            @Override
            public void onPageSelected(final int position) {
                finishSwipeWithAnimation(position);
                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                int position = viewPager.getCurrentItem();
                if (state == ViewPager.SCROLL_STATE_IDLE && lottieAnimationView.getProgress() < getAutoPlayEndProgressForPosition(position)) {
                    autoPlay(position);
                }
            }

        };

        viewPager.addOnPageChangeListener(onPageChangeListener);
    }

    private void autoPlay(int position) {
        float currentProgress = lottieAnimationView.getProgress();
        float autoPlayEndProgress = getAutoPlayEndProgressForPosition(position);
        SpritzStepWithOffset currentStep = spritzStepsWithOffset.get(position);

        spritzAnimator.cancelCurrentAnimations();
        spritzAnimator.autoPlay(currentProgress, autoPlayEndProgress, currentStep);
    }

    private boolean swipingForward(float newPosition) {
        return newPosition >= currentPosition;
    }

    public void startPendingAnimations() {
        int position = viewPager.getCurrentItem();
        autoPlay(position);
    }

    private void finishSwipeWithAnimation(int position) {
        float from = lottieAnimationView.getProgress();

        if (swipingForward(position)) {
            float to = getSwipeEndForPreviousPositionOrZero(position);
            SpritzStepWithOffset currentStep = spritzStepsWithOffset.get(position);
            spritzAnimator.finishSwipeForward(from, to, currentStep);
        } else {
            float to = getAutoPlayEndProgressForPosition(position);
            SpritzStepWithOffset currentStep = spritzStepsWithOffset.get(position);
            spritzAnimator.finishSwipeBackwards(from, to, currentStep);
        }
    }

    private float getSwipeEndForPreviousPositionOrZero(int position) {
        float swipeEndProgress = 0;
        if (position > 0) {
            swipeEndProgress = getSwipeEndProgressForPosition(position - 1);
        }
        return swipeEndProgress;
    }

    private float getSwipeEndProgressForPosition(int position) {
        return ((float) spritzStepsWithOffset.get(position).swipeEnd()) / totalAnimationDuration;
    }

    private float getAutoPlayEndProgressForPosition(int position) {
        return ((float) spritzStepsWithOffset.get(position).autoPlayEnd()) / totalAnimationDuration;
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
                    lottieAnimationView,
                    spritzStepsWithOffset,
                    calculateTotalAnimationDuration(),
                    defaultSwipeAnimationDuration,
                    defaultSwipeForwardInterpolator,
                    defaultSwipeBackwardsInterpolator
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
