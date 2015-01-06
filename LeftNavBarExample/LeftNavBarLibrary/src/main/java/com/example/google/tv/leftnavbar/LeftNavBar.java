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

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.widget.SpinnerAdapter;

/**
 * TV-specific implementation of the ActionBar API, using the title bar and a
 * left navigation panel.
 */
public class LeftNavBar extends ActionBar {

    /**
     * Always show an expanded version of the Left Navigation bar.
     *
     * @hide
     * @see #setDisplayOptions(int)
     * @see #setDisplayOptions(int, int)
     */
    public static final int DISPLAY_ALWAYS_EXPANDED = 0x20;

    /**
     * Use the app logo when the Left Navigation bar is expanded.
     *
     * @hide
     * @see #setDisplayOptions(int)
     * @see #setDisplayOptions(int, int)
     */
    public static final int DISPLAY_USE_LOGO_WHEN_EXPANDED = 0x80;

    /**
     * Show an indeterminate progress indicator.
     *
     * @hide
     * @see #setDisplayOptions(int)
     * @see #setDisplayOptions(int, int)
     */
    public static final int DISPLAY_SHOW_INDETERMINATE_PROGRESS = 0x100;

    /**
     * Display option for the Left Navigation bar to automatically expand when
     * certain ev ents happen. This includes gaining focus but could also
     * include other events, such as mouse hover.
     *
     * @see #setDisplayOptions(int)
     * @see #setDisplayOptions(int, int)
     */
    public static final int DISPLAY_AUTO_EXPAND = 0x40;

    /**
     * Display options applied by default.
     */
    public static final int DEFAULT_DISPLAY_OPTIONS = ActionBar.DISPLAY_SHOW_HOME
            | ActionBar.DISPLAY_USE_LOGO | ActionBar.DISPLAY_SHOW_TITLE;

    private Context mContext;

    private boolean mIsOverlay;

    private TitleBarView mTitleBar;

    private LeftNavView mLeftNav;

    private View mContent;

    public LeftNavBar(Activity activity) {
        initialize(activity.getWindow(), activity);
    }

    public LeftNavBar(Dialog dialog) {
        initialize(dialog.getWindow(), dialog.getContext());
    }

    private void initialize(Window window, Context context) {
        View decor = window.getDecorView();
        ViewGroup group = (ViewGroup) window.getDecorView();
        LayoutInflater inflater = (LayoutInflater) decor.getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.lib_title_container, group, true);
        inflater.inflate(R.layout.lib_left_nav, group, true);
        mContext = decor.getContext();
        mIsOverlay = window.hasFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        mTitleBar = (TitleBarView) decor.findViewById(R.id.title_container);

        mLeftNav = (LeftNavView) decor.findViewById(R.id.left_nav);

        mContent = group.getChildAt(0);

        if (mTitleBar == null || mLeftNav == null) {
            throw new IllegalStateException(
                    getClass().getSimpleName() + ": incompatible window decor!");
        }

        setDisplayOptions(DEFAULT_DISPLAY_OPTIONS);
        showOptionsMenu(true);
    }

    private void updateWindowLayout(boolean animated) {
        updateTitleBar(animated);
        setLeftMargin(mTitleBar, mLeftNav.getApparentWidth(true));
        if (!mIsOverlay) {
            setLeftMargin(mContent, mLeftNav.getApparentWidth(false));
            setTopMargin(mContent, mTitleBar.getApparentHeight());
        }
    }


    private void updateTitleBar(boolean animated) {
        int options = getDisplayOptions();
        boolean titleVisible = has(options, DISPLAY_SHOW_TITLE);
        boolean progressVisible = has(options, DISPLAY_SHOW_INDETERMINATE_PROGRESS);
        boolean horizontalProgressVisible = mTitleBar.isHorizontalProgressVisible();
        mTitleBar.setVisible(
                isShowing() && (titleVisible || progressVisible || horizontalProgressVisible),
                animated);
        mTitleBar.setProgressVisible(progressVisible);
    }

    private void setLeftMargin(View view, int margin) {
        MarginLayoutParams params = (MarginLayoutParams) view.getLayoutParams();
        params.leftMargin = margin;
        view.setLayoutParams(params);
    }

    private void setTopMargin(View view, int margin) {
        MarginLayoutParams params = (MarginLayoutParams) view.getLayoutParams();
        params.topMargin = margin;
        view.setLayoutParams(params);
    }

    // ----------------------------------------------------------------------------------------------
    // Visibility.

    @Override
    public void show() {
        setVisible(true);
    }

    @Override
    public void hide() {
        setVisible(false);
    }

    private void setVisible(boolean visible) {
        boolean shouldAnimate = mIsOverlay;
        if (mLeftNav.setVisible(visible, shouldAnimate)) {
            updateWindowLayout(shouldAnimate);
        }
    }

    @Override
    public boolean isShowing() {
        return mLeftNav.isVisible();
    }

    // ----------------------------------------------------------------------------------------------
    // Title / subtitle.

    @Override
    public void setTitle(CharSequence title) {
        mTitleBar.setTitle(title);
    }

    @Override
    public void setTitle(int resId) {
        setTitle(mContext.getString(resId));
    }

    @Override
    public CharSequence getTitle() {
        return mTitleBar.getTitle();
    }

    @Override
    public void setSubtitle(CharSequence subtitle) {
        mTitleBar.setSubtitle(subtitle);
    }

    @Override
    public void setSubtitle(int resId) {
        setSubtitle(mContext.getString(resId));
    }

    @Override
    public CharSequence getSubtitle() {
        return mTitleBar.getSubtitle();
    }

    // ----------------------------------------------------------------------------------------------
    // Tabs.

    @Override
    public Tab newTab() {
        return new TabImpl(mContext) {

            @Override
            public void select() {
                selectTab(this);
            }
        };
    }

    /**
     * Ensures the given tab is a valid object.
     */
    private TabImpl convertTab(Tab tab) {
        if (tab == null) {
            return null;
        }
        if (!(tab instanceof TabImpl)) {
            throw new IllegalArgumentException("Invalid tab object.");
        }
        return (TabImpl) tab;
    }

    @Override
    public void addTab(Tab tab) {
        addTab(tab, TabDisplay.LAST_POSITION);
    }

    @Override
    public void addTab(Tab tab, boolean setSelected) {
        addTab(tab, TabDisplay.LAST_POSITION, setSelected);
    }

    @Override
    public void addTab(Tab tab, int position) {
        addTab(tab, position, getTabCount() == 0);
    }

    @Override
    public void addTab(Tab tab, int position, boolean setSelected) {
        mLeftNav.getTabs().add(convertTab(tab), position, setSelected);
    }

    @Override
    public Tab getSelectedTab() {
        return mLeftNav.getTabs().getSelected();
    }

    @Override
    public Tab getTabAt(int index) {
        return mLeftNav.getTabs().get(index);
    }

    @Override
    public int getTabCount() {
        return mLeftNav.getTabs().getCount();
    }

    @Override
    public void removeAllTabs() {
        mLeftNav.getTabs().removeAll();
    }

    @Override
    public void removeTab(Tab tab) {
        mLeftNav.getTabs().remove(convertTab(tab));
    }

    @Override
    public void removeTabAt(int position) {
        mLeftNav.getTabs().remove(position);
    }

    @Override
    public void selectTab(Tab tab) {
        mLeftNav.getTabs().select(convertTab(tab));
    }

    // ----------------------------------------------------------------------------------------------
    // Navigation modes.

    @Override
    public int getNavigationItemCount() {
        switch (getNavigationMode()) {
            case NAVIGATION_MODE_TABS:
                return getTabCount();

            case NAVIGATION_MODE_LIST:
                return mLeftNav.getSpinner().getCount();

            default:
                throw new IllegalStateException(
                        "No count available for mode: " + getNavigationMode());
        }
    }

    @Override
    public int getNavigationMode() {
        return mLeftNav.getNavigationMode();
    }

    @Override
    public int getSelectedNavigationIndex() {
        switch (getNavigationMode()) {
            case NAVIGATION_MODE_TABS:
                Tab selected = getSelectedTab();
                return selected != null ? selected.getPosition() : -1;

            case NAVIGATION_MODE_LIST:
                return mLeftNav.getSpinner().getSelected();

            default:
                throw new IllegalStateException(
                        "No selection available for mode: " + getNavigationMode());
        }
    }

    @Override
    public void setListNavigationCallbacks(SpinnerAdapter adapter, OnNavigationListener callback) {
        mLeftNav.getSpinner().setContent(adapter, callback);
    }

    @Override
    public void setNavigationMode(int mode) {
        mLeftNav.setNavigationMode(mode);
    }

    @Override
    public void setSelectedNavigationItem(int position) {
        switch (getNavigationMode()) {
            case NAVIGATION_MODE_TABS:
                selectTab(getTabAt(position));
                break;

            case NAVIGATION_MODE_LIST:
                mLeftNav.getSpinner().setSelected(position);
                break;

            default:
                throw new IllegalStateException(
                        "Cannot set selection on mode: " + getNavigationMode());
        }
    }

    // ----------------------------------------------------------------------------------------------
    // Display options.

    @Override
    public int getDisplayOptions() {
        return mLeftNav.getDisplayOptions();
    }

    private static boolean has(int changes, int option) {
        return (changes & option) != 0;
    }

    @Override
    public void setDisplayOptions(int options) {
        int changes = mLeftNav.setDisplayOptions(options);
        if (has(changes, DISPLAY_ALWAYS_EXPANDED) || has(changes, DISPLAY_AUTO_EXPAND)
                || has(changes, DISPLAY_SHOW_TITLE)
                || has(changes, DISPLAY_SHOW_INDETERMINATE_PROGRESS)) {
            updateWindowLayout(false);
        }
    }

    @Override
    public void setDisplayOptions(int options, int mask) {
        int current = getDisplayOptions();
        int updated = ((options & mask) | (current & ~mask));
        setDisplayOptions(updated);
    }

    @Override
    public void setDisplayHomeAsUpEnabled(boolean showHomeAsUp) {
        setDisplayOptions(showHomeAsUp ? DISPLAY_HOME_AS_UP : 0, DISPLAY_HOME_AS_UP);
    }

    @Override
    public void setDisplayShowCustomEnabled(boolean showCustom) {
        setDisplayOptions(showCustom ? DISPLAY_SHOW_CUSTOM : 0, DISPLAY_SHOW_CUSTOM);
    }

    @Override
    public void setDisplayShowHomeEnabled(boolean showHome) {
        setDisplayOptions(showHome ? DISPLAY_SHOW_HOME : 0, DISPLAY_SHOW_HOME);
    }

    @Override
    public void setDisplayShowTitleEnabled(boolean showTitle) {
        setDisplayOptions(showTitle ? DISPLAY_SHOW_TITLE : 0, DISPLAY_SHOW_TITLE);
    }

    @Override
    public void setDisplayUseLogoEnabled(boolean useLogo) {
        setDisplayOptions(useLogo ? DISPLAY_USE_LOGO : 0, DISPLAY_USE_LOGO);
    }

    /**
     * Sets the horizontal progress indicator to the given value.
     */
    public void setShowHorizontalProgress(int value) {
        mTitleBar.setHorizontalProgress(value);
        updateWindowLayout(false);
    }

    // ----------------------------------------------------------------------------------------------
    // Custom view.

    @Override
    public View getCustomView() {
        return mLeftNav.getCustomView();
    }

    @Override
    public void setCustomView(View view) {
        mLeftNav.setCustomView(view);
    }

    @Override
    public void setCustomView(View view, LayoutParams layoutParams) {
        view.setLayoutParams(layoutParams);
        setCustomView(view);
    }

    @Override
    public void setCustomView(int resId) {
        setCustomView(LayoutInflater.from(mContext).inflate(resId, mLeftNav, false));
    }

    @Override
    public void setIcon(int resId) {
        // Ignored in this example
    }

    @Override
    public void setIcon(Drawable icon) {
        // Ignored in this example
    }

    @Override
    public void setLogo(int resId) {
        // Ignored in this example
    }

    @Override
    public void setLogo(Drawable logo) {
        // Ignored in this example
    }

    // ----------------------------------------------------------------------------------------------
    // Miscellaneous.

    @Override
    public void addOnMenuVisibilityListener(OnMenuVisibilityListener listener) {
        // No use for that, apps should use the regular options menu API.
    }

    @Override
    public void removeOnMenuVisibilityListener(OnMenuVisibilityListener listener) {
        // No use for that, apps should use the regular options menu API.
    }

    @Override
    public void setBackgroundDrawable(Drawable d) {
        mLeftNav.setBackgroundDrawable(d);
    }

    @Override
    public int getHeight() {
        // The height is completely irrelevant in our case.
        // Returning the left nav's width as this is what apps may want to
        // adjust to.
        return mLeftNav.getApparentWidth(true /*
                                               * ignore the fact that it may be
                                               * hidden
                                               */);
    }

    public void setExpandedSize(int s) {
        mLeftNav.setMinimumWidth(s);

    }

    // ----------------------------------------------------------------------------------------------
    // Copies of ActionBarImpl methods.
    // This is to ensure this implementation can be easily swapped in.

    public void setShowHideAnimationEnabled(boolean enabled) {
        mLeftNav.setAnimationsEnabled(enabled);
        mTitleBar.setAnimationsEnabled(enabled);
    }

    public void dispatchMenuVisibilityChanged(boolean visible) {
    }

    public ActionMode startActionMode(ActionMode.Callback callback) {
        return null;
    }

    // Extra methods
    public void showOptionsMenu(boolean show) {
        mLeftNav.showOptionsMenu(show);
    }

    public void setOnClickHomeListener(View.OnClickListener listener) {
        mLeftNav.setOnClickHomeListener(listener);
    }
}
