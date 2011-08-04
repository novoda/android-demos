package com.novoda.demos.activitylaunchmode;

import java.util.Stack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public abstract class BaseActivity extends Activity {
	
	private static final String LOG_TAG = "ActivitesLaunchDemo";
	
	private final int DISPLAY_STACK_DELAY = 500;
	
	private TextView lifecycle;
	
	private StringBuilder lifecycleFlow = new StringBuilder();
	
	private Handler handler = new Handler();

	private LinearLayout currentStackView;

	private Runnable displayStack = new Runnable() {

		public void run() {
			Log.v(LOG_TAG,"===============================");
			BaseApplication app = (BaseApplication)getApplication();
			Stack<BaseActivity> currentStack = app.getCurrentStack();
			for(int location = currentStack.size() - 1; location >= 0 ; location--){
				BaseActivity activity = currentStack.get(location);
				Log.v(LOG_TAG,activity.getClass().getSimpleName());
				ImageView activityImage = new ImageView(BaseActivity.this);
				activityImage.setBackgroundResource(activity.getBackgroundColour());
				LinearLayout.LayoutParams params = new LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 10);
				params.setMargins(2, 2, 2, 2);
				activityImage.setLayoutParams(params);
				currentStackView.addView(activityImage);
			}
			Log.v(LOG_TAG,"===============================");
		}
		
	};
	
	@Override
	public void onContentChanged() {
		log();
		super.onContentChanged();
	}

	@Override
	protected void onDestroy() {
		log();
		BaseApplication app = (BaseApplication)getApplication();
		app.removeFromStack(this);
		super.onDestroy();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		log();
		super.onNewIntent(intent);
	}

	@Override
	protected void onPause() {
		log();
		super.onPause();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		log();
		super.onPostCreate(savedInstanceState);
	}

	@Override
	protected void onPostResume() {
		log();
		super.onPostResume();
	}

	@Override
	protected void onRestart() {
		log();
		super.onRestart();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		log();
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onResume() {
		log();
		currentStackView.removeAllViews();
		handler.postDelayed(displayStack, DISPLAY_STACK_DELAY);
		super.onResume();
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		log();
		return super.onRetainNonConfigurationInstance();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		log();
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onStart() {
		log();
		super.onStart();
	}

	@Override
	protected void onStop() {
		log();
		super.onStop();
	}
	
	private void log() {
    	Thread current = Thread.currentThread();
    	StackTraceElement trace = current.getStackTrace()[3];
    	String method = trace.getMethodName();
    	Log.v(LOG_TAG,getLaunchMode() + ": " + method);
    	lifecycleFlow.append(method).append("\n");
    	if(lifecycle != null){
    		lifecycle.setText(lifecycleFlow.toString());
    	}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findViewById(R.id.main_layout).setBackgroundResource(getBackgroundColour());
        TextView header = (TextView)findViewById(R.id.header);
        header.setText(getLaunchMode());
        lifecycle = (TextView)findViewById(R.id.lifecycle_content);
        lifecycle.setMovementMethod(new ScrollingMovementMethod());
        currentStackView = (LinearLayout) findViewById(R.id.current_stack_view);
        log();
		addToStack();
    }

    private void addToStack() {
    	BaseApplication app = (BaseApplication)getApplication();
    	app.pushToStack(this);
	}

	private String getLaunchMode(){
    	return "[" + hashCode() + "] " + getClass().getSimpleName();
    }
    
	public void onStandardClick(View v){
    	startActivity(new Intent(this, Standard.class));
    }
    
    public void onSingleTopClick(View v){
    	startActivity(new Intent(this, SingleTop.class));
    }
    
    public void onSingleTaskClick(View v){
    	startActivity(new Intent(this, SingleTask.class));
    }
    
    public void onSingleInstanceClick(View v){
    	startActivity(new Intent(this, SingleInstance.class));
    }
    
    public abstract int getBackgroundColour();
}
