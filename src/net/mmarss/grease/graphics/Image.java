package net.mmarss.grease.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.stbi_failure_reason;
import static org.lwjgl.stb.STBImage.stbi_load;

import java.nio.ByteBuffer;

import net.mmarss.grease.exception.GreaseFileException;
import net.mmarss.grease.exception.GreaseInvalidArgumentException;

/**
 * Represents an image; an array of pixel data.
 */
public class Image {
	
	/** The image width, in pixels. */
	private int	width;
	/** The image height, in pixels. */
	private int	height;
	
	/**
	 * The number of color channels in the image data.
	 * The components are as follows:
	 * 1: grey
	 * 2: grey, alpha
	 * 3: red, green, blue
	 * 4: red, green, blue, alpha
	 */
	private int numChannels;
	
	/** The binary image data. */
	private ByteBuffer data;
	
	/** The OpenGL texture identifier for this image. */
	private int textureId = -1;
	
	/**
	 * Constructs a new empty image object.
	 */
	public Image() {}
	
	/**
	 * Constructs a new image object, attempting to load the specified image file.
	 * 
	 * @param filename
	 *            the file to load.
	 * @throws GreaseFileException
	 *             if the file cannot be read.
	 */
	public Image(String filename) throws GreaseFileException {
		
		load(filename);
	}
	
	/**
	 * Constructs a new blank RGBA image with the specified dimensions.
	 * 
	 * @param width
	 *            the width of the new image, in pixels.
	 * @param height
	 *            the height of the new image, in pixels.
	 */
	public Image(int width, int height) {
		
		this(width, height, 4);
	}
	
	/**
	 * Constructs a new image with the specified dimensions and number of channels.
	 * The color channels are as follows:
	 * 1: grey
	 * 2: grey, alpha
	 * 3: red, green, blue
	 * 4: red, green, blue, alpha
	 * 
	 * @param width
	 *            the width of the new image, in pixels.
	 * @param height
	 *            the height of the new image, in pixels.
	 * @param numChannels
	 *            the number of channels in the image, an integer from 1 to 4.
	 */
	public Image(int width, int height, int numChannels) {
		
		clearImage(width, height, numChannels);
	}
	
	/**
	 * Attempts to load the specified image file.
	 * 
	 * @param filename
	 *            the file to load.
	 * @throws GreaseFileException
	 *             if the file cannot be read.
	 */
	public synchronized void load(String filename) throws GreaseFileException {
		
		int[] x = new int[1];
		int[] y = new int[1];
		int[] n = new int[1];
		
		ByteBuffer bytes = stbi_load(filename, x, y, n, 0);
		if (bytes == null) {
			throw new GreaseFileException("Could not load image " + filename + ". " + stbi_failure_reason());
		}
		
		width = x[0];
		height = y[0];
		numChannels = n[0];
		data = bytes;
	}
	
	/**
	 * Clears the image, setting all of its pixel data to zero.
	 */
	public synchronized void clearImage() {
		
		data.clear();
	}
	
	/**
	 * Clears the image, setting its old pixel data to zero and creating a new pixel
	 * buffer with the specified dimensions.
	 * 
	 * @param width
	 *            the width of the image, in pixels.
	 * @param height
	 *            the height of the image, in pixels.
	 * @param numChannels
	 *            the number of color channels in the image, an integer from 1 to 4.
	 */
	public synchronized void clearImage(int width, int height, int numChannels) {
		
		if (width < 0) {
			throw new GreaseInvalidArgumentException("width", width, "Image dimensions must be non-negative.");
		}
		if (height < 0) {
			throw new GreaseInvalidArgumentException("width", width, "Image dimensions must be non-negative.");
		}
		if (numChannels < 1 || numChannels > 4) {
			throw new GreaseInvalidArgumentException("numChannels", numChannels,
					"Image must have from 1 to 4 channels.");
		}
		
		data.clear();
		this.width = width;
		this.height = height;
		this.numChannels = numChannels;
		data = ByteBuffer.allocate(width * height * numChannels);
	}
	
	/**
	 * @return the width of the image, in pixels.
	 */
	public int getWidth() {
		
		return width;
	}
	
	/**
	 * @return the height of the image, in pixels.
	 */
	public int getHeight() {
		
		return height;
	}
	
	/**
	 * @return the number of channels used by this image.
	 */
	public int getNumChannels() {
		
		return numChannels;
	}
	
	/**
	 * @return a read-only view of the raw pixel data.
	 */
	public ByteBuffer getPixelData() {
		
		return data.asReadOnlyBuffer();
	}
	
	public void generateTexture() {
		
		if (textureId == -1) {
			textureId = glGenTextures();
		}
	}
	
	public void bindTexture() {
		
		glBindTexture(GL_TEXTURE_2D, textureId);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		int format;
		if (numChannels == 1) {
			format = GL_LUMINANCE;
		} else if (numChannels == 2) {
			format = GL_LUMINANCE_ALPHA;
		} else if (numChannels == 3) {
			format = GL_RGB;
		} else {
			format = GL_RGBA;
		}
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, format, GL_UNSIGNED_BYTE, data);
	}
	
	public void unbindTexture() {
		
		glBindTexture(GL_TEXTURE_2D, 0);
	}
}
