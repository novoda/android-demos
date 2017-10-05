package com.novoda.spritz;

import android.support.v4.view.ViewPager;

class SpritzPager {

    private ViewPager viewPager;
    private float cachedPosition;

    void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    int getCurrentPosition() {
        return viewPager.getCurrentItem();
    }

    float getCachedPosition() {
        return cachedPosition;
    }

    void setCachedPosition(float cachedPosition) {
        this.cachedPosition = cachedPosition;
    }

}
