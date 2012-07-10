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
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Manages the visibility of a view, with optional animations.
 */
public class VisibilityController {

    private final View mView;
    private final int mAnimationDuration;
    private boolean mVisible;

    VisibilityController(View view) {
        mView = view;
		mAnimationDuration = 10000; // view.getContext().getResources()
				//.getInteger(android.R.integer.config_shortAnimTime);
        mVisible = view.getVisibility() == View.VISIBLE;
    }

    boolean isVisible() {
        return mVisible;
    }
    boolean setVisible(final boolean visible, boolean animated) {
        if (isVisible() == visible) {
            return false;
        }
        mVisible = visible;
        if (animated) {
            float toAlpha = visible ? 1.0f : 0.0f;
            ObjectAnimator mAnimator = ObjectAnimator.ofFloat(mView, "Alpha", 1-toAlpha, toAlpha);
            mAnimator.setDuration(mAnimationDuration).
                addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationStart(Animator animator) {
                    if (visible) {
                        setViewVisible(true);
                    }
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if (!visible) {
                        setViewVisible(false);
                    }
                }
            });
            mAnimator.start();
        } else {
            setViewVisible(visible);
        }
        return true;
    }

    private void setViewVisible(boolean visible) {
        mView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
}

