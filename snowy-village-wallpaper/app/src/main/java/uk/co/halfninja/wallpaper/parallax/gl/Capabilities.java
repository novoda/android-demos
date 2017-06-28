package uk.co.halfninja.wallpaper.parallax.gl;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

/**
 * Calculates various capabilities of your OpenGL implementation and/or
 * your hardware.
 */
public class Capabilities {
	private boolean nonPowerOfTwoTextures;

	public Capabilities() {}
	
	public Capabilities(GL10 gl) {
		reload(gl);
	}
	
	public void reload(GL10 gl) {
		String extensions = gl.glGetString(GL10.GL_EXTENSIONS);
		
		Log.d("GLCapabilities", extensions);
		
		nonPowerOfTwoTextures = extensions.contains("ARB_texture_non_power_of_two");
	}
	
	public boolean supportsNonPowerOfTwoTextures() {
		return nonPowerOfTwoTextures;
	}
}
