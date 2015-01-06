package com.novoda.wallpaper;

import java.util.Timer;
import java.util.TimerTask;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class AnimSwitchWallPaper extends WallpaperService {

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
        	
        	for (int i = 0; i< FRONT_RES; i++) {
        		mFrontPicIds[i] = res.getIdentifier("front_day00" + (i + 1), "drawable", "com.novoda.wallpaper");
        	}
        	for (int i = 0; i< LEFT_RES; i++) {
        		mLeftPicIds[i] = res.getIdentifier("left_day00" + (i + 1), "drawable", "com.novoda.wallpaper");
        	}
        	for (int i = 0; i< RIGHT_RES; i++) {
        		mRightPicIds[i] = res.getIdentifier("right_day00" + (i + 1), "drawable", "com.novoda.wallpaper");
        	}
        }
        
        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
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
            	boolean draggedLotsRight = (mDragEventStartX - event.getX()) >=160;
            	boolean draggedLotsLeft = (event.getX() - mDragEventStartX) >=160;
            	Log.v(TAG, "X:["+event.getX()+"+] - dragStart["+mDragEventStartX+"] =" + (event.getX() - mDragEventStartX));
            	
				if( (mDragEventStartX > 150) && draggedLotsRight ){
            		takingACorner =true;
            		currentDirection = DRIVING_RIGHT;
            		Log.d(TAG, "Driving animation started Right >");
            		new Timer().schedule(new TimerTask(){
						@Override
						public void run() {
							takingACorner =false;
		            		picIdx =0;
						}}, 1000);
            	}
            	
            	if( (mDragEventStartX < 150) && draggedLotsLeft ){
            		takingACorner =true;
            		currentDirection = DRIVING_LEFT;
            		Log.d(TAG, "Driving animation started < Left");
            		new Timer().schedule(new TimerTask(){
						@Override
						public void run() {
		            		takingACorner =false;
		            		picIdx =0;
						}}, 1000);
            	}
            	
            	mDragEventInProgress = false;
            	mDragEventStartX = 0;
            }
            super.onTouchEvent(event);
        }

        
        void drawFrame() {
            final SurfaceHolder holder = getSurfaceHolder();
            Canvas c = null;
            try {
                c = holder.lockCanvas();
                if (c != null) {
                	c.save();
                	drawCar(c);
                    c.restore();
                }
            } finally {
                if (c != null) holder.unlockCanvasAndPost(c);
            }

            // Reschedule the next redraw
            mHandler.removeCallbacks(mDrawWallpaper);
            if (mVisible) {
                mHandler.postDelayed(mDrawWallpaper, 1000 / 300);
            }
        }

        private void drawCar(Canvas c) {
    		
        	if(takingACorner){
        		if(currentDirection == DRIVING_RIGHT){
	        		drawAnim(c, mRightPicIds);        			
        		}else{
	        		drawAnim(c, mLeftPicIds);
        		}
    		}else{
	        	if(!mDragEventInProgress){
	        		drawAnim(c, mFrontPicIds);
	        	}else{
/*
 * Uncomment this to respond 
 * to all onscreen touch events 	        		
 *
 *	        		if(mDragEventStartX > 150){
 *	        			drawAnimRight(c, mLeftPics);
 *	        		}else{
 *						drawAnim(c, mLeftPics);
 *	        		}
 */	        	
	        	}
    		}
		}

        void drawAnim(Canvas c, int[] pics) {
        	c.drawBitmap(BitmapFactory.decodeResource(getResources(), pics[picIdx]), 0, 100, null);
        	++picIdx;
        	if (picIdx == FRONT_RES) picIdx = 0;
        }

        private int picIdx = 0;
		private final Paint mPaint = new Paint();
		private float mTouchX = -1;
		private float mTouchY = -1;
		private int currentDirection = DRIVING_FORWARD;
		private static final int DRIVING_FORWARD = 5678;
		private static final int DRIVING_LEFT = 9876;
		private static final int DRIVING_RIGHT = 234;
		private boolean mDragEventInProgress = false;
		private float mDragEventStartX = 0;
		private boolean mVisible;
		private float mPosY;
		private boolean takingACorner = false;
		private Matrix mMatrix = new Matrix();
		private static final int FRONT_RES = 4;
		private static final int LEFT_RES = 4;
		private static final int RIGHT_RES = 4;
		private final int[] mFrontPicIds = new int[FRONT_RES];
		private final int[] mRightPicIds = new int[RIGHT_RES];
		private final int[] mLeftPicIds = new int[LEFT_RES];

		private final Runnable mDrawWallpaper = new Runnable() {
		    public void run() {
		        drawFrame();
		    }
		};
    }

	private final Handler mHandler = new Handler();
	private static final String TAG = AnimSwitchWallPaper.class.getSimpleName();
}
