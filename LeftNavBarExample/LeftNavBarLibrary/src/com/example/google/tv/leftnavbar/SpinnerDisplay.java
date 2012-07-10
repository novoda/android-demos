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

import android.app.ActionBar.OnNavigationListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

/**
 * A display for the "navigation list" feature.
 */
class SpinnerDisplay {

    private final Context mContext;
    private Spinner mView;
    private OnNavigationListener mListener;
    private boolean mExpanded;

    SpinnerDisplay(Context context, ViewGroup parent, TypedArray attributes) {
        mContext = context;
        createView(parent, attributes);
    }

    View getView() {
        return mView;
    }

    SpinnerDisplay setVisible(boolean visible) {
        mView.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    SpinnerDisplay setExpanded(boolean expanded) {
        mExpanded = expanded;
        refreshSelectedItem();
        return this;
    }

    void setContent(SpinnerAdapter adapter, OnNavigationListener listener) {
        mListener = listener;
        mView.setAdapter(adapter);
        refreshSelectedItem();
    }

    void setSelected(int position) {
        mView.setSelection(position);
    }

    int getSelected() {
        return mView.getSelectedItemPosition();
    }

    int getCount() {
        return mView.getCount();
    }

    private void refreshSelectedItem() {
        View selected = mView.getSelectedView();
        if (selected == null) {
            return;
        }
        selected.setActivated(mExpanded);
    }

    private void createView(ViewGroup parent, TypedArray attributes) {
        mView = (Spinner) LayoutInflater.from(mContext).inflate(
                R.layout.leftnav_bar_spinner, parent, false);
        mView.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mListener != null) {
                    mListener.onNavigationItemSelected(position, id);
                }
                refreshSelectedItem();
            }

            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
}
