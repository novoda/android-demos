package com.novoda.wallpaper;

import java.util.Timer;
import java.util.TimerTask;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.SystemClock;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;


public class ImageSwitchWallPaper extends WallpaperService {

    private final Handler mHandler = new Handler();
    private static final String TAG = ImageSwitchWallPaper.class.getSimpleName();
    
    @Override
    public void onCreate() {
    	super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public Engine onCreateEngine() {
        return new OutRunEngine();
    }

    class OutRunEngine extends Engine {

		OutRunEngine() {
            Resources res = getResources();
            carFrontBitmap = res.getIdentifier("day_front", "drawable", "com.novoda.wallpaper");
            carRightBitmap = res.getIdentifier("day_right", "drawable", "com.novoda.wallpaper");
            carLeftBitmap = res.getIdentifier("day_left", "drawable", "com.novoda.wallpaper");
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            // By default we don't get touch events, so enable them.
            setTouchEventsEnabled(true);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mHandler.removeCallbacks(mDrawWallpaper);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            mVisible = visible;
            if (visible) {
                drawFrame();
            } else {
                mHandler.removeCallbacks(mDrawWallpaper);
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            // store the center of the surface, so we can draw the cube in the right spot
            mCenterX = width/2.0f;
            mCenterY = height/2.0f;
            drawFrame();
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            mVisible = false;
            mHandler.removeCallbacks(mDrawWallpaper);
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset, float xStep, float yStep, int xPixels, int yPixels) {
            mOffset = xOffset;
//        	Log.i(TAG, "XOffset["+xOffset+"] xStep["+xStep+"] xPixels["+xPixels+"]");
            
            drawFrame();
        }

        /*
         * Store the position of the touch event so we can use it for drawing later
         */
        @Override
        public void onTouchEvent(MotionEvent event) {
        	
        	
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
            	mDragEventInProgress = true;
            	mDragEventStartX = event.getX();
            }
            
            if (event.getAction() == MotionEvent.ACTION_UP) {
            	
            	Log.i(TAG, "X:["+event.getX()+"+] - dragStart["+mDragEventStartX+"] =" + (event.getX() - mDragEventStartX));
            	boolean draggedLotsRight = (mDragEventStartX - event.getX()) >=160;
            	boolean draggedLotsLeft = (event.getX() - mDragEventStartX) >=160;
            	
				if( (mDragEventStartX > 150) && draggedLotsRight ){
            		Log.d(TAG, "Driving animation started Right >");
            		takingACorner =true;
            		slidePic = carRightBitmap;
            		new Timer().schedule(new TimerTask(){
						@Override
						public void run() {
							takingACorner =false;
						}}, 1000);
            	}
            	
            	if( (mDragEventStartX < 150) && draggedLotsLeft ){
            		Log.d(TAG, "Driving animation started < Left");
            		takingACorner =true;	
            		slidePic = carLeftBitmap;
            		new Timer().schedule(new TimerTask(){
						@Override
						public void run() {
		            		takingACorner =false;
						}}, 1000);
            	}
            	
            	mDragEventInProgress = false;
            	mDragEventStartX = 0;
            }
            	
            	
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                mTouchX = event.getX();
                mTouchY = event.getY();
            	
            	if(mTouchX >= 150){
                	mDrivingForward = false;
                	mTurningLeft = false;
                	mTurningRight = true;
//            		Log.i(TAG, "Right:  mTouchX ["+mTouchX+"]");
            	}else{
                	mDrivingForward = false;
                	mTurningLeft = true;
                	mTurningRight = false;
//            		Log.i(TAG, "Left:   mTouchX ["+mTouchY+"]");
            	}
            	
            } else {
            	mDrivingForward = true;
            	mTurningLeft = false;
            	mTurningRight = false;
                mTouchX = -1;
                mTouchY = -1;
            }
            super.onTouchEvent(event);
        }

        void drawFrame() {
            final SurfaceHolder holder = getSurfaceHolder();

            Canvas c = null;
            try {
                c = holder.lockCanvas();
                if (c != null) {
                	drawCar(c);
                }
            } finally {
                if (c != null) holder.unlockCanvasAndPost(c);
            }

            // Reschedule the next redraw
            mHandler.removeCallbacks(mDrawWallpaper);
            if (mVisible) {
                mHandler.postDelayed(mDrawWallpaper, 1000 / 25);
            }
        }

        private void drawCar(Canvas c) {
    		
        	if(takingACorner){
    			c.drawBitmap(BitmapFactory.decodeResource(getResources(), slidePic), 20, 120, null);
    		}else{
    			
	        	if(mDragEventInProgress){
//	        		if(mDragEventStartX > 150){
//	        			c.drawBitmap(carRightBitmap, 20, 0, null);
//	        		}else{
//	        			c.drawBitmap(carLeftBitmap, 20, 0, null);
//	        		}
	        	}else{
	        		c.drawBitmap(BitmapFactory.decodeResource(getResources(), carFrontBitmap), 20, 120, null);
	        	}
    		}
		}

        void drawTouchPoint(Canvas c) {
            if (mTouchX >=0 && mTouchY >= 0) {
                c.drawCircle(mTouchX, mTouchY, 80, mPaint);
            }
        }

		private final Paint mPaint = new Paint();
		private float mOffset;
		private float mTouchX = -1;
		private float mTouchY = -1;
		private float mCenterX;
		private float mCenterY;
		private boolean mDrivingForward = true;
		private boolean mTurningLeft = false;
		private boolean mTurningRight = false;
		private boolean mDragEventAnimStarted = false;
		private int carRightBitmap;
		private int carLeftBitmap;
		private int carFrontBitmap;
		private int slidePic;
		private boolean duringSlide = false;
		private boolean mDragEventInProgress = false;
		private float mDragEventStartX = 0;
		private final Runnable mDrawWallpaper = new Runnable() {
		    public void run() {
		        drawFrame();
		    }
		};
		private boolean mVisible;
		private float xLastOffset;
		private boolean takingACorner = false;

    }
}
