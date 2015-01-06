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
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Manages the visual cues related to the options menu.
 */
class OptionsDisplay {

    private final Context mContext;
    private ViewGroup mView;
    private boolean mExpanded;

    OptionsDisplay(Context context, ViewGroup parent, TypedArray attributes) {
        mContext = context;
        createView(parent, attributes);
    }

    View getView() {
        return mView;
    }

    OptionsDisplay setVisible(boolean visible) {
        mView.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    boolean isVisible() {
        return mView.getVisibility() == View.VISIBLE;
    }

    OptionsDisplay setExpanded(boolean expanded) {
        mExpanded = expanded;
        refreshExpandedState();
        return this;
    }

    private void refreshExpandedState() {
        // Menu icon.
        setOptionExpanded(mView.getChildAt(1), mExpanded);
        // "Show always" options.
        ViewGroup optionsContainer = getOptionsContainer();
        for (int i = 0; i < optionsContainer.getChildCount(); ++i) {
            setOptionExpanded(optionsContainer.getChildAt(i), mExpanded);
        }
    }

    private void createView(ViewGroup parent, TypedArray attributes) {
        mView = (ViewGroup) LayoutInflater.from(mContext).inflate(
                R.layout.leftnav_bar_options, parent, false);
        View menuOption = mView.findViewById(R.id.menu);

        configureOption(menuOption,
                mContext.getResources().getString(R.string.lib_leftnav_bar_option_label),
                true);
        menuOption.setClickable(true);
        menuOption.setFocusable(true);
        menuOption.setOnClickListener(new View.OnClickListener() {
           public void onClick(View view) {
                if (mContext instanceof Activity) {
                    ((Activity) mContext).openOptionsMenu();
                }
            }
        });
        setDuplicateParentState(getOptionIcon(menuOption));
        setDuplicateParentState(getOptionTitle(menuOption));
    }

    /**
     * Forces a view to duplicate its parent state, working around a bug whereby the attribute only
     * works if set before the view is added to its parent.
     */
    private void setDuplicateParentState(View view) {
        view.setDuplicateParentStateEnabled(true);
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent == null) {
            return;
        }
        int index = parent.indexOfChild(view);
        parent.removeViewAt(index);
        parent.addView(view, index);
    }

    private View configureOption(View option, CharSequence title, boolean active) {
        ImageView iconView = getOptionIcon(option);
        iconView.setEnabled(active);
        getOptionTitle(option).setText(title);
        return option;
    }

    private static void setOptionExpanded(View option, boolean expanded) {
        getOptionTitle(option).setVisibility(expanded ? View.VISIBLE : View.GONE);
    }

    private static ImageView getOptionIcon(View option) {
        return (ImageView) option.findViewById(R.id.icon);
    }

    private static TextView getOptionTitle(View option) {
        return (TextView) option.findViewById(R.id.title);
    }

    private ViewGroup getOptionsContainer() {
        return (ViewGroup) mView.findViewById(R.id.shown_options);
    }
}
