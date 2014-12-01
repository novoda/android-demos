package novoda.demo;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

class GalleryAdapter extends BaseAdapter {

    private final Context mContext;

    public GalleryAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return 15;
    }

    public Object getItem(int position) {
        return new Object();
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.carousel_gallery_li, null);
        } else {
            view = convertView;
        }
        final ImageView imageView = (ImageView) view.findViewById(R.id.image);
        
        Bitmap image = null;
        try {
            InputStream bitmap=mContext.getAssets().open("placeholder.png");
            image=BitmapFactory.decodeStream(bitmap);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        
        imageView.setImageBitmap(image);
        return view;
    }
}
