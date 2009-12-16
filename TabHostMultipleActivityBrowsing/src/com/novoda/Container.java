package com.novoda;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.style.SuperscriptSpan;
import android.util.Log;
import android.widget.TabHost;

public class Container extends TabActivity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		host = getTabHost();
		Intent intent = new Intent(this, Tab1Activity.class);
		host.addTab(host.newTabSpec("one").setIndicator("TAB1").setContent(intent));
		intent = new Intent(this, Tab2Activity.class);
		host.addTab(host.newTabSpec("two").setIndicator("TAB2").setContent(intent));
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
            		Log.i("handler", "using the handler");
                    host.setCurrentTab(msg.arg1);            		
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
	
	private TabHost host;
	public static final int SWITCH_TAB = 2545;
	protected static TabChangeReceiver receiver;
	
}