package com.novoda;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class Container extends TabActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		TabHost host = getTabHost();
		host.addTab(host.newTabSpec("one").setIndicator("TAB1").setContent(new Intent(this, Tab1Activity.class)));
		host.addTab(host.newTabSpec("two").setIndicator("TAB2").setContent(new Intent(this, Tab2Activity.class)));
	}
}