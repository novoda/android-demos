package com.novoda.example.compass.view;

import java.util.ArrayList;

import com.novoda.example.compass.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

public class RotatedImageView extends View {

    private static final int INVALID_POINTER_ID = -1;

    private Drawable backgroundImage;
    private float mPosX;
    private float mPosY;

    private ArrayList<PointF> touchPoints = null;

    private float mLastTouchX;
    private float mLastTouchY;
    private int mActivePointerId = INVALID_POINTER_ID;

    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;

    private float degrees;
    private float old_degrees;
    private boolean firstIterationMultitouch;
    private float last_rotation_degrees;

    private float last_center_rotation_position_x;
    private float last_center_rotation_position_y;

    private boolean isMultiTouch = false;

    private boolean isCompassActivated;
    private float compass_degrees;
    private float original_compass_degrees;

    private String TAG = "RotatedImage";

    public RotatedImageView(Context context) {
        this(context, null, 0);
    }

    public RotatedImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RotatedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialization(context);
    }

    private void initialization(Context context) {
        initializeBackgroundImage(context);
        initializeScaleDetector(context);
        initializeVariables();
        last_rotation_degrees = 0;
        last_center_rotation_position_x = 0;
        last_center_rotation_position_y = 0;
        isCompassActivated = false;
    }

    private void initializeBackgroundImage(Context context) {
        backgroundImage = context.getResources().getDrawable(R.drawable.floorplan);
        backgroundImage.setBounds(0, 0, backgroundImage.getIntrinsicWidth(), backgroundImage.getIntrinsicHeight());

    }

    private void initializeScaleDetector(Context context) {
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    private void initializeVariables() {
        touchPoints = new ArrayList<PointF>();
        firstIterationMultitouch = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        activateScaleDetector(ev);

        final int action = ev.getAction();

        switch (action & MotionEvent.ACTION_MASK) {

        case MotionEvent.ACTION_DOWN: {
            updateXYPositionSimpleTouch(ev);
            updatePointerId(ev);
            mActivePointerId = ev.getPointerId(0);
            // invalidate();
            break;
        }

        case MotionEvent.ACTION_POINTER_DOWN: {
            Log.d(TAG, "Pointer down");
            isMultiTouch = true;
            firstIterationMultitouch = true;
            setInitalPositionMultiPoints(ev);
            invalidate();
            break;
        }

        case MotionEvent.ACTION_MOVE: {

            if (isMultiTouch) {
                setMultitouchPoints(ev);
            } else {
                updatePositionToMoveTheImage(ev);
            }
            break;
        }

        case MotionEvent.ACTION_UP: {
            mActivePointerId = INVALID_POINTER_ID;
            Log.d(TAG, "Action up");
            break;
        }

        case MotionEvent.ACTION_CANCEL: {
            mActivePointerId = INVALID_POINTER_ID;
            break;
        }

        case MotionEvent.ACTION_POINTER_UP: {
            Log.d(TAG, "Pointer up");
            isMultiTouch = false;
            last_rotation_degrees = degrees + last_rotation_degrees;
            Log.d(TAG, "last_rotation_degrees: " + last_rotation_degrees);
            updateXYPositionSimpleTouch(ev);
            updatePointerId(ev);

            break;
        }

        }

        return true;
    }

    private void activateScaleDetector(MotionEvent ev) {
        mScaleDetector.onTouchEvent(ev);
    }

    private void updateXYPositionSimpleTouch(MotionEvent ev) {
        mLastTouchX = ev.getX();
        mLastTouchY = ev.getY();

    }

    private void updatePointerId(MotionEvent ev) {
        final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        final int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            // This was our active pointer going up. Choose a new
            // active pointer and adjust accordingly.
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mLastTouchX = ev.getX(newPointerIndex);
            mLastTouchY = ev.getY(newPointerIndex);
            mActivePointerId = ev.getPointerId(newPointerIndex);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        
        if (isCompassActivated) {
            canvas.rotate(original_compass_degrees - compass_degrees, 300, 600);
        }

        else {

            if (!isMultiTouch) {
                canvas.translate(mPosX, mPosY);
                canvas.rotate(last_rotation_degrees, last_center_rotation_position_x, last_center_rotation_position_y);
            }

            if (isMultiTouch) {

                if (touchPoints.size() > 1) {
                    // canvas.scale(mScaleFactor, mScaleFactor);
                    rotateImageFromMultitouch(touchPoints, canvas);
                }
            }
        }

        drawCanvas(canvas);

    }

    private void rotateImageFromMultitouch(ArrayList<PointF> touchPoints, Canvas canvas) {

        PointF midpt;

        for (int index = 1; index < touchPoints.size(); ++index) {

            if (firstIterationMultitouch) {

                double r = Math.atan2(touchPoints.get(index).x - touchPoints.get(index - 1).x, touchPoints.get(index).y
                        - touchPoints.get(index - 1).y);
                old_degrees = -(int) Math.toDegrees(r);
                firstIterationMultitouch = false;
                canvas.translate(mPosX, mPosY);
                canvas.rotate(last_rotation_degrees, last_center_rotation_position_x, last_center_rotation_position_y);

            }

            else {

                double r = Math.atan2(touchPoints.get(index).x - touchPoints.get(index - 1).x, touchPoints.get(index).y
                        - touchPoints.get(index - 1).y);
                int currentDegrees = -(int) Math.toDegrees(r);

                degrees = currentDegrees - old_degrees;

                

                midpt = getMidPoint(touchPoints.get(index - 1).x, touchPoints.get(index - 1).y,
                        touchPoints.get(index).x, touchPoints.get(index).y);

                last_center_rotation_position_x = midpt.x;
                last_center_rotation_position_y = midpt.y;

                canvas.translate(mPosX, mPosY);
                canvas.rotate(degrees + last_rotation_degrees, -mPosX + midpt.x, -mPosY + midpt.y);

            }
        }
    }

    private void drawCanvas(Canvas canvas) {
        canvas.save();
        backgroundImage.draw(canvas);
        canvas.restore();
    }

    // Still not used
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));

            invalidate();
            return true;
        }
    }

    private PointF getMidPoint(float x1, float y1, float x2, float y2) {
        PointF point = new PointF();

        float x = x1 + x2;
        float y = y1 + y2;

        point.set(x / 2, y / 2);

        return point;
    }

    public void setMultitouchPoints(MotionEvent event) {

        touchPoints.clear();

        int pointerIndex = 0;

        for (int index = 0; index < event.getPointerCount(); ++index) {
            pointerIndex = event.getPointerId(index);

            touchPoints.add(new PointF(event.getX(pointerIndex), event.getY(pointerIndex)));
        }
    }

    public void setInitalPositionMultiPoints(MotionEvent event) {

        touchPoints.clear();

        int pointerIndex = 0;

        for (int index = 0; index < event.getPointerCount(); ++index) {
            pointerIndex = event.getPointerId(index);

            touchPoints.add(new PointF(event.getX(pointerIndex), event.getY(pointerIndex)));
        }

        double r = Math.atan2(touchPoints.get(1).x - touchPoints.get(1 - 1).x,
                touchPoints.get(1).y - touchPoints.get(1 - 1).y);
        old_degrees = -(int) Math.toDegrees(r);
    }

    public void updatePositionToMoveTheImage(MotionEvent event) {

        final int pointerIndex = event.findPointerIndex(mActivePointerId);
        final float x = event.getX(pointerIndex);
        final float y = event.getY(pointerIndex);

        if (!mScaleDetector.isInProgress()) {

            final float dx = x - mLastTouchX;
            final float dy = y - mLastTouchY;

            mPosX += dx;
            mPosY += dy;

            invalidate();
        }

        mLastTouchX = x;
        mLastTouchY = y;

    }

    public void calculateNewPoints() {

    }

    public void checkIfCompassIsActivated() {

    }

    public void setCompassActivated(boolean activated, float originalDegrees) {
        isCompassActivated = activated;
        original_compass_degrees = originalDegrees;
    }

    public void updateCompassDegrees(float degrees) {
        compass_degrees = -degrees;
        invalidate();
    }


}
