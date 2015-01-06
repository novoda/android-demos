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
import android.app.ActionBar.Tab;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Basic implementation of the ActionBar.Tab type.
 * <p>
 * {@code select()} is left unimplemented so that the entity managing tabs can define the proper
 * selection flow.
 */
abstract class TabImpl extends Tab {

    private final Context mContext;

    private ActionBar.TabListener mCallback;
    private Object mTag;
    private Drawable mIcon;
    private CharSequence mText;
    private CharSequence mContentDescription;
    private int mPosition;
    private View mCustomView;

    public TabImpl(Context context) {
        mContext = context;
    }

    @Override
    public Object getTag() {
        return mTag;
    }

    @Override
    public Tab setTag(Object tag) {
        mTag = tag;
        return this;
    }

    @Override
    public Tab setTabListener(ActionBar.TabListener callback) {
        mCallback = callback;
        return this;
    }

    @Override
    public View getCustomView() {
        return mCustomView;
    }

    @Override
    public Tab setCustomView(View view) {
        mCustomView = view;
        return this;
    }

    @Override
    public Tab setCustomView(int layoutResId) {
        return setCustomView(LayoutInflater.from(mContext).inflate(layoutResId, null));
    }

    @Override
    public Drawable getIcon() {
        return mIcon;
    }

    @Override
    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    @Override
    public CharSequence getText() {
        return mText;
    }

    @Override
    public CharSequence getContentDescription() {
        return mContentDescription;
    }

    @Override
    public Tab setIcon(Drawable icon) {
        mIcon = icon;
        return this;
    }

    @Override
    public Tab setIcon(int resId) {
        return setIcon(mContext.getResources().getDrawable(resId));
    }

    @Override
    public Tab setText(int resId) {
        return setText(mContext.getResources().getText(resId));
    }

    @Override
    public Tab setText(CharSequence text) {
        mText = text;
        return this;
    }

    @Override
    public Tab setContentDescription(int resId) {
        return setContentDescription(mContext.getResources().getText(resId));
    }

    @Override
    public Tab setContentDescription(CharSequence contentDesc) {
        mContentDescription = contentDesc;
        return this;
    }

    //----------------------------------------------------------------------------------------------
    // Non-API.

    public ActionBar.TabListener getCallback() {
        return mCallback;
    }

    boolean hasCustomView() {
        return mCustomView != null;
    }

    @Override
    public String toString() {
        Object source = mTag != null ? mTag : mText;
        return "Tab:" + (source != null ? source.toString() : "<no id>");
    }
}
