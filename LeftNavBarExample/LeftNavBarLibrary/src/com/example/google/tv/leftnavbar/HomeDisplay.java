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

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Manages the "home" cue of the Left Navigation Bar.
 */
class HomeDisplay {

    private static final String TAG = "LeftNavBar-Home";

    enum Mode {
        ICON,
        LOGO,
        BOTH  // Icon when collapsed, logo when expanded.
    }
    private Mode mMode;

    private final Context mContext;
    private Drawable mLogo;
    private Drawable mIcon;
    private View mView;
    private boolean mExpanded;

    HomeDisplay(Context context, ViewGroup parent, TypedArray attributes) {
        mContext = context;
        mMode = Mode.ICON;
        ApplicationInfo appInfo = context.getApplicationInfo();
        PackageManager pm = context.getPackageManager();
        loadLogo(attributes, pm, appInfo);
        loadIcon(attributes, pm, appInfo);
        createView(parent, attributes);
    }

    private void loadLogo(TypedArray a, PackageManager pm, ApplicationInfo appInfo) {
        if (mContext instanceof Activity) {
            try {
                mLogo = pm.getActivityLogo(((Activity) mContext).getComponentName());
            } catch (NameNotFoundException e) {
                Log.e(TAG, "Failed to load app logo.", e);
            }
        }
        if (mLogo == null) {
            mLogo = appInfo.loadLogo(pm);
        }
    }

    private void loadIcon(TypedArray a, PackageManager pm, ApplicationInfo appInfo) {
        if (mContext instanceof Activity) {
            try {
                mIcon = pm.getActivityIcon(((Activity) mContext).getComponentName());
            } catch (NameNotFoundException e) {
                Log.e(TAG, "Failed to load app icon.", e);
            }
        }
        if (mIcon == null) {
            mIcon = appInfo.loadIcon(pm);
        }
    }

    public void setOnClickHomeListener(View.OnClickListener listener) {
        mView.setOnClickListener(listener);
    }

    private void createView(ViewGroup parent, TypedArray attributes) {
        mView = LayoutInflater.from(mContext).inflate(R.layout.leftnav_bar_home, parent, false);
    }

    private void updateImage() {
        boolean useIcon = mMode == Mode.ICON
                || mLogo == null
                || (mMode == Mode.BOTH && !mExpanded);
        ((ImageView) mView.findViewById(R.id.home)).setImageDrawable(useIcon ? mIcon : mLogo);
    }

    View getView() {
        return mView;
    }

    HomeDisplay setVisible(boolean visible) {
        mView.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    boolean isVisible() {
        return mView.getVisibility() == View.VISIBLE;
    }

    HomeDisplay setExpanded(boolean expanded) {
        mExpanded = expanded;
        updateImage();
        return this;
    }

    HomeDisplay setImageMode(Mode mode) {
        mMode = mode;
        updateImage();
        return this;
    }

    HomeDisplay setAsUp(boolean asUp) {
        mView.findViewById(R.id.up).setVisibility(asUp ? View.VISIBLE : View.GONE);
        return this;
    }
}
