package com.novoda;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

import com.novoda.utils.PinchZoomDetector;

public class Main extends Activity implements OnTouchListener {
	private static final String TAG = "Pinch Zoom Detector";
	private Toast toast;
	private PinchZoomDetector zoomDetector;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		View view = findViewById(R.id.activity_main);
		view.setOnTouchListener(this);
		toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		zoomDetector = new PinchZoomDetector();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int result = zoomDetector.detectZoom(event);
		int resId = 0;
		
		switch(result){
			case PinchZoomDetector.ZOOMING_STARTED:
				resId = R.string.toast_zooming_started;
				break;
			case PinchZoomDetector.ZOOMING_IN:
				resId = R.string.toast_zooming_in;
				break;
			case PinchZoomDetector.ZOOMING_OUT:
				resId = R.string.toast_zooming_out;
				break;
			case PinchZoomDetector.ZOOMING_STOPPED:
				resId = R.string.toast_zooming_stopped;
				break;
		}
		
		if(resId != 0){
			String s = getResources().getString(resId);
			shortToast(s);
			Log.d(TAG,s);
		}
		
		return true;
	}

	private void shortToast(String s) {
		toast.setText(s);
		if(!toast.getView().isShown()){
			toast.show();
		}
	}

}