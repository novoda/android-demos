/*
 * Copyright (C) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.google.tv.leftnavbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;


/**
 * Left navigation panel hosting various controls such as tabs.
 */
public class LeftNavView extends LinearLayout {
	
    private final HomeDisplay mHome;
    private final TabDisplay mTabs;
    private final OptionsDisplay mOptions;
    private final SpinnerDisplay mSpinner;

    private final int mWidthCollapsed;
    private int mWidthExpanded;
    private final int mApparentWidthCollapsed;
    private final int mApparentWidthExpanded;


    public void setWidth(int a) {
         mWidthExpanded = a;
    }

    private final VisibilityController mVisibilityController;

    private final int mAnimationDuration;

    private int mDisplayOptions;
    private int mNavigationMode;
    private boolean mExpanded;
    private boolean mAnimationsEnabled;
    private ValueAnimator mWidthAnimator;

    public LeftNavView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mVisibilityController = new VisibilityController(this);
        LayoutInflater.from(context).inflate(R.layout.left_nav, this, true);
        setOrientation(VERTICAL);

        mHome = new HomeDisplay(context, this, null).setVisible(false);
        mTabs = new TabDisplay(context, this, null).setVisible(false);
        mOptions = new OptionsDisplay(context, this, null).setVisible(false);
        mSpinner = new SpinnerDisplay(context, this, null).setVisible(false);

        Resources res = context.getResources();
        mWidthCollapsed = res.getDimensionPixelSize(
                R.dimen.left_nav_collapsed_width);
        mWidthExpanded = res.getDimensionPixelSize(
               (R.dimen.left_nav_expanded_width));
        mApparentWidthCollapsed = res.getDimensionPixelSize(
                R.dimen.left_nav_collapsed_apparent_width);
        mApparentWidthExpanded = res.getDimensionPixelSize(
                R.dimen.left_nav_expanded_apparent_width);
        mAnimationDuration = res.getInteger(        		
        		android.R.integer.config_shortAnimTime);

        mNavigationMode = ActionBar.NAVIGATION_MODE_STANDARD;
        setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
  }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        // Add header / footer views.
        addView(mHome.getView(), 0);
        // Central section falls here.
        addView(mOptions.getView(), 2);

        // Add views to the central section.
        ViewGroup main = getMainSection();
        main.addView(mTabs.getView());
        main.addView(mSpinner.getView());
    }

    private ViewGroup getMainSection() {
        return (ViewGroup) findViewById(R.id.main);
    }

    public boolean setVisible(boolean visible, boolean animated) {
        return mVisibilityController.setVisible(visible, animated && mAnimationsEnabled);
    }

    public boolean isVisible() {
        return mVisibilityController.isVisible();
    }

    public void setOnClickHomeListener(View.OnClickListener listener) {
        mHome.setOnClickHomeListener(listener);
    }

    @Override
    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        if (direction == FOCUS_FORWARD) {
            // When setting the initial focus for the window, all views should be considered
            // focusable.
            super.addFocusables(views, direction, focusableMode);
            return;
        }
        if (direction != FOCUS_LEFT && !hasFocus()) {
            // We don't want to gain focus unless it's coming from the right or we already have
            // focus.
            return;
        }
        if (!hasFocus()) {
            // Try to focus a navigation mode first.
            int initialCount = views.size();
            switch (mNavigationMode) {
                case ActionBar.NAVIGATION_MODE_TABS:
                    mTabs.getView().addFocusables(views, direction, focusableMode);
                    break;

                case ActionBar.NAVIGATION_MODE_LIST:
                    mSpinner.getView().addFocusables(views, direction, focusableMode);
                    break;

                default:
                    if (hasCustomView()) {
                        getCustomView().addFocusables(views, direction, focusableMode);
                    }
                    break;
            }
            if (views.size() > initialCount) {
                // Some focusable elements were added.
                return;
            }
        }
        super.addFocusables(views, direction, focusableMode);
    }

    @Override
    public View focusSearch(View focused, int direction) {
        if (hasFocus() && direction != FOCUS_RIGHT) {
            // If we have focus, we should only relinquish focus if it is moving to the right.
            // Otherwise we restrict the focus search to our children.
            return FocusFinder.getInstance().findNextFocus(this, focused, direction);
        } else {
            return super.focusSearch(focused, direction);
        }
    }

    protected void onDescendantFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (has(mDisplayOptions, LeftNavBar.DISPLAY_AUTO_EXPAND)) {
            setExpanded(hasFocus);
        }
    }
    
    /*@Override
    protected void onDescendantFocusChanged(boolean hasFocus) {
        super.onDescendantFocusChanged(hasFocus);
        if (has(mDisplayOptions, ActionBar.DISPLAY_AUTO_EXPAND)) {
            setExpanded(hasFocus);
        }
    }*/

    public int getDisplayOptions() {
        return mDisplayOptions;
    }

    /**
     * Sets the display options and returns the options which have changed.
     */
    public int setDisplayOptions(int options) {
        int changes = options ^ mDisplayOptions;
        mDisplayOptions = options;
        if (has(changes, ActionBar.DISPLAY_SHOW_HOME)) {
            mHome.setVisible(has(options, ActionBar.DISPLAY_SHOW_HOME));
        }
        if (has(changes, ActionBar.DISPLAY_USE_LOGO) ||
                has(changes, LeftNavBar.DISPLAY_USE_LOGO_WHEN_EXPANDED)) {
            setHomeMode();
        }
        if (has(changes, ActionBar.DISPLAY_HOME_AS_UP)) {
            mHome.setAsUp(has(options, ActionBar.DISPLAY_HOME_AS_UP));
        }
        if (has(changes, ActionBar.DISPLAY_SHOW_CUSTOM)) {
            setCustomViewVisibility(has(mDisplayOptions, ActionBar.DISPLAY_SHOW_CUSTOM));
        }
        if (has(changes, LeftNavBar.DISPLAY_AUTO_EXPAND) ||
                has(changes, LeftNavBar.DISPLAY_ALWAYS_EXPANDED)) {
            setExpandedState();
        }
        return changes;
    }

    private void setHomeMode() {
        HomeDisplay.Mode mode;
        if (has(mDisplayOptions, LeftNavBar.DISPLAY_USE_LOGO_WHEN_EXPANDED)) {
            mode = HomeDisplay.Mode.BOTH;
        } else if (has(mDisplayOptions, ActionBar.DISPLAY_USE_LOGO)) {
            mode = HomeDisplay.Mode.LOGO;
        } else {
            mode = HomeDisplay.Mode.ICON;
        }
        mHome.setImageMode(mode);
    }

    private void setExpandedState() {
        if (has(mDisplayOptions, LeftNavBar.DISPLAY_AUTO_EXPAND)) {
            setExpanded(hasFocus(), false);
        } else {
            setExpanded(has(mDisplayOptions, LeftNavBar.DISPLAY_ALWAYS_EXPANDED), false);
        }
    }

    private void setExpanded(boolean expanded) {
        setExpanded(expanded, mAnimationsEnabled && isVisible());
    }

    private void setExpanded(final boolean expanded, boolean animated) {
        if (mExpanded == expanded) {
            return;
        }
        if (animated) {
            if (mWidthAnimator != null) {
                mWidthAnimator.cancel();
            }
            mWidthAnimator = ValueAnimator.ofInt(
                    getLayoutParams().width,  // Starting value.
                    expanded ? mWidthExpanded : mWidthCollapsed);
            mWidthAnimator.setDuration(mAnimationDuration);
            mWidthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    setViewWidth((Integer) animation.getAnimatedValue());
                }
            });
            mWidthAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animator) {
                    if (!expanded) {
                        setContentExpanded(false);
                    }
                }
                @Override
                public void onAnimationEnd(Animator animator) {
                    if (expanded) {
                        setContentExpanded(true);
                    }
                }
            });
            mWidthAnimator.start();
        } else {
            setViewWidth(expanded ? mWidthExpanded : mWidthCollapsed);
            setContentExpanded(expanded);
        }
        mExpanded = expanded;
    }

    private void setContentExpanded(boolean expanded) {
        mTabs.setExpanded(expanded);
        mOptions.setExpanded(expanded);
        mHome.setExpanded(expanded);
        mSpinner.setExpanded(expanded);
        if (hasCustomView()) {
            getCustomView().setActivated(expanded);
        }
    }

    private void setViewWidth(int width) {
        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = width;
        setLayoutParams(params);
    }

    /**
     * Returns the "steady state" width for the view, taking into account all shadowing effects.
     */
    public int getApparentWidth(boolean ignoreHiddenState) {
        if (!isVisible() && !ignoreHiddenState) {
            return 0;
        }
        boolean isCollapsed = has(mDisplayOptions, LeftNavBar.DISPLAY_AUTO_EXPAND) ||
                !has(mDisplayOptions, LeftNavBar.DISPLAY_ALWAYS_EXPANDED);
        return isCollapsed ? mApparentWidthCollapsed : mApparentWidthExpanded;
    }

    public void setAnimationsEnabled(boolean enabled) {
        mAnimationsEnabled = enabled;
    }

    private static boolean has(int changes, int option) {
        return (changes & option) != 0;
    }

    public TabDisplay getTabs() {
        return mTabs;
    }

    public SpinnerDisplay getSpinner() {
        return mSpinner;
    }

    public void setNavigationMode(int mode) {
        if (mNavigationMode == mode) {
            return;
        }
        setNavigationModeVisibility(mNavigationMode, false);
        setNavigationModeVisibility(mode, true);
        mNavigationMode = mode;
    }

    private void setNavigationModeVisibility(int mode, boolean visible) {
        switch (mode) {
            case ActionBar.NAVIGATION_MODE_TABS:
                mTabs.setVisible(visible);
                break;

            case ActionBar.NAVIGATION_MODE_LIST:
                mSpinner.setVisible(visible);
                break;

            default:
                break;
        }
    }

    public int getNavigationMode() {
        return mNavigationMode;
    }

    public void showOptionsMenu(Boolean show) {
        mOptions.setVisible(show);
    }

    public void setCustomView(View view) {
        ViewGroup main = getMainSection();
        CustomViewWrapper current = getCustomViewWrapper();
        if (current != null) {
            current.detach();
            main.removeView(current);
        }
        if (view != null) {
            view.setActivated(mExpanded);
            main.addView(new CustomViewWrapper(getContext(), view));
            setCustomViewVisibility(has(mDisplayOptions, ActionBar.DISPLAY_SHOW_CUSTOM));
        }
    }

    private boolean hasCustomView() {
        return getCustomViewWrapper() != null;
    }

    private boolean hasVisibleCustomView() {
        return hasCustomView() && getCustomViewWrapper().getVisibility() == VISIBLE;
    }

    public View getCustomView() {
        CustomViewWrapper wrapper = getCustomViewWrapper();
        return wrapper != null ? wrapper.getView() : null;
    }

    private CustomViewWrapper getCustomViewWrapper() {
        ViewGroup main = getMainSection();
        // The custom view comes after the tabs and the spinner.
        if (main.getChildCount() == 3) {
            return (CustomViewWrapper) main.getChildAt(2);
        }
        return null;
    }

    private void setCustomViewVisibility(boolean visible) {
        View current = getCustomViewWrapper();
        if (current != null) {
            current.setVisibility(visible ? VISIBLE : GONE);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (hasVisibleCustomView()) {
            getCustomViewWrapper().onPostMeasure(this);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (hasVisibleCustomView()) {
            getCustomViewWrapper().onPostLayout(this);
        }
    }

    /**
     * Wrapper around custom views to allow them to use the layout parameters defined in the
     * ActionBar class and still be displayed within this view group.
     */
    private static final class CustomViewWrapper extends ViewGroup {

        private final View mView;

        CustomViewWrapper(Context context, View view) {
            super(context);
            setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mView = view;
            if (!(view.getLayoutParams() instanceof ActionBar.LayoutParams)) {
                view.setLayoutParams(generateDefaultLayoutParams());
            }
            addView(view);
        }

        View getView() {
            return mView;
        }

        void detach() {
            removeView(mView);
        }

        /**
         * Locates the top bound of the space available for custom views, expressed in the
         * coordinate system of {@code parent}.
         */
        private int findTopOfAvailableSpace(LeftNavView parent) {
            //int top = parent.mPaddingTop;
        	int top = parent.getPaddingTop();
            if (parent.mHome.isVisible()) {
                top += parent.mHome.getView().getMeasuredHeight();
            }
            switch (parent.mNavigationMode) {
                case ActionBar.NAVIGATION_MODE_TABS:
                    top += parent.mTabs.getView().getMeasuredHeight();
                    break;

                case ActionBar.NAVIGATION_MODE_LIST:
                    top += parent.mSpinner.getView().getMeasuredHeight();
                    break;

                default:
                    break;
            }
            return top;
        }

        /**
         * Locates the bottom bound of the space available for custom views, expressed in the
         * coordinate system of {@code parent}.
         */
        private int findBottomOfAvailableSpace(LeftNavView parent) {
            //int bottom = parent.getMeasuredHeight() - parent.mPaddingBottom;
        	int bottom = parent.getMeasuredHeight() - parent.getPaddingBottom();
            if (parent.mOptions.isVisible()) {
                bottom -= parent.mOptions.getView().getMeasuredHeight();
            }
            return bottom;
        }

        private void checkDimensionsConsistency(int value, int expected) {
            if (value != expected) {
                throw new IllegalStateException("Inconsistent dimensions!");
            }
        }

        /**
         * Should be called after the rest of the left nav has been measured, so that custom views
         * can be sized according to the space left and the desired gravity.
         */
        void onPostMeasure(LeftNavView parent) {
            // Dimensions of the entire parent.
            int totalWidth = parent.getMeasuredWidth();
            int totalHeight = parent.getMeasuredHeight();
            // Coordinates of the top and bottom of the available space.
            int topOfAvailableSpace = findTopOfAvailableSpace(parent);
            int bottomOfAvailableSpace = findBottomOfAvailableSpace(parent);
            // Dimensions of the available space.
            int availableWidth = totalWidth - parent.getPaddingLeft() - parent.getPaddingRight();
            int availableHeight = bottomOfAvailableSpace - topOfAvailableSpace;
            // Space available in each half of the parent.
            // This is used when attempting to vertically center a custom view which has its height
            // as "match parent": in this case its size will be limited by the smallest of the two
            // spaces.
            int availableInTopHalf = totalHeight / 2 - topOfAvailableSpace;
            int availableInBottomHalf = bottomOfAvailableSpace - totalHeight / 2;

            // Sanity checks.
            if (getMeasuredWidth() != 0) {
                checkDimensionsConsistency(availableWidth, getMeasuredWidth());
            }
            if (getMeasuredHeight() != 0) {
                checkDimensionsConsistency(availableHeight, getMeasuredHeight());
            }

            ActionBar.LayoutParams params = (ActionBar.LayoutParams) mView.getLayoutParams();
            int horizontalMargin = params.leftMargin + params.rightMargin;
            int verticalMargin = params.topMargin + params.bottomMargin;

            int widthMode = params.width != LayoutParams.WRAP_CONTENT ?
                    MeasureSpec.EXACTLY : MeasureSpec.AT_MOST;
            int widthValue = params.width >= 0 ?
                    Math.min(params.width, availableWidth) : availableWidth;
            widthValue = Math.max(0, widthValue - horizontalMargin);

            int heightMode = params.height != LayoutParams.WRAP_CONTENT ?
                    MeasureSpec.EXACTLY : MeasureSpec.AT_MOST;
            int heightValue = params.height >= 0 ?
                    Math.min(params.height, availableHeight) : availableHeight;
            heightValue = Math.max(0, heightValue - verticalMargin);

            int vGravity = params.gravity & Gravity.VERTICAL_GRAVITY_MASK;
            if (vGravity == Gravity.CENTER_VERTICAL &&
                    params.height == ViewGroup.LayoutParams.MATCH_PARENT &&
                    availableInTopHalf > 0 &&
                    availableInBottomHalf > 0) {
                // Attempt to center if there's enough space to center.
                heightValue = Math.min(availableInTopHalf, availableInBottomHalf) * 2;
            }

            mView.measure(
                    MeasureSpec.makeMeasureSpec(widthValue, widthMode),
                    MeasureSpec.makeMeasureSpec(heightValue, heightMode));
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            // Nothing to do here, all the work is done in onPostLayout.
        }

        /**
         * Should be called as the last layout step to properly position the custom view.
         */
        void onPostLayout(LeftNavView parent) {
            int width = mView.getMeasuredWidth();
            int height = mView.getMeasuredHeight();
            ActionBar.LayoutParams params = (ActionBar.LayoutParams) mView.getLayoutParams();

            // Expressed within the coordinate system of the present view.
            int xPosition = 0;
            // Horizontal alignment is always performed within the available space.
           // int containerWidth = mRight - mLeft;
            int containerWidth = getRight() - getLeft();
            switch (params.gravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
                case Gravity.CENTER_HORIZONTAL:
                    xPosition = (containerWidth - width) / 2;
                    break;
                case Gravity.LEFT:
                    xPosition = params.leftMargin;
                    break;
                case Gravity.RIGHT:
                    xPosition = containerWidth - width - params.rightMargin;
                    break;
            }

            int vGravity = params.gravity & Gravity.VERTICAL_GRAVITY_MASK;
            // For "center vertical" the view gets centered within the parent, not within the
            // available space.
           /* int superContainerHeight =
                    parent.mBottom - parent.mTop - parent.mPaddingTop - parent.mPaddingBottom;*/
            int superContainerHeight =
                parent.getBottom() - parent.getTop() - parent.getPaddingTop() - parent.getPaddingBottom();
            int superCenteredTop = (superContainerHeight - height) / 2 + parent.getPaddingTop();
            // The coordinates of the wrapper in parent's coordinate system.
            int top = findTopOfAvailableSpace(parent);
            int bottom = findBottomOfAvailableSpace(parent);

            // Sanity checks.
            if (getBottom() - getTop() != 0) {
                checkDimensionsConsistency(bottom - top, getBottom() - getTop());
            }

            // See if we actually have room to truly center; if not push against top or bottom.
            if (vGravity == Gravity.CENTER_VERTICAL) {
                if (superCenteredTop < top) {
                    vGravity = Gravity.TOP;
                } else if (superCenteredTop + height > bottom) {
                    vGravity = Gravity.BOTTOM;
                }
            }

            // Expressed within the coordinate system of the present view.
            int yPosition = 0;
            int containerHeight = bottom - top;
            switch (vGravity) {
                case Gravity.CENTER_VERTICAL:
                    yPosition = superCenteredTop - top;
                    break;
                case Gravity.TOP:
                    yPosition = params.topMargin;
                    break;
                case Gravity.BOTTOM:
                    yPosition = containerHeight - height - params.bottomMargin;
                    break;
            }

            mView.layout(xPosition, yPosition, xPosition + width, yPosition + height);
        }

        @Override
        protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
            return new ActionBar.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }
}
