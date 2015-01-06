package com.novoda.utils;

import android.util.FloatMath;
import android.view.MotionEvent;

public class PinchZoomDetector {
	public static final int OTHER = 0;
	public static final int ZOOMING_STARTED = 1;
	public static final int ZOOMING_IN = 2;
	public static final int ZOOMING_OUT = 3;
	public static final int ZOOMING_STOPPED = 4;

	public float minimumFingerDistance = 20f;
	public float zoomInterval = 50f;

	private boolean zooming;
	private float oldDistance;

	public PinchZoomDetector() {
	}

	public PinchZoomDetector(float minimumFingerDistance, float zoomInterval) {
		this.minimumFingerDistance = minimumFingerDistance;
		this.zoomInterval = zoomInterval;
	}

	public PinchZoomDetector(float zoomInterval) {
		this.zoomInterval = zoomInterval;
	}

	public int detectZoom(MotionEvent event) {
		int result = OTHER;
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_MOVE:
				result = moving(event);
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				result = release(event);
				break;
		}
		return result;
	}

	private int moving(MotionEvent event) {
		int result = OTHER;
		float distance = fingerDistance(event);
		if (zooming == false) {
			if (event.getPointerCount() > 1) {
				if (distance > minimumFingerDistance) {
					zooming = true;
					oldDistance = distance;
					result = ZOOMING_STARTED;
				}
			}
		}
		if (zooming) {
			if (distance + zoomInterval < oldDistance) {
				result = ZOOMING_OUT;
				oldDistance = distance;
			} else if (distance - zoomInterval > oldDistance) {
				result = ZOOMING_IN;
				oldDistance = distance;
			}
		}
		return result;
	}

	private int release(MotionEvent event) {
		int result = OTHER;
		if (zooming) {
			zooming = false;
			result = ZOOMING_STOPPED;
		}
		return result;
	}

	private float fingerDistance(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

}
