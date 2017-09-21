package com.novoda.spritz;

import android.animation.TimeInterpolator;

import java.util.ArrayList;
import java.util.List;

class SpritzPageWithOffset {

    private final long autoPlayDuration;
    private final long autoPlayEnd;
    private final long swipeEnd;
    private final TimeInterpolator swipeForwardInterpolator;
    private final TimeInterpolator swipeBackwardsInterpolator;

    private SpritzPageWithOffset(long autoPlayDuration,
                                 long autoPlayEnd,
                                 long swipeEnd,
                                 TimeInterpolator swipeForwardInterpolator,
                                 TimeInterpolator swipeBackwardsInterpolator) {

        this.autoPlayDuration = autoPlayDuration;
        this.autoPlayEnd = autoPlayEnd;
        this.swipeEnd = swipeEnd;
        this.swipeForwardInterpolator = swipeForwardInterpolator;
        this.swipeBackwardsInterpolator = swipeBackwardsInterpolator;
    }

    long autoPlayDuration() {
        return autoPlayDuration;
    }

    long autoPlayEnd() {
        return autoPlayEnd;
    }

    long swipeEnd() {
        return swipeEnd;
    }

    TimeInterpolator swipeForwardInterpolator() {
        return swipeForwardInterpolator;
    }

    TimeInterpolator swipeBackwardsInterpolator() {
        return swipeBackwardsInterpolator;
    }

    static List<SpritzPageWithOffset> fromSpritzPages(SpritzPage... spritzPages) {
        List<SpritzPageWithOffset> spritzPageWithOffsetList = new ArrayList<>(spritzPages.length);
        long totalAnimationTime = 0;

        for (SpritzPage spritzPage : spritzPages) {
            long autoPlayEnd = totalAnimationTime + spritzPage.autoPlayDuration();
            long swipeEnd = totalAnimationTime + spritzPage.autoPlayDuration() + spritzPage.swipeDuration();

            spritzPageWithOffsetList.add(
                    new SpritzPageWithOffset(
                            spritzPage.autoPlayDuration(),
                            autoPlayEnd,
                            swipeEnd,
                            spritzPage.swipeForwardInterpolator(),
                            spritzPage.swipeBackwardsInterpolator()
                    )
            );

            totalAnimationTime = swipeEnd;
        }

        return spritzPageWithOffsetList;
    }
}
