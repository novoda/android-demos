package uk.co.halfninja.wallpaper.parallax.gl;

import static javax.microedition.khronos.opengles.GL10.GL_FLOAT;
import static javax.microedition.khronos.opengles.GL10.GL_MODELVIEW;
import static javax.microedition.khronos.opengles.GL10.GL_TEXTURE_COORD_ARRAY;
import static javax.microedition.khronos.opengles.GL10.GL_TRIANGLE_STRIP;
import static javax.microedition.khronos.opengles.GL10.GL_VERTEX_ARRAY;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Rectangle for OpenGL. Maintains static buffers, so
 * multiple quads will share a buffer.
 */
public class Quad {
	private static final int SIZEOF_FLOAT = 4;
	private static final int SIZEOF_SHORT = 2;
	private static FloatBuffer vertexBuffer;
	private static ShortBuffer indexBuffer;
	private static FloatBuffer defaultTexBuffer;	// buffer holding the texture coordinates
	
	static {
		float vertices[] = {
		      0f,  0f,  // 0, Top Left
		      0f, 1f,  // 1, Bottom Left
		      1f,  0f,  // 3, Top Right
		      1f, 1f,  // 2, Bottom Right
		};
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * SIZEOF_FLOAT);
		byteBuffer.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuffer.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		
		defaultTexBuffer = allocateTextureBuffer(1f, 1f);
	}
	
	/**
	 * Allocate a texture buffer with the given values replacing 1.
	 * e.g. if x is 0.5, then the texture will be twice the normal width.
	 */
	private static FloatBuffer allocateTextureBuffer(float u, float v) {
		float texturepoints[] = {
			0f, 0f, 
			0f, v, 
			u, 0, 
			u, v,
		};
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(texturepoints.length * SIZEOF_FLOAT);
		byteBuffer.order(ByteOrder.nativeOrder());
		FloatBuffer texBuffer = byteBuffer.asFloatBuffer();
		texBuffer.put(texturepoints);
		texBuffer.position(0);
		return texBuffer;
	}
	
	private FloatBuffer textureBuffer = defaultTexBuffer;
	private Texture texture;
	private float x;
	private float y;
	private float width;
	private float height;
	
	public void draw(GL10 gl) {
		gl.glMatrixMode( GL_MODELVIEW );
        gl.glLoadIdentity();
        gl.glTranslatef(x, y, 0f);
        gl.glScalef(width,height,1f);        
		
		gl.glEnableClientState(GL_VERTEX_ARRAY);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glVertexPointer(2, GL_FLOAT, 0, vertexBuffer);
		if (texture != null) {
			gl.glEnableClientState(GL_TEXTURE_COORD_ARRAY);
			texture.bind(gl);
			gl.glTexCoordPointer(2, GL_FLOAT, 0, textureBuffer);
		}
		gl.glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
		//gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4, GL10.GL_UNSIGNED_SHORT, indexBuffer);
		gl.glDisableClientState(GL_VERTEX_ARRAY);
		if (texture != null) {
			gl.glDisableClientState(GL_TEXTURE_COORD_ARRAY);
		}
	}
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
		if (!texture.isMatchesDimensions()) {
			textureBuffer = allocateTextureBuffer(texture.getU(), texture.getV());
		} else {
			textureBuffer = defaultTexBuffer;
		}
		this.width = texture.getBitmapWidth();
		this.height = texture.getBitmapHeight();
	}
}
