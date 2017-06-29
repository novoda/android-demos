package uk.co.halfninja.wallpaper.parallax.gl;

import javax.microedition.khronos.opengles.GL10;

import static javax.microedition.khronos.opengles.GL10.GL_PROJECTION;

/**
 * Ah yes, the classic GL utility class.
 */
public final class Utils {
	
	/**
	 * Sets an orthographic projection with dimensions matching
	 * the regular screen pixel coordinates. [0,0] is in the top left
	 * and [w,h] is in the bottom right of the screen, rather than
	 * the regular OpenGL convention of the origin being in the
	 * bottom left.
	 */
	public static void pixelProjection(GL10 gl, int w, int h) {
		gl.glViewport(0, 0, w, h);
		gl.glMatrixMode(GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(0, w, h, 0, -100, 100);
	}
	
}
