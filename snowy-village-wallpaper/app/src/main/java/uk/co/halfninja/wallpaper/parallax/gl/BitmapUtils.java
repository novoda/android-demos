package uk.co.halfninja.wallpaper.parallax.gl;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.util.Log;

public class BitmapUtils {

	private static final String TAG = BitmapUtils.class.getName();

	public static Bitmap createPowerOfTwoBitmap(Bitmap bitmap) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		int w2 = getNextPowerOfTwo(w);
		int h2 = getNextPowerOfTwo(h);
		if(w == w2 && h == h2) {
			// What luck, it's already compatible.
			return bitmap;
		} else {
			Bitmap biggerBitmap = Bitmap.createBitmap(w2, h2, Config.ARGB_8888);
			Canvas canvas = new Canvas(biggerBitmap);
			canvas.drawBitmap(bitmap, 0, 0, null);
			return biggerBitmap;
		}
	}
	
	/**
	 * Returns the next power of two that is greater than or
	 * equal to the given value. If the given number is already
	 * a power of two, that number is returned.
	 */
	public static int getNextPowerOfTwo(int value) {
		int result = 1;
		value--;
		while (value > 0) {
			result = result << 1;
			value = value >> 1;
		}
		return result;
	}

}
