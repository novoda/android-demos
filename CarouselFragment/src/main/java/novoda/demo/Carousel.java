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

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.TextView;

public class Carousel extends Activity {

    private static int count;

    private int selectedCarouselItemIndex;

	private TextView featuredId;

    private static Context mContext;

    private static CarouselFragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_carousel);
        featuredId = (TextView)findViewById(R.id.featured);
    }

    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    switch (keyCode) {
	    case KeyEvent.KEYCODE_MEDIA_REWIND:
	    case KeyEvent.KEYCODE_DPAD_LEFT: {
	        fragment = CarouselFragment.newInstance(selectedCarouselItemIndex - 1);
	        FragmentTransaction ft = getFragmentManager().beginTransaction();
	        ft.replace(R.id.carousel, fragment);
	        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	        ft.commit();
	
	        break;
	    }
	    case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
	    case KeyEvent.KEYCODE_DPAD_RIGHT: {
	       
	        fragment = CarouselFragment.newInstance(selectedCarouselItemIndex + 1);
	        FragmentTransaction ft = getFragmentManager().beginTransaction();
	        ft.replace(R.id.carousel, fragment);
	        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	        ft.commit();
	
	        break;
	    }
	    case KeyEvent.KEYCODE_DPAD_DOWN: {
	        
	        fragment = CarouselFragment.newInstance(selectedCarouselItemIndex);
	        FragmentTransaction ft = getFragmentManager().beginTransaction();
	        ft.replace(R.id.carousel, fragment);
	        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	        ft.commit();
	
	        break;
	    }
	    case KeyEvent.KEYCODE_DPAD_UP: {
	        FragmentTransaction ft = getFragmentManager().beginTransaction();
	        ft.hide(fragment);
	        ft.commit();
	        break;
	    }
	    }
	    return super.onKeyDown(keyCode, event);
	}

    public static class CarouselFragment extends Fragment {
	    static int mCurCheckPosition = 0;
	    @Override
	    public void onActivityCreated(Bundle savedInstanceState) {
	        super.onActivityCreated(savedInstanceState);
	    }
	
	    public static CarouselFragment newInstance(int index) {
	        CarouselFragment f = new CarouselFragment();
	        mCurCheckPosition = index;
	        return f;
	    }
	
	    @Override
	    public View onCreateView(
	            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        super.onCreateView(inflater, container, savedInstanceState);
	        Gallery gallery = (Gallery) inflater.inflate(R.layout.carousel_gallery, null);
	
	        gallery.setAdapter(new GalleryAdapter(mContext));
	        gallery.setOnItemClickListener(new OnItemClickListener() {
	            public void onItemClick(AdapterView<?> l, View v, int position, long id) {
	                count = position;
	                ((Carousel) mContext).setSelectedId(id);
	                FragmentTransaction ft = getFragmentManager().beginTransaction();
	                ft.hide(fragment);
	                ft.commit();
	            }
	        });
	
	        gallery.requestFocus();
	        gallery.setSelection(mCurCheckPosition);
	
	        return gallery;
	    }
	}

	protected void setSelectedId(long id) {
		featuredId.setText(Long.toString(id));
	}
}
