package com.novoda.example.compass.view;

import java.util.concurrent.atomic.AtomicBoolean;

import com.novoda.example.compass.R;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * This class extends the View class and is designed draw the compass on the
 * View.
 * 
 * @author Justin Wetherell <phishman3579@gmail.com>
 */
public class RotatableImageView extends View {
    private static final AtomicBoolean drawing = new AtomicBoolean(false);
    private static final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private static int parentWidth = 0;
    private static int parentHeight = 0;
    private static Matrix matrix = null;
    private static Bitmap bitmap = null;
    private int rotationValue;
    private boolean isCompassEnabled;

    public RotatableImageView(Context context) {
        super(context);

        initialize();
    }

    public RotatableImageView(Context context, AttributeSet attr) {
        super(context, attr);

        initialize();
    }

    private void initialize() {
        matrix = new Matrix();
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.floorplan);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(parentWidth, parentHeight);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onDraw(Canvas canvas) {
        if (canvas == null)
            throw new NullPointerException();

        if (!drawing.compareAndSet(false, true))
            return;

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        if (bitmap.getWidth() > canvasWidth || bitmap.getHeight() > canvasHeight) {
            bitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmapWidth * .9), (int) (bitmapHeight * .9), true);
        }

        int bitmapX = bitmap.getWidth() / 2;
        int bitmapY = bitmap.getHeight() / 2;

        int parentX = parentWidth / 2;
        int parentY = parentHeight / 2;

        int centerX = parentX - bitmapX;
        int centerY = parentY - bitmapY;


        
        if (isCompassEnabled){
            matrix.setRotate(rotationValue, bitmapX, bitmapY);
        }
        matrix.postTranslate(centerX, centerY);

        canvas.drawBitmap(bitmap, matrix, paint);

        drawing.set(false);
    }
    
    public void setRotationInDegrees(int rotation){
        this.rotationValue = rotation;
        invalidate();
    }
    
    public void useCompassToRotate(boolean isCompassEnabled){
        this.isCompassEnabled = isCompassEnabled;
    }
}
