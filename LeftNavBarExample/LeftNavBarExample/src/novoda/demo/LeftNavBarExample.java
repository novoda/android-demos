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

package novoda.demo;

import android.app.*;
import android.content.Context;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.example.google.tv.leftnavbar.LeftNavBar;

public class LeftNavBarExample extends Activity {

    private static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);

        LeftNavBarWrapper leftNavBarWrapper = new LeftNavBarWrapper(this);
        LeftNavBar leftNavBar = leftNavBarWrapper.newInstance();

        setContentView(R.layout.main);

        leftNavBarWrapper.leftNarBarInit(leftNavBar);
    }

    public static class GridFragment extends Fragment {

	    @Override
	    public View onCreateView(
	            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        super.onCreateView(inflater, container, savedInstanceState);

            GridView gridView = (GridView) inflater.inflate(R.layout.gridview, null);
            gridView.setAdapter(new GridViewAdapter(mContext));
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> l, View v, int position, long id) {

                }
            });

            gridView.requestFocus();

            return gridView;
	    }
	}
}
