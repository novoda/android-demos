package com.novoda;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class Tab1Activity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.tab1);
        
        findViewById(R.id.tab2).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent("com.novoda.TAB");
				intent.putExtra("tab", 1);
				sendBroadcast(intent);
			}
		});
        
	}
}
