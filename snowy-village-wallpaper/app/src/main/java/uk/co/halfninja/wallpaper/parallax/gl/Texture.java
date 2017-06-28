package uk.co.halfninja.wallpaper.parallax.gl;

import javax.microedition.khronos.opengles.GL10;

public class Texture {
	public final int id;
	private int texWidth, texHeight, bitmapWidth, bitmapHeight;
	private boolean matchesDimensions;
	public Texture(int id) {
		this.id = id;
	}
	public void bind(GL10 gl) {
		gl.glBindTexture(GL10.GL_TEXTURE_2D, id);
	}
	public int getTexWidth() {
		return texWidth;
	}
	public void setTexWidth(int texWidth) {
		this.texWidth = texWidth;
	}
	public int getTexHeight() {
		return texHeight;
	}
	public void setTexHeight(int texHeight) {
		this.texHeight = texHeight;
	}
	public int getBitmapWidth() {
		return bitmapWidth;
	}
	public void setBitmapWidth(int bitmapWidth) {
		this.bitmapWidth = bitmapWidth;
	}
	public int getBitmapHeight() {
		return bitmapHeight;
	}
	public void setBitmapHeight(int bitmapHeight) {
		this.bitmapHeight = bitmapHeight;
	}
	public boolean isMatchesDimensions() {
		return bitmapHeight==texHeight && bitmapWidth==texWidth;
	}
	public float getU() {
		if (bitmapWidth==texWidth) return 1f;
		return ((float)bitmapWidth/texWidth);
	}
	public float getV() {
		if (bitmapHeight==texHeight) return 1f;
		return ((float)bitmapHeight/texHeight);
	}
	public String toString() {
		return String.format("Texture[id=%d,size=(%d,%d),bitmapsize=(%d,%d)",
				id, texWidth, texHeight, bitmapWidth, bitmapHeight
				);
				
	}
}
