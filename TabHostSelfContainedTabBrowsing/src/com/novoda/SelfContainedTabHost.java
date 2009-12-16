package com.novoda;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TabHost;

public class SelfContainedTabHost extends Activity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabhost_container);

        tabHost = (TabHost)this.findViewById(R.id.tabhost);
        tabHost.setup();

        tabHost.addTab(tabHost.newTabSpec("one").setContent(R.id.tab1content).setIndicator("TAB 1"));
        tabHost.addTab(tabHost.newTabSpec("two").setContent(R.id.tab2content).setIndicator("TAB 2"));
        tabHost.findViewById(R.id.tab1button).setOnClickListener(goToTab2());
        tabHost.findViewById(R.id.tab2button).setOnClickListener(goToTab1());
    }
	
	@Override
	protected void onResume() {
		super.onResume();
		receiver = new TabChangeReceiver();
		registerReceiver(receiver, new IntentFilter("com.novoda.TAB"), null, mHandler);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
	};
	
    protected Handler mHandler = new Handler() {
        @Override public void handleMessage(Message msg) {
            switch (msg.what) {
            	case SWITCH_TAB:
                    tabHost.setCurrentTab(msg.arg1);            		
            		break;
            }
        }
    };

    public class TabChangeReceiver extends android.content.BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			int intExtra = intent.getIntExtra("tab", 0);
			Log.i(TabChangeReceiver.class.getSimpleName(), "Recieved broadcast with extra=[" + intExtra + "]");
			mHandler.sendMessage(mHandler.obtainMessage(SWITCH_TAB, intExtra, 0));
		}
	}

	private OnClickListener goToTab1() {
		return new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent("com.novoda.TAB");
				intent.putExtra("tab", 0);
				sendBroadcast(intent);
			}
		};
	}	
	
	private OnClickListener goToTab2() {
		return new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent("com.novoda.TAB");
				intent.putExtra("tab", 1);
				sendBroadcast(intent);
			}
		};
	}
	
	private TabHost tabHost;
	public static final int SWITCH_TAB = 2545;
	protected static TabChangeReceiver receiver;
}