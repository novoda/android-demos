package com.novoda.example.compass;

import utils.RotatedImageView;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class ImageActivity extends Activity implements OnTouchListener{

    private ImageView image;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        RotatedImageView view = new RotatedImageView(this);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        
        setContentView(view);
        
//        setContentView(R.layout.activity_image);        
//        loadImage();
    }
    
    private void loadImage(){
        image = (ImageView) findViewById(R.id.backgroundimage);
        image.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        
        double r = Math.atan2(event.getX() - image.getWidth() / 2, image.getHeight() / 2 - event.getY());
        int rotation = (int) Math.toDegrees(r);
        
        if (event.getAction() == MotionEvent.ACTION_MOVE)
        {
            updateRotation(rotation);
        }
        
        return true;
    }
    
    private void updateRotation(double rot)
    {
        float newRot = new Float(rot);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.floorplan);

        Matrix matrix = new Matrix();
        matrix.postRotate(newRot - 50);

        Bitmap redrawnBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        image.setImageBitmap(redrawnBitmap);
    }
    
    
    
}
