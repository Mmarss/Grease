package net.mmarss.grease.graphics;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;

import org.lwjgl.system.MemoryUtil;

import net.mmarss.grease.core.Shader;
import net.mmarss.grease.exception.GreaseException;

/**
 * A class managing 2-dimensional graphics.
 */
public class Graphics2d {
	
	/** The canvas width, in pixels. */
	private int		width	= 0;
	/** The canvas height, in pixels. */
	private int		height	= 0;
	/** Whether the canvas was resized since the last render cycle. */
	private boolean	resized	= false;
	
	/** The shader used to render these graphics. */
	private Shader shader = null;
	
	private int	triangleVboId;
	private int	triangleVaoId;
	
	/**
	 * Constructs a new 2d graphics object.
	 */
	public Graphics2d() {}
	
	/**
	 * Resizes the canvas.
	 * 
	 * @param width
	 *            the new canvas width, in pixels.
	 * @param height
	 *            the new canvas height, in pixels.
	 */
	public void resize(int width, int height) {
		
		this.width = width;
		this.height = height;
		resized = true;
	}
	
	/**
	 * Checks whether the canvas has been resized since the last render cycle.
	 * 
	 * @return <code>true</code> if the canvas has been resized, or
	 *         <code>false</code> otherwise.
	 */
	public boolean resized() {
		
		return resized;
	}
	
	/**
	 * @return the canvas width.
	 */
	public int getWidth() {
		
		return width;
	}
	
	/**
	 * @return the canvas height.
	 */
	public int getHeight() {
		
		return height;
	}
	
	/**
	 * Initializes this 2d graphics manager. This method must be called before the
	 * first render cycle in which it is used, from the window thread.
	 */
	public void init() {
		
		// Load the shader
		try {
			
			shader = new Shader();
			shader.create();
			shader.loadShaders("vertex2d.vsh", "fragment2d.fsh");
			shader.link();
			
		} catch (GreaseException e) {
			
			e.printStackTrace();
			shader = null;
			return;
		}
		
		float[] vertices = new float[] { 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f };
		FloatBuffer verticesBuffer = null;
		
		try {
			
			verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);
			verticesBuffer.put(vertices).flip();
			
			// Create and bind VAO
			triangleVaoId = glGenVertexArrays();
			glBindVertexArray(triangleVaoId);
			
			// Create and bind VBO
			triangleVboId = glGenBuffers();
			glBindBuffer(GL_ARRAY_BUFFER, triangleVboId);
			glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
			
			// Configure VBO
			glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
			
			// Unbind VBO
			glBindBuffer(GL_ARRAY_BUFFER, 0);
			
			// Unbind VAO
			glBindVertexArray(0);
			
		} finally {
			
			if (verticesBuffer != null) {
				MemoryUtil.memFree(verticesBuffer);
			}
		}
	}
	
	/**
	 * Prepares the rendering context for rendering 2d graphics through this object.
	 */
	public void preRender() {
		
		if (shader == null) {
			return;
		}
		
		// Clear the framebuffer
		glClear(GL_COLOR_BUFFER_BIT);
		
		if (resized) {
			glViewport(0, 0, width, height);
			resized = false;
		}
		
		shader.bind();
		
		glBindVertexArray(triangleVaoId);
		glEnableVertexAttribArray(0);
	}
	
	/**
	 * Renders any objects registered for this render cycle.
	 */
	public void render() {
		
		if (shader == null) {
			return;
		}
		
		glDrawArrays(GL_TRIANGLES, 0, 3);
	}
	
	/**
	 * Restores the rendering context, finishing this render cycle.
	 */
	public void postRender() {
		
		if (shader == null) {
			return;
		}
		
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		
		shader.unbind();
	}
	
	/**
	 * Cleans up any resources allocated by this graphics manager.
	 */
	public void cleanup() {
		
		if (shader != null) {
			shader.cleanup();
		}
		
		glDisableVertexAttribArray(0);
		
		// Delete VBO
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glDeleteBuffers(triangleVboId);
		
		// Delete VAO
		glBindVertexArray(0);
		glDeleteVertexArrays(triangleVaoId);
	}
}
