package com.novoda;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TabHost;

public class SelfContainedTabHost extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabhost_container);

        TabHost tabs = (TabHost)this.findViewById(R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec one = tabs.newTabSpec("one");
        one.setContent(R.id.tab1content);
        one.setIndicator("TAB 1");
        tabs.addTab(one);

        TabHost.TabSpec two = tabs.newTabSpec("two");
        two.setContent(R.id.tab2content);
        two.setIndicator("TAB 2");
        tabs.addTab(two);

        tabs.setCurrentTab(0);
    }
}