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

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Holds the various widgets of the title bar.
 */
public class TitleBarView extends RelativeLayout {

    private final VisibilityController mVisibilityController;

    private boolean mIsLegacy;
    private boolean mAnimationsEnabled;

    private TextView mTitle;
    private TextView mSubtitle;
    private ImageView mLeftIcon;
    private ImageView mRightIcon;
    private ProgressBar mCircularProgress;
    private ProgressBar mHorizontalProgress;

    private int mTitleResource;
    private int mSubtitleResource;
    Context mContext;

    public TitleBarView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        mContext=context;
        mVisibilityController = new VisibilityController(this);
        TypedArray a = context.obtainStyledAttributes(attrs, new int[] {
        		android.R.attr.windowTitleStyle,
                android.R.attr.defaultValue });
        mIsLegacy = a.getBoolean(a.getIndex(1 /* defaultValue */), false);
        if (mIsLegacy) {
            mTitleResource = a.getResourceId(a.getIndex(0 /* windowTitleStyle */), 0);
        } else {
            a.recycle();
            a = context.obtainStyledAttributes(null,
                    new int[] { android.R.attr.titleTextStyle,
                            android.R.attr.subtitleTextStyle },
                    android.R.attr.actionBarStyle,
                    0);
            mTitleResource = a.getResourceId(a.getIndex(0 /* titleTextStyle */), 0);
            mSubtitleResource = a.getResourceId(a.getIndex(1 /* subtitleTextStyle */), 0);
        }
        a.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() == 0) {
            // Set up the default content.
            LayoutInflater.from(mContext).inflate(R.layout.lib_title_bar, this, true);
        }
        mTitle = (TextView) findViewById(R.id.title);
        mSubtitle = (TextView) findViewById(R.id.subtitle);
        mLeftIcon = (ImageView) findViewById(R.id.left_icon);
        mRightIcon = (ImageView) findViewById(R.id.right_icon);
        mCircularProgress = (ProgressBar) findViewById(R.id.progress_circular);
        if (mCircularProgress != null) {
            mCircularProgress.setIndeterminate(true);  // Cannot be done in XML...
        }
        mHorizontalProgress = (ProgressBar) findViewById(R.id.progress_horizontal);
        if (mIsLegacy) {
            setTextStyle(mTitle, mTitleResource);
            disableSubtitle();
        } else {
            setTextStyle(mTitle, mTitleResource);
            setTextStyle(mSubtitle, mSubtitleResource);
            disableLeftIcon();
            disableRightIcon();
        }
    }

    private void setTextStyle(TextView view, int style) {
        if (style != 0) {
            view.setTextAppearance(getContext(), style);
        }
    }

    public void setTitle(CharSequence text) {
        mTitle.setText(text);
    }

    public void setTitleColor(int color) {
        mTitle.setTextColor(color);
    }

    public void setLeftIcon(Drawable drawable, int alpha) {
        setIcon(mLeftIcon, drawable, alpha);
    }

    public void setRightIcon(Drawable drawable, int alpha) {
        setIcon(mRightIcon, drawable, alpha);
    }

    private void setIcon(ImageView view, Drawable drawable, int alpha) {
        if (view == null) {
            return;
        }
        if (drawable != null) {
            drawable.setAlpha(alpha);
            view.setImageDrawable(drawable);
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    public void setHorizontalProgress(int value) {
        if (mHorizontalProgress == null) {
            return;
        }
        switch (value) {
            case Window.PROGRESS_VISIBILITY_ON:
                mHorizontalProgress.setVisibility(View.VISIBLE);
                break;

            case Window.PROGRESS_VISIBILITY_OFF:
                mHorizontalProgress.setVisibility(View.GONE);
                break;

            case Window.PROGRESS_INDETERMINATE_ON:
                mHorizontalProgress.setIndeterminate(true);
                break;

            case Window.PROGRESS_INDETERMINATE_OFF:
                mHorizontalProgress.setIndeterminate(false);
                break;

            default:
                if (Window.PROGRESS_START <= value && value <= Window.PROGRESS_END) {
                    mHorizontalProgress.setProgress(value - Window.PROGRESS_START);
                } else if (Window.PROGRESS_SECONDARY_START <= value &&
                        value <= Window.PROGRESS_SECONDARY_END) {
                    mHorizontalProgress.setSecondaryProgress(
                            value - Window.PROGRESS_SECONDARY_START);
                }
                break;
        }
    }

    public boolean isHorizontalProgressVisible() {
        return mHorizontalProgress != null && mHorizontalProgress.getVisibility() == VISIBLE;
    }

    public void setCircularProgress(int value) {
        if (mCircularProgress == null) {
            return;
        }
        switch (value) {
            case Window.PROGRESS_VISIBILITY_ON:
                mCircularProgress.setVisibility(View.VISIBLE);
                break;

            case Window.PROGRESS_VISIBILITY_OFF:
                mCircularProgress.setVisibility(View.GONE);
                break;

            default:
                break;
        }
    }

    public void disableLeftIcon() {
        removeFromParent(mLeftIcon);
        mLeftIcon = null;
    }

    public void disableRightIcon() {
        removeFromParent(mRightIcon);
        mRightIcon = null;
    }

    public void disableHorizontalProgress() {
        removeFromParent(mHorizontalProgress);
        mHorizontalProgress = null;
    }

    public void disableCircularProgress() {
        removeFromParent(mCircularProgress);
        mCircularProgress = null;
    }

    private void disableSubtitle() {
        removeFromParent(mSubtitle);
        mSubtitle = null;
    }

    private static void removeFromParent(View view) {
        if (view == null) {
            return;
        }
        ViewParent parent = view.getParent();
        if (parent != null) {
            ((ViewGroup) parent).removeView(view);
        }
    }

    public CharSequence getTitle() {
        return mTitle.getText();
    }

    public void setSubtitle(CharSequence text) {
        mSubtitle.setText(text);
        mSubtitle.setVisibility(TextUtils.isEmpty(text) ? GONE : VISIBLE);
    }

    public CharSequence getSubtitle() {
        return mSubtitle.getText();
    }

    public void setAnimationsEnabled(boolean enabled) {
        mAnimationsEnabled = enabled;
    }

    public void setVisible(boolean visible, boolean animated) {
        mVisibilityController.setVisible(visible, animated && mAnimationsEnabled);
    }

    public boolean isVisible() {
        return mVisibilityController.isVisible();
    }

    public int getApparentHeight() {
        return isVisible() ?
                getContext().getResources().getDimensionPixelSize(R.dimen.title_bar_apparent_height) :
                0;
    }

    public void setProgressVisible(boolean visible) {
        setCircularProgress(visible ?
                Window.PROGRESS_VISIBILITY_ON :
                Window.PROGRESS_VISIBILITY_OFF);
    }
}
