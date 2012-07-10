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

import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles the tab navigation mode.
 */
class TabDisplay {

    /**
     * Should be used to append a tab at the end of the list.
     *
     * @see #add(TabImpl, int, boolean)
     */
    public static final int LAST_POSITION = -2;

    private static final TabImpl NONE = null;

    private final Context mContext;
    private final TabAdapter mAdapter;

    private TabListView mList;
    private boolean mExpanded;

    TabDisplay(Context context, ViewGroup parent, TypedArray attributes) {
        mContext = context;
        mAdapter = new TabAdapter(context);
        createView(parent);
    }

    private void createView(ViewGroup parent) {
        int resource = R.layout.leftnav_bar_tabs;
        mList = (TabListView) LayoutInflater.from(mContext).inflate(resource, parent, false);
        mList.setAdapter(mAdapter);
        mList.setItemsCanFocus(true);
        mList.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
    }

    View getView() {
        return mList;
    }

    TabDisplay setVisible(boolean visible) {
        mList.setVisibility(visible ? View.VISIBLE : View.GONE);
        mAdapter.setSelectionActive(visible);
        return this;
    }

    TabDisplay setExpanded(boolean expanded) {
        mExpanded = expanded;
        mAdapter.refresh();
        return this;
    }

    /**
     * @see #LAST_POSITION
     */
    void add(TabImpl tab, int position, boolean setSelected) {
        if (position == LAST_POSITION) {
            position = mAdapter.getCount();
        }
        mAdapter.insert(tab, position);
        if (setSelected) {
            select(tab);
        }
    }

    TabImpl get(int position) {
        return mAdapter.getItem(position);
    }

    void select(TabImpl tab) {
        mAdapter.setSelected(tab);
    }

    TabImpl getSelected() {
        return mAdapter.getSelected();
    }

    private void onSelectionChanged(TabImpl oldSelection, TabImpl newSelection) {
        FragmentTransaction transaction = null;
        if (mContext instanceof Activity) {
            transaction = ((Activity) mContext).getFragmentManager()
                    .beginTransaction()
                    .disallowAddToBackStack();
        }
        if (oldSelection == newSelection) {
            if (newSelection != NONE && newSelection.getCallback() != null) {
                newSelection.getCallback().onTabReselected(newSelection, transaction);
            }
        } else {
            if (oldSelection != NONE && oldSelection.getCallback() != null) {
                oldSelection.getCallback().onTabUnselected(oldSelection, transaction);
            }
            if (newSelection != NONE && newSelection.getCallback() != null) {
                newSelection.getCallback().onTabSelected(newSelection, transaction);
            }
        }
        if (transaction != null && !transaction.isEmpty()) {
            transaction.commit();
        }

        mList.setHighlighted(mAdapter.getPosition(newSelection));
    }

    int getCount() {
        return mAdapter.getCount();
    }

    void removeAll() {
        mAdapter.clear();
    }

    void remove(TabImpl tab) {
        mAdapter.remove(tab);
    }

    void remove(int position) {
        remove(mAdapter.getItem(position));
    }

    /**
     * Removes the given view from its parent if it has one.
     */
    private static void detachFromParent(View view) {
        if (view == null) {
            return;
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
    }

    /**
     * Creates views for tabs, and handles the selection state.
     */
    private final class TabAdapter extends ArrayAdapter<TabImpl> {

        /**
         * Holds the views created thus far.
         */
        private final Map<TabImpl, TabFrame> mCachedViews;

        /**
         * The currently selected tab, or {@code TabDisplay#NONE} if none is selected.
         */
        private TabImpl mSelection;

        /**
         * {@code true} if selection changes should trigger a callback and any visual change;
         * otherwise the selection is just remembered.
         */
        private boolean mIsSelectionActive;

        /**
         * When selection is not active, this simply remembers which tab was selected.
         */
        private TabImpl mSavedSelection;

        TabAdapter(Context context) {
            super(context, 0);
            mCachedViews = new HashMap<TabImpl, TabFrame>();
            mSelection = NONE;
            mSavedSelection = NONE;
            mIsSelectionActive = true;
        }

        public void setSelectionActive(boolean active) {
            if (active == mIsSelectionActive) {
                return;
            }
            if (active) {
                // Restore the saved selection.
                mIsSelectionActive = true;
                setSelected(mSavedSelection);
                mSavedSelection = NONE;
            } else {
                // Save the selection and deselect the actual tab.
                mSavedSelection = mSelection;
                setSelected(NONE);
                mIsSelectionActive = false;
            }
        }

        public void setSelected(TabImpl tab) {
            if (!mIsSelectionActive) {
                // In this case, simply storing the selected tab.
                mSavedSelection = tab;
                return;
            }
            TabImpl oldSelection = mSelection;
            mSelection = tab;
            if (oldSelection != mSelection) {
                setSelectionState(oldSelection, false);
                setSelectionState(mSelection, true);
            }
            onSelectionChanged(oldSelection, mSelection);
        }

        public TabImpl getSelected() {
            return mIsSelectionActive ? mSelection : mSavedSelection;
        }

        private boolean isSelected(TabImpl tab) {
            return tab != NONE && tab == getSelected();
        }

        private void setSelectionState(TabImpl tab, boolean selected) {
            if (tab != NONE && mCachedViews.containsKey(tab)) {
                mCachedViews.get(tab).select(selected);
            }
        }

        public void refresh() {
            for (TabFrame frame : mCachedViews.values()) {
                frame.expand(mExpanded);
            }
        }

        @Override
        public int getItemViewType(int position) {
            // This ensures views are not recycled.
            return IGNORE_ITEM_VIEW_TYPE;
        }

        @Override
        public void insert(TabImpl tab, int position) {
            super.insert(tab, position);
            updatePositions(false /* normal order */);
        }

        @Override
        public void remove(TabImpl tab) {
            // Need to make sure custom views are properly removed from their parent.
            detachFromParent(tab.getCustomView());
            mCachedViews.remove(tab);
            super.remove(tab);
            updatePositions(false /* normal order */);
            if (isSelected(tab)) {
                setSelected(getCount() == 0 ? NONE : getItem(Math.max(0, tab.getPosition() - 1)));
            }
            tab.setPosition(Tab.INVALID_POSITION);
        }

        @Override
        public void clear() {
            updatePositions(true /* all invalid */);
            for (int i = 0; i < getCount(); ++i) {
                detachFromParent(getItem(i).getCustomView());
            }
            mCachedViews.clear();
            setSelected(NONE);
            super.clear();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final TabImpl tab = getItem(position);
            if (!mCachedViews.containsKey(tab)) {
                TabFrame frame = (TabFrame) LayoutInflater.from(getContext()).inflate(
                        R.layout.leftnav_bar_tab, parent, false);
                if (tab.hasCustomView()) {
                    frame.configureCustom(tab.getCustomView());
                } else {
                    frame.configureNormal(tab.getIcon(), tab.getText());
                }
                frame.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        setSelected(tab);
                    }
                });
                mCachedViews.put(tab, frame);
            }
            setSelectionState(tab, isSelected(tab));
            TabFrame result = mCachedViews.get(tab);
            result.expand(mExpanded);
            return result;
        }

        /**
         * Updates the tab objects so that they correctly report their position in the list.
         *
         * @param   allInvalid      {@code true} to set the position as "invalid" on all tabs
         */
        private void updatePositions(boolean allInvalid) {
            for (int i = 0; i < getCount(); ++i) {
                getItem(i).setPosition(allInvalid ? Tab.INVALID_POSITION : i);
            }
        }
    }
}
