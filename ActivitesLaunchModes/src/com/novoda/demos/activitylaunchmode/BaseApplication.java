package com.novoda.demos.activitylaunchmode;

import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import android.app.ActivityManager;
import android.app.Application;
import android.app.ActivityManager.RunningTaskInfo;

public class BaseApplication extends Application{

	private HashMap<Integer, Stack<BaseActivity>> tasks;
	
	private ActivityManager manager;
	
	private boolean intentFilterMode;
	
	@Override
	public void onCreate() {
		super.onCreate();
		manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		tasks = new HashMap<Integer, Stack<BaseActivity>>();
	}
	
	public void pushToStack(BaseActivity activity){
		int currentTaskId = getCurrentTaskId();
		if(!tasks.containsKey(currentTaskId)){
			tasks.put(currentTaskId, new Stack<BaseActivity>());
		}
		Stack<BaseActivity> stack = tasks.get(currentTaskId);
		stack.add(activity);
	}
	
	private int getCurrentTaskId() {
		List<RunningTaskInfo> runningTasks = manager.getRunningTasks(1);
		RunningTaskInfo runningTask = runningTasks.get(0);
		return runningTask.id;
	}

	public void removeFromStack(BaseActivity activity){
		Stack<BaseActivity> stack = tasks.get(getCurrentTaskId());
		if(stack != null){
			stack.remove(activity);
		}
	}
	
	public Stack<BaseActivity> getCurrentStack(){
		return tasks.get(getCurrentTaskId());
	}
	
	public void setIntentFilterMode(boolean mode){
		intentFilterMode = mode;
	}
	
	public boolean isIntentFilterMode(){
		return intentFilterMode;
	}
	
}
