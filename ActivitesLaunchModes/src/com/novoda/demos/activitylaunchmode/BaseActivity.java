package com.novoda.demos.activitylaunchmode;

import java.util.Stack;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public abstract class BaseActivity extends Activity {
	
	private String [] intentFlagsText = {
							 "CLEAR_TOP"
							, "CLEAR_WHEN_TASK_RESET"
							, "EXCLUDE_FROM_RECENTS"
							, "FORWARD_RESULT"
							, "MULTIPLE_TASK"
							, "NEW_TASK"
							, "NO_HISTORY"
							, "NO_USER_ACTION"
							, "PREVIOUS_IS_TOP"
							, "REORDER_TO_FRONT"
							, "RESET_TASK_IF_NEEDED"
							, "SINGLE_TOP"};
	
	private int [] intentFlags = {
		Intent.FLAG_ACTIVITY_CLEAR_TOP,
		Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET,
		Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS,
		Intent.FLAG_ACTIVITY_FORWARD_RESULT,
		Intent.FLAG_ACTIVITY_MULTIPLE_TASK,
		Intent.FLAG_ACTIVITY_NEW_TASK,
		Intent.FLAG_ACTIVITY_NO_HISTORY,
		Intent.FLAG_ACTIVITY_NO_USER_ACTION,
		Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP,
		Intent.FLAG_ACTIVITY_REORDER_TO_FRONT,
		Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED,
		Intent.FLAG_ACTIVITY_SINGLE_TOP
	};
	
	private int chosenFlags;
	
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
		removeFromStack();
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
    
	private void removeFromStack() {
		BaseApplication app = (BaseApplication)getApplication();
		app.removeFromStack(this);
	}

	private String getLaunchMode(){
    	return "[" + hashCode() + "] " + getClass().getSimpleName();
    }
	
	public void generalOnClick(View v){
		if(isIntentFilterMode()){
			showIntentFilterDialog(v);
		}else{
			startActivity(getNextIntent(v));
		}
	}

	private void showIntentFilterDialog(final View nextActivityBtn) {
		chosenFlags = 0;
	    final Builder build = new Builder(this);
	    build.setTitle("List selection");
	    build.setCancelable(true);
	    final OnMultiChoiceClickListener onClick =
	      new OnMultiChoiceClickListener() {
	    	@Override 
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				chosenFlags |= intentFlags[which];
			}
	      };
	    build.setMultiChoiceItems(intentFlagsText, null, onClick);
	    build.setPositiveButton("Done", new OnClickListener() {
	      @Override public void onClick(final DialogInterface dialog, final int which) {
	    	  Intent intent = getNextIntent(nextActivityBtn);
	    	  intent.setFlags(chosenFlags);
	    	  startActivity(intent);
	      }
	    });
	    build.show();
	}

	private boolean isIntentFilterMode() {
		BaseApplication app = (BaseApplication)getApplication();
		return app.isIntentFilterMode();
	}
	
	private void setIntentFilterMode(boolean mode) {
		BaseApplication app = (BaseApplication)getApplication();
		app.setIntentFilterMode(mode);
	}

	private Intent getNextIntent(View v){
		Class<? extends BaseActivity> nextActivity = null;
		switch(v.getId()){
		case R.id.btn_standard:
			nextActivity = Standard.class;
			break;
		case R.id.btn_singletop:
			nextActivity = SingleTop.class;
			break;
		case R.id.btn_singletask:
			nextActivity = SingleTask.class;
			break;
		case R.id.btn_singleInstance:
			nextActivity = SingleInstance.class;
			break;
		}
		return new Intent(this, nextActivity);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.base_activity, menu);
	    log();
	    return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem filterOption = menu.findItem(R.id.menuitem_intentfilter_mode);
		String title = "Turn " + (isIntentFilterMode() ? "off" : "on") + " IntentFilter mode"; 
		filterOption.setTitle(title);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.menuitem_intentfilter_mode:
	        setIntentFilterMode(!isIntentFilterMode());
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
    
    public abstract int getBackgroundColour();
}
