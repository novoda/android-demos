package com.novoda;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;

public class SelfContainedTabHost extends Activity {
	
	private static final String TAG = SelfContainedTabHost.class.getSimpleName(); 

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabhost_container);

        TabHost tabs = (TabHost)this.findViewById(R.id.tabhost);
        tabs.setup();

        tabs.addTab(tabs.newTabSpec("one").setContent(R.id.tab1content).setIndicator("TAB 1"));
        tabs.addTab(tabs.newTabSpec("two").setContent(R.id.tab2content).setIndicator("TAB 2"));
        tabs.setCurrentTab(0);
    }
}