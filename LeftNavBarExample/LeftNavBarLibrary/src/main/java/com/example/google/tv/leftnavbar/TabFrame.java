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
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Container for tabs, responsible for handling events and setting the correct state.
 */
public class TabFrame extends LinearLayout {

    private boolean mConfigured;
    private boolean mIsCustom;

    public TabFrame(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    @Override
    public void setSelected(boolean selected) {
        // No-op: the selected state is used to show that the tab is selected in the logical sense,
        // whereas ListView would attempt to use that state to show the tab as "highlighted" - we
        // use focus for that.
    }

    /**
     * Sets the selected state of this view.
     */
    public void select(boolean selected) {
        super.setSelected(selected);
    }

    /**
     * Expands the content of this tab.
     * <p>
     * For regular tabs, this will simply reveal the tab's title and mark the icon as activated.
     * For custom tabs, this will set their "activated" state.
     *
     * @see View#setActivated(boolean)
     */
    public void expand(boolean expanded) {
        if (!mIsCustom) {
            getTitle().setVisibility(expanded ? View.VISIBLE : View.GONE);
        }
        setActivated(expanded);
    }

    private ImageView getIcon() {
        return (ImageView) findViewById(R.id.icon);
    }

    private TextView getTitle() {
        return (TextView) findViewById(R.id.title);
    }

    public void configureNormal(Drawable icon, CharSequence text) {
        markConfigured(false);
        getIcon().setImageDrawable(icon);
        getTitle().setText(text);
    }

    public void configureCustom(View content) {
        markConfigured(true);

        // The focused state should be rendered by the content.
        setBackgroundDrawable(null);

        // Prevent the content from receiving events, but let it reflect the correct state.
        content.setFocusable(false);
        content.setFocusableInTouchMode(false);
        content.setClickable(false);
        content.setDuplicateParentStateEnabled(true);

        removeAllViews();
        addView(content);
    }

    private void markConfigured(boolean isCustom) {
        if (mConfigured) {
            throw new IllegalStateException("Frame already configured.");
        }
        mConfigured = true;
        mIsCustom = isCustom;
    }
}
