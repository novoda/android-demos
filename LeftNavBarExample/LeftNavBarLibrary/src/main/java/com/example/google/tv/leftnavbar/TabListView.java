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
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Extension of ListView which better handles focus.
 */
public class TabListView extends ListView {

    private int mHighlighted;
    private boolean mClearingFocus;

    public TabListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Sets the highlighted item which will receive focus whenever this list gains focus.
     */
    public void setHighlighted(int index) {
        mHighlighted = index;
    }

    @Override
    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        int selectedIndex = mHighlighted - getFirstVisiblePosition();
        if (!hasFocus() &&
                selectedIndex >= 0 && selectedIndex < getChildCount() &&
                direction == FOCUS_LEFT) {
            // This will force focus on the highlighted item.
            setSelection(mHighlighted);
            getChildAt(selectedIndex).addFocusables(views, direction, focusableMode);
        } else {
            super.addFocusables(views, direction, focusableMode);
        }
    }

    @Override
    public void clearFocus() {
        mClearingFocus = true;
        super.clearFocus();
        mClearingFocus = false;
    }

    /**
     * Whenever the geometry of the list changes, focus gets cleared. This is problematic when the
     * list gets expanded. Instead of letting the focus go away, it simply gets reset slightly
     * later.
     */
    @Override
    public void clearChildFocus(View child) {
        if (mClearingFocus) {
            super.clearChildFocus(child);
        } else {
            post(new Runnable() {
                public void run() {
                    setSelection(mHighlighted);
                }
            });
        }
    }

    /**
     * Draws an extra divider at the top and bottom of the list.
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        Drawable divider = getDivider();
        if (divider == null) {
            return;
        }
        
        Rect bounds = new Rect();
        bounds.left = getPaddingLeft();
        bounds.right = getRight() - getLeft() - getPaddingRight();
        // Top.
        bounds.top = getPaddingTop();
        bounds.bottom = getPaddingTop() + getDividerHeight();
        divider.setBounds(bounds);
        divider.draw(canvas);
        // Bottom.
        bounds.top = getBottom() - getTop() - getPaddingBottom() - getDividerHeight();
        bounds.bottom = getBottom() - getTop() - getPaddingBottom();
        divider.setBounds(bounds);
        divider.draw(canvas);
    }
}
