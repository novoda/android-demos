package com.novoda.spritz;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.support.v4.view.ViewPager;
import android.view.animation.LinearInterpolator;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Spritz {

    private final LottieAnimationView lottieAnimationView;
    private final List<SpritzPageWithOffset> spritzPagesWithOffset;
    private final long totalAnimationDuration;
    private final long defaultSwipeAnimationDuration;
    private final TimeInterpolator defaultSwipeForwardInterpolator;
    private final TimeInterpolator defaultSwipeBackwardsInterpolator;

    private ViewPager viewPager;
    private float currentPosition;
    private AnimatorSet animatorSet;

    public static Builder with(LottieAnimationView lottieAnimationView) {
        return new Builder(lottieAnimationView);
    }

    private Spritz(LottieAnimationView lottieAnimationView,
                   List<SpritzPageWithOffset> spritzPagesWithOffset,
                   long totalAnimationDuration,
                   long defaultSwipeAnimationDuration,
                   TimeInterpolator defaultSwipeForwardInterpolator,
                   TimeInterpolator defaultSwipeBackwardsInterpolator) {

        this.lottieAnimationView = lottieAnimationView;
        this.spritzPagesWithOffset = spritzPagesWithOffset;
        this.totalAnimationDuration = totalAnimationDuration;
        this.defaultSwipeAnimationDuration = defaultSwipeAnimationDuration;
        this.defaultSwipeForwardInterpolator = defaultSwipeForwardInterpolator;
        this.defaultSwipeBackwardsInterpolator = defaultSwipeBackwardsInterpolator;
        this.animatorSet = new AnimatorSet();
    }

    private void attach(ViewPager viewPager) {
        this.viewPager = viewPager;

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

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
                autoPlayForPosition(position);
                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // do nothing
            }

        });

        currentPosition = viewPager.getCurrentItem();
    }

    private boolean swipingForward(float newPosition) {
        return newPosition >= currentPosition;
    }

    private float getSwipeEndProgressForPosition(int position) {
        return ((float) spritzPagesWithOffset.get(position).swipeEnd()) / totalAnimationDuration;
    }

    public void startPendingAnimations() {
        autoPlayForPosition(viewPager.getCurrentItem());
    }

    private void autoPlayForPosition(int position) {
        animatorSet.cancel();

        List<Animator> animationList;

        if (swipingForward(position)) {
            animationList = swipeForwardThenAutoPlay(position);
        } else {
            animationList = swipeBackwards(position);
        }

        animatorSet = new AnimatorSet();
        animatorSet.playSequentially(animationList);
        animatorSet.start();
    }

    private List<Animator> swipeForwardThenAutoPlay(int position) {
        float currentProgress = lottieAnimationView.getProgress();
        float previousSwipeEndProgress = getSwipeEndForPreviousPositionOrZero(position);
        float autoPlayEndProgress = getAutoPlayEndProgressForPosition(position);
        SpritzPageWithOffset currentPage = spritzPagesWithOffset.get(position);

        ValueAnimator finishSwipeAnimation = ValueAnimator
                .ofFloat(currentProgress, previousSwipeEndProgress)
                .setDuration(defaultSwipeAnimationDuration);
        finishSwipeAnimation.setInterpolator(getSwipeForwardInterpolatorFor(currentPage));
        finishSwipeAnimation.addUpdateListener(defaultAnimatorUpdateListener());

        ValueAnimator autoPlayAnimation = ValueAnimator
                .ofFloat(previousSwipeEndProgress, autoPlayEndProgress)
                .setDuration(currentPage.autoPlayDuration());
        autoPlayAnimation.addUpdateListener(defaultAnimatorUpdateListener());

        return Arrays.<Animator>asList(finishSwipeAnimation, autoPlayAnimation);
    }

    private float getSwipeEndForPreviousPositionOrZero(int position) {
        float swipeEndProgress = 0;
        if (position > 0) {
            swipeEndProgress = getSwipeEndProgressForPosition(position - 1);
        }
        return swipeEndProgress;
    }

    private TimeInterpolator getSwipeForwardInterpolatorFor(SpritzPageWithOffset currentPage) {
        TimeInterpolator pageSwipeForwardInterpolator = currentPage.swipeForwardInterpolator();
        if (pageSwipeForwardInterpolator != null) {
            return pageSwipeForwardInterpolator;
        }
        return this.defaultSwipeForwardInterpolator;
    }

    private List<Animator> swipeBackwards(int position) {
        SpritzPageWithOffset currentPage = spritzPagesWithOffset.get(position);
        float currentProgress = lottieAnimationView.getProgress();
        float autoPlayEndProgress = getAutoPlayEndProgressForPosition(position);

        ValueAnimator finishSwipeAnimation = ValueAnimator
                .ofFloat(currentProgress, autoPlayEndProgress)
                .setDuration(defaultSwipeAnimationDuration);
        finishSwipeAnimation.setInterpolator(getSwipeBackwardsInterpolatorFor(currentPage));
        finishSwipeAnimation.addUpdateListener(defaultAnimatorUpdateListener());

        return Collections.<Animator>singletonList(finishSwipeAnimation);
    }

    private float getAutoPlayEndProgressForPosition(int position) {
        return ((float) spritzPagesWithOffset.get(position).autoPlayEnd()) / totalAnimationDuration;
    }

    private TimeInterpolator getSwipeBackwardsInterpolatorFor(SpritzPageWithOffset currentPage) {
        TimeInterpolator pageSwipeBackwardsInterpolator = currentPage.swipeBackwardsInterpolator();
        if (pageSwipeBackwardsInterpolator != null) {
            return pageSwipeBackwardsInterpolator;
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

    public static class Builder {

        private static final long DEFAULT_SWIPE_ANIMATION_DURATION = TimeUnit.MILLISECONDS.toMillis(500);

        private final LottieAnimationView lottieAnimationView;

        private long defaultSwipeAnimationDuration = DEFAULT_SWIPE_ANIMATION_DURATION;
        private TimeInterpolator defaultSwipeForwardInterpolator = new LinearInterpolator();
        private TimeInterpolator defaultSwipeBackwardsInterpolator = new LinearInterpolator();
        private List<SpritzPageWithOffset> spritzPagesWithOffset;

        private Builder(LottieAnimationView lottieAnimationView) {
            this.lottieAnimationView = lottieAnimationView;
            spritzPagesWithOffset = new ArrayList<>();
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

        public Builder withPages(SpritzPage... spritzPages) {
            this.spritzPagesWithOffset = SpritzPageWithOffset.fromSpritzPages(spritzPages);
            return this;
        }

        public Spritz attachTo(ViewPager viewPager) {
            Spritz spritz = new Spritz(
                    lottieAnimationView,
                    spritzPagesWithOffset,
                    calculateTotalAnimationDuration(),
                    defaultSwipeAnimationDuration,
                    defaultSwipeForwardInterpolator,
                    defaultSwipeBackwardsInterpolator
            );
            spritz.attach(viewPager);
            return spritz;
        }

        private long calculateTotalAnimationDuration() {
            if (spritzPagesWithOffset.isEmpty()) {
                return 0;
            }

            int lastIndex = spritzPagesWithOffset.size() - 1;
            return spritzPagesWithOffset.get(lastIndex).swipeEnd();
        }

    }

}
