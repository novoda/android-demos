package uk.co.halfninja.wallpaper.parallax.test;

import android.test.AndroidTestCase;

import uk.co.halfninja.wallpaper.parallax.gl.BitmapUtils;

public class BitmapUtilsTest extends AndroidTestCase {
	public void testPowerOfTwo() {
		assertEquals(2, BitmapUtils.getNextPowerOfTwo(2));
		assertEquals(4, BitmapUtils.getNextPowerOfTwo(3));
		assertEquals(4, BitmapUtils.getNextPowerOfTwo(4));
		assertEquals(64, BitmapUtils.getNextPowerOfTwo(47));
		assertEquals(128, BitmapUtils.getNextPowerOfTwo(99));
	}
}
