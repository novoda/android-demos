package com.novoda.spritz;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
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
    private final long defaultSwipeAnimationDuration;
    private final TimeInterpolator defaultSwipeForwardInterpolator;
    private final TimeInterpolator defaultSwipeBackwardsInterpolator;

    private ViewPager viewPager;
    private float currentPosition;
    private AnimatorSet animatorSet;
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
        this.defaultSwipeAnimationDuration = defaultSwipeAnimationDuration;
        this.defaultSwipeForwardInterpolator = defaultSwipeForwardInterpolator;
        this.defaultSwipeBackwardsInterpolator = defaultSwipeBackwardsInterpolator;
        this.animatorSet = new AnimatorSet();
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
                    animatorSet.cancel();
                    animatorSet = playAnimations(autoPlay(position));
                }
            }

        };

        viewPager.addOnPageChangeListener(onPageChangeListener);
    }

    private ValueAnimator autoPlay(int position) {
        float currentProgress = lottieAnimationView.getProgress();
        float autoPlayEndProgress = getAutoPlayEndProgressForPosition(position);
        SpritzStepWithOffset currentStep = spritzStepsWithOffset.get(position);

        ValueAnimator autoPlayAnimation = ValueAnimator
                .ofFloat(currentProgress, autoPlayEndProgress)
                .setDuration(currentStep.autoPlayDuration());
        autoPlayAnimation.addUpdateListener(defaultAnimatorUpdateListener());
        return autoPlayAnimation;
    }

    private boolean swipingForward(float newPosition) {
        return newPosition >= currentPosition;
    }

    public void startPendingAnimations() {
        animatorSet.cancel();
        int position = viewPager.getCurrentItem();
        animatorSet = playAnimations(autoPlay(position));
    }

    private void finishSwipeWithAnimation(int position) {
        animatorSet.cancel();

        Animator[] animationList;

        if (swipingForward(position)) {
            animationList = swipeForward(position);
        } else {
            animationList = swipeBackwards(position);
        }

        animatorSet = playAnimations(animationList);
    }

    private AnimatorSet playAnimations(Animator... animators) {
        animatorSet = new AnimatorSet();
        animatorSet.playSequentially(animators);
        animatorSet.start();

        return animatorSet;
    }

    private Animator[] swipeForward(int position) {
        float currentProgress = lottieAnimationView.getProgress();
        float previousSwipeEndProgress = getSwipeEndForPreviousPositionOrZero(position);
        SpritzStepWithOffset currentStep = spritzStepsWithOffset.get(position);

        ValueAnimator finishSwipeAnimation = ValueAnimator
                .ofFloat(currentProgress, previousSwipeEndProgress)
                .setDuration(defaultSwipeAnimationDuration);
        finishSwipeAnimation.setInterpolator(getSwipeForwardInterpolatorFor(currentStep));
        finishSwipeAnimation.addUpdateListener(defaultAnimatorUpdateListener());

        return new Animator[]{finishSwipeAnimation};
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

    private TimeInterpolator getSwipeForwardInterpolatorFor(SpritzStepWithOffset currentStep) {
        TimeInterpolator stepSwipeForwardInterpolator = currentStep.swipeForwardInterpolator();
        if (stepSwipeForwardInterpolator != null) {
            return stepSwipeForwardInterpolator;
        }
        return this.defaultSwipeForwardInterpolator;
    }

    private Animator[] swipeBackwards(int position) {
        SpritzStepWithOffset currentStep = spritzStepsWithOffset.get(position);
        float currentProgress = lottieAnimationView.getProgress();
        float autoPlayEndProgress = getAutoPlayEndProgressForPosition(position);

        ValueAnimator finishSwipeAnimation = ValueAnimator
                .ofFloat(currentProgress, autoPlayEndProgress)
                .setDuration(defaultSwipeAnimationDuration);
        finishSwipeAnimation.setInterpolator(getSwipeBackwardsInterpolatorFor(currentStep));
        finishSwipeAnimation.addUpdateListener(defaultAnimatorUpdateListener());

        return new Animator[]{finishSwipeAnimation};
    }

    private float getAutoPlayEndProgressForPosition(int position) {
        return ((float) spritzStepsWithOffset.get(position).autoPlayEnd()) / totalAnimationDuration;
    }

    private TimeInterpolator getSwipeBackwardsInterpolatorFor(SpritzStepWithOffset currentStep) {
        TimeInterpolator stepSwipeBackwardsInterpolator = currentStep.swipeBackwardsInterpolator();
        if (stepSwipeBackwardsInterpolator != null) {
            return stepSwipeBackwardsInterpolator;
        }
        return this.defaultSwipeBackwardsInterpolator;
    }

    private ValueAnimator.AnimatorUpdateListener defaultAnimatorUpdateListener() {
        return new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lottieAnimationView.setProgress((Float) animation.getAnimatedValue());
            }
        };
    }

    public void detachFrom(ViewPager viewPager) {
        viewPager.removeOnPageChangeListener(onPageChangeListener);
    }

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
