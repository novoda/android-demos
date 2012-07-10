/*
 * Copyright (C) 2011 The Android Open Source Project
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

import java.util.HashMap;
import java.util.Map;

/** This service maintains a single instance of LeftNavBar per Activity **/
public class LeftNavBarService {

    private final Map<Integer, LeftNavBar> map;

    private static final LeftNavBarService service = new LeftNavBarService();

    private LeftNavBarService() {
        map = new HashMap<Integer, LeftNavBar>();
    }

    public static LeftNavBarService instance() {
        return service;
    }

    public LeftNavBar getLeftNavBar(Activity activity) {
        if (map.get(activity.hashCode()) == null) {
            LeftNavBar leftNavBar = new LeftNavBar(activity);           
            map.put(activity.hashCode(), leftNavBar);
        }
        return map.get(activity.hashCode());
    }
}
