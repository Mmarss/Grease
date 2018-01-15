package net.mmarss.grease.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import net.mmarss.grease.core.Shader;
import net.mmarss.grease.exception.GreaseFileException;
import net.mmarss.grease.exception.GreaseShaderException;
import net.mmarss.grease.exception.GreaseShaderUniformException;

/**
 * A class managing 2-dimensional graphics.
 */
public class Graphics2d extends Renderer {
	
	/** The shader used to render these graphics. */
	private Shader shader = null;
	
	private Font defaultFont = null;
	
	private int	rectVertVboId;
	private int	rectTexVboId;
	private int	rectEboId;
	private int	rectVaoId;
	
	/**
	 * Constructs a new 2d graphics object.
	 */
	public Graphics2d() {}
	
	/**
	 * Initializes this 2d graphics manager. This method must be called before the
	 * first render cycle in which it is used, from the window thread.
	 */
	@Override
	public void init() {
		
		// Load the shader
		try {
			
			shader = new Shader();
			shader.create();
			shader.loadShaders("vertex2d.vsh", "fragment2d.fsh");
			shader.link();
			
			shader.bind();
			
			shader.createUniform("color", Vector4f.class);
			
			shader.createUniform("model", Matrix4f.class);
			shader.createUniform("view", Matrix4f.class);
			shader.createUniform("projection", Matrix4f.class);
			
			shader.createUniform("texMatrix", Matrix4f.class);
			
			shader.createUniform("useTexture", boolean.class);
			shader.createUniform("texImage", int.class);
			
			shader.setUniform("projection", new Matrix4f().ortho2D(0, getWidth(), getHeight(), 0));
			shader.setUniform("useTexture", false);
			
		} catch (GreaseShaderException | GreaseFileException e) {
			
			e.printStackTrace();
			shader = null;
			return;
		}
		
		float[] vertices = new float[] { 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f };
		float[] texUV = new float[] { 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f };
		short[] indices = new short[] { 0, 1, 2, 3, 2, 1 };
		
		// Create and bind VAO
		rectVaoId = glGenVertexArrays();
		glBindVertexArray(rectVaoId);
		
		// Create position VBO
		rectVertVboId = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, rectVertVboId);
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(0);
		
		// Create texture VBO
		rectTexVboId = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, rectTexVboId);
		glBufferData(GL_ARRAY_BUFFER, texUV, GL_STATIC_DRAW);
		glVertexAttribPointer(1, 2, GL_FLOAT, true, 0, 0);
		glEnableVertexAttribArray(1);
		
		// Create and bind EBO
		rectEboId = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, rectEboId);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
		
		// Unbind VBO
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		// Unbind EBO
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		
		// Unbind VAO
		glBindVertexArray(0);
		
		shader.unbind();
		
	}
	
	/**
	 * Prepares the rendering context for rendering 2d graphics through this object.
	 */
	@Override
	public void preRender() {
		
		if (shader == null) {
			return;
		}
		
		// Clear the framebuffer
		glClear(GL_COLOR_BUFFER_BIT);
		
		shader.bind();
		
		if (wasResized()) {
			
			glViewport(0, 0, getWidth(), getHeight());
			
			try {
				shader.setUniform("projection", new Matrix4f().ortho2D(0, getWidth(), getHeight(), 0));
			} catch (GreaseShaderUniformException e) {
				e.printStackTrace();
				shader.cleanup();
				shader = null; // Stop rendering with this object.
				return;
			}
		}
		
		glEnable(GL_BLEND);
		
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		glBindVertexArray(rectVaoId);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, rectEboId);
		
		setColor(0.102f, 0.345f, 0.000f, 1.0f);
	}
	
	/**
	 * Renders any objects registered for this render cycle.
	 */
	@Override
	public void render() {
		
		if (shader == null) {
			return;
		}
	}
	
	/**
	 * Restores the rendering context, finishing this render cycle.
	 */
	@Override
	public void postRender() {
		
		if (shader == null) {
			return;
		}
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
		
		glDisable(GL_BLEND);
		
		shader.unbind();
	}
	
	/**
	 * Cleans up any resources allocated by this graphics manager.
	 */
	@Override
	public void cleanup() {
		
		if (shader != null) {
			shader.cleanup();
		}
		
		// Delete EBO
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glDeleteBuffers(rectEboId);
		
		// Delete VBO
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glDeleteBuffers(rectVertVboId);
		
		// Delete VAO
		glBindVertexArray(0);
		glDeleteVertexArrays(rectVaoId);
	}
	
	/**
	 * Sets the rendering color to the specified value.
	 * 
	 * @param r
	 *            the red component of the color.
	 * @param g
	 *            the green component of the color.
	 * @param b
	 *            the blue component of the color.
	 */
	public void setColor(float r, float g, float b) {
		
		setColor(r, g, b, 1.0f);
	}
	
	/**
	 * Sets the rendering color to the specified value.
	 * 
	 * @param r
	 *            the red component of the color.
	 * @param g
	 *            the green component of the color.
	 * @param b
	 *            the blue component of the color.
	 * @param a
	 *            the alpha component of the color.
	 */
	public void setColor(float r, float g, float b, float a) {
		
		try {
			shader.setUniform("color", new Vector4f(r, g, b, a));
		} catch (GreaseShaderUniformException e) { // Will only happen if the shader is changed.
			e.printStackTrace();
			return;
		}
	}
	
	/**
	 * Draws an axis-aligned rectangle from (x0, y0) to (x1, y1).
	 * 
	 * @param x0
	 *            the x-coordinate of the first corner.
	 * @param y0
	 *            the y-coordinate of the first corner.
	 * @param x1
	 *            the x-coordinate of the second corner.
	 * @param y1
	 *            the y-coordinate of the second corner.
	 * 
	 */
	public void drawRect(float x0, float y0, float x1, float y1) {
		
		try {
			
			shader.setUniform("model", new Matrix4f().scaling(x1 - x0, y1 - y0, 1f).translateLocal(x0, y0, 0f));
			
		} catch (GreaseShaderUniformException e) { // Will only happen if the shader is changed.
			e.printStackTrace();
			return;
		}
		
		glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_SHORT, 0);
	}
	
	/**
	 * Draws the image to the screen, with the normalized image transformed by the
	 * given matrix.
	 * 
	 * @param modelMatrix
	 *            the model matrix used to transform the normalized image.
	 */
	private void drawImage(Image image, Matrix4f modelMatrix) {
		
		image.generateTexture();
		image.bindTexture();
		
		try {
			
			shader.setUniform("useTexture", true);
			shader.setUniform("texImage", 0);
			shader.setUniform("model", modelMatrix);
			
		} catch (GreaseShaderUniformException e) { // Will only happen if the shader is changed.
			e.printStackTrace();
			return;
		}
		
		glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_SHORT, 0);
		
		try {
			
			shader.setUniform("useTexture", false);
		} catch (GreaseShaderUniformException e) { // Will only happen if the shader is changed.
			e.printStackTrace();
			return;
		}
		
		image.unbindTexture();
	}
	
	/**
	 * Draws the image to the screen at the specified coordinates.
	 * 
	 * @param image
	 *            the image to draw.
	 * @param x
	 *            the x-coordinate of the top-left image corner.
	 * @param y
	 *            the y-coordinate of the top-left image corner.
	 */
	public void drawImage(Image image, float x, float y) {
		
		drawImage(image, new Matrix4f().translation(x, y, 0f).scale(image.getWidth(), image.getHeight(), 1f));
	}
	
	/**
	 * Draws the image to the screen, centered on the specified coordinates.
	 * 
	 * @param image
	 *            the image to draw.
	 * @param x
	 *            the x-coordinate of the image center.
	 * @param y
	 *            the y-coordinate of the image center.
	 * 
	 */
	public void drawImageCentered(Image image, float x, float y) {
		
		drawImage(image, x - image.getWidth() / 2, y - image.getHeight() / 2);
	}
	
	/**
	 * Draws the image to the screen, centered on the specified coordinates and
	 * rotated by the given angle.
	 * 
	 * @param image
	 *            the image to draw.
	 * @param x
	 *            the x-coordinate of the image center.
	 * @param y
	 *            the y-coordinate of the image center.
	 * @param angle
	 *            the angle to rotate the image by, in radians.
	 */
	public void drawImageRotated(Image image, float x, float y, float angle) {
		
		drawImage(image, new Matrix4f().translate(x, y, 0f).rotate(angle, 0f, 0f, -1f)
				.scale(image.getWidth(), image.getHeight(), 1f).translate(-0.5f, -0.5f, 0f));
	}
	
	/**
	 * Draws the image to the screen at the specified coordinates, scaled by the
	 * given factor.
	 * 
	 * @param image
	 *            the image to draw.
	 * @param x
	 *            the x-coordinate of the top-left image corner.
	 * @param y
	 *            the y-coordinate of the top-left image corner.
	 * @param scale
	 *            the scaling factor to apply to the image.
	 */
	public void drawImageScaled(Image image, float x, float y, float scale) {
		
		drawImageScaled(image, x, y, scale, scale);
	}
	
	/**
	 * Draws the image to the screen at the specified coordinates, scaled by the
	 * given factors.
	 * 
	 * @param image
	 *            the image to draw.
	 * @param x
	 *            the x-coordinate of the top-left image corner.
	 * @param y
	 *            the y-coordinate of the top-left image corner.
	 * @param scalex
	 *            the scaling factor to apply to the image in the horizontal
	 *            direction.
	 * @param scaley
	 *            the scaling factor to apply to the image in the vertical
	 *            direction.
	 */
	public void drawImageScaled(Image image, float x, float y, float scalex, float scaley) {
		
		drawImage(image,
				new Matrix4f().translation(x, y, 0f).scale(image.getWidth() * scalex, image.getHeight() * scaley, 1f));
	}
	
	/**
	 * Draws the image in the axis-aligned rectangle from (x0, y0) to (x1, y1).
	 * 
	 * @param image
	 *            the image to draw.
	 * @param x0
	 *            the x-coordinate of the first corner.
	 * @param y0
	 *            the y-coordinate of the first corner.
	 * @param x1
	 *            the x-coordinate of the second corner.
	 * @param y1
	 *            the y-coordinate of the second corner.
	 * 
	 */
	public void drawImageRect(Image image, float x0, float y0, float x1, float y1) {
		
		drawImage(image, new Matrix4f().translation(x0, y0, 0f).scale(x1 - x0, y1 - y0, 1f));
	}
	
	public void drawString(String string, float x, float y) {
		
		drawString(string, defaultFont, x, y);
	}
	
	public void drawString(String string, Font font, float x, float y) {
		
		font.bindTexture();
		
		try {
			
			shader.setUniform("useTexture", true);
			shader.setUniform("texImage", 0);
			
		} catch (GreaseShaderUniformException e) { // Will only happen if the shader is changed.
			e.printStackTrace();
			return;
		}
		
		float xAdvance = 0.0f;
		for (int i = 0; i < string.length(); i++) {
			Font.CharacterMetrics metrics = font.getCharacterMetrics(string.charAt(i));
			
			try {
				shader.setUniform("model",
						new Matrix4f().translation(x + xAdvance + metrics.vertRect.getMinX(),
								y + metrics.vertRect.getMinY(), 0f)
								.scale(metrics.vertRect.getWidth(), metrics.vertRect.getHeight(), 1f));
				shader.setUniform("texMatrix",
						new Matrix4f().translation(metrics.texRect.getMinX(), metrics.texRect.getMinY(), 0.0f)
								.scale(metrics.texRect.getWidth(), metrics.texRect.getHeight(), 1f));
				xAdvance += metrics.advance;
				
			} catch (GreaseShaderUniformException e) { // Will only happen if the shader is changed.
				e.printStackTrace();
				return;
			}
			
			glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_SHORT, 0);
		}
		try {
			
			shader.setUniform("texMatrix", new Matrix4f());
			shader.setUniform("useTexture", false);
		} catch (GreaseShaderUniformException e) { // Will only happen if the shader is changed.
			e.printStackTrace();
			return;
		}
		
		font.unbindTexture();
	}
}
