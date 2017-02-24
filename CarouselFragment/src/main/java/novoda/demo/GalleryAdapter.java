package novoda.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

class GalleryAdapter extends BaseAdapter {

    private static final String TAG = "GalleryAdapter";
    private static final String PLACEHOLDER_FILE = "placeholder.png";
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
            InputStream bitmap = mContext.getAssets().open(PLACEHOLDER_FILE);
            image = BitmapFactory.decodeStream(bitmap);
        } catch (IOException exception) {
            Log.e(TAG, "An error occurred when you have tried to open the file: "+ PLACEHOLDER_FILE, exception);
        }

        imageView.setImageBitmap(image);
        return view;
    }
}
