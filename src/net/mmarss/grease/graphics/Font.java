package net.mmarss.grease.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.stbi_failure_reason;
import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.stb.STBTruetype.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTTPackContext;
import org.lwjgl.stb.STBTTPackedchar;
import org.lwjgl.system.MemoryStack;

import net.mmarss.grease.core.GreaseUtil;
import net.mmarss.grease.exception.GreaseFileException;
import net.mmarss.grease.exception.GreaseRuntimeException;
import net.mmarss.grease.geom2d.Rect;
import net.mmarss.grease.geom2d.Rectc;

public class Font {
	
	private static final String	DEFAULT_FONT_FILE		= "../res/Roboto24.png";
	private static final int	DEFAULT_BITMAP_WIDTH	= 512;
	private static final int	DEFAULT_BITMAP_HEIGHT	= 512;
	private static final String	DEFAULT_FONT_CHARS		= "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!№;%:?*()_+-=.,/|\\\"'@#$^&{}[]";
	
	private Map< Character, CharacterMetrics > metrics;
	
	private int textureId;
	
	private STBTTPackedchar.Buffer charData;
	
	public class CharacterMetrics {
		
		public final char	character;
		public final Rectc	texRect;
		public final float	advance;
		public final Rectc	vertRect;
		
		private CharacterMetrics(char character, Rectc texRect, float advance, Rectc vertRect) {
			
			this.character = character;
			this.texRect = texRect;
			this.advance = advance;
			this.vertRect = vertRect;
		}
	}
	
	public Font() throws GreaseFileException {
		
		this(DEFAULT_FONT_FILE);
	}
	
	public Font(String filename) throws GreaseFileException {
		
		load(filename);
	}
	
	private void load(String filename) throws GreaseFileException {
		
		String extension = filename.substring(filename.lastIndexOf('.') + 1, filename.length() - 1);
		switch (extension) {
		case "ttf":
			loadTTF(filename);
			break;
		case "png":
		case "bmp":
			loadBitmap(filename);
			break;
		}
	}
	
	private void loadBitmap(String filename) throws GreaseFileException {
		
		// Load bitmap
		int[] x = new int[1];
		int[] y = new int[1];
		int[] n = new int[1];
		
		ByteBuffer bytes = stbi_load(filename, x, y, n, 0);
		if (bytes == null) {
			throw new GreaseFileException("Could not load image " + filename + ". " + stbi_failure_reason());
		}
		
		int width = x[0];
		int height = y[0];
		int numChannels = n[0];
		ByteBuffer data = bytes;
		
		// Generate texture
		if (textureId == -1) {
			textureId = glGenTextures();
		}
		
		glBindTexture(GL_TEXTURE_2D, textureId);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		
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
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_LUMINANCE_ALPHA, width, height, 0, format, GL_UNSIGNED_BYTE, data);
		
		glBindTexture(GL_TEXTURE_2D, 0);
		
		// Generate character map
		String mapFilename = filename.substring(0, filename.lastIndexOf('.')) + "txt";
		metrics = new HashMap<>();
		try (BufferedReader br = new BufferedReader(new FileReader(mapFilename))) {
			
			String line;
			while ((line = br.readLine()) != null) {
				String[] spec = line.split(" ");
				switch (spec[0]) {
				case "info":
					break;
				case "common":
					break;
				case "page":
					break;
				case "chars":
					break;
				case "char":
					char character = '\0';
					int texX = 0, texY = 0, texWidth = 0, texHeight = 0;
					int xOffset = 0, yOffset = 0;
					float advance = 0f;
					for (String s : spec) {
						String[] item = s.split("=");
						switch (item[0]) {
						case "id":
							character = (char) Integer.parseInt(item[1]);
							break;
						case "xoffset":
							xOffset = Integer.parseInt(item[1]);
						case "x":
							texX += Integer.parseInt(item[1]);
							break;
						case "yoffset":
							yOffset = Integer.parseInt(item[1]);
						case "y":
							texY += Integer.parseInt(item[1]);
							break;
						case "width":
							texWidth = Integer.parseInt(item[1]);
							break;
						case "height":
							texHeight = Integer.parseInt(item[1]);
							break;
						case "xadvance":
							advance = Float.parseFloat(item[1]);
							break;
						}
					}
					metrics.put(character,
							new CharacterMetrics(character,
									new Rect(texX / width, texY / height, texWidth / width, texHeight / height),
									advance, new Rect(xOffset, yOffset, width, height)));
					break;
				}
			}
			
		} catch (IOException e) {
			throw new GreaseFileException("Could not read file " + mapFilename + ": " + e.getMessage());
		}
	}
	
	private void loadTTF(String filename) throws GreaseFileException {
		
		loadTTF(filename, 24.0f);
	}
	
	private void loadTTF(String filename, float size) throws GreaseFileException {
		
		textureId = glGenTextures();
		
		ByteBuffer ttf = GreaseUtil.loadBinaryResource(filename);
		STBTTFontinfo fontInfo = STBTTFontinfo.create();
		if (!stbtt_InitFont(fontInfo, ttf)) {
			throw new GreaseRuntimeException("Could not initialize font information");
		}
		
		try (MemoryStack stack = MemoryStack.stackPush()) {
			
			IntBuffer ascentBuffer = stack.mallocInt(1);
			IntBuffer descentBuffer = stack.mallocInt(1);
			IntBuffer lineSpacingBuffer = stack.mallocInt(1);
			
			stbtt_GetFontVMetrics(fontInfo, ascentBuffer, descentBuffer, lineSpacingBuffer);
			
			int ascent = ascentBuffer.get(0);
			int descent = descentBuffer.get(0);
			int lineSpacing = lineSpacingBuffer.get(0);
		}
		
		ByteBuffer bitmap = ByteBuffer.allocate(DEFAULT_BITMAP_WIDTH * DEFAULT_BITMAP_HEIGHT);
		
		try (STBTTPackContext packContext = STBTTPackContext.malloc()) {
			stbtt_PackBegin(packContext, bitmap, DEFAULT_BITMAP_WIDTH, DEFAULT_BITMAP_HEIGHT, 0, 1);
			stbtt_PackSetOversampling(packContext, 1, 1); // No oversampling; 1x1
			try (MemoryStack stack = MemoryStack.stackPush()) {
				charData = STBTTPackedchar.malloc(50);
				charData.position(' ');
				charData.limit('~');
				stbtt_PackFontRange(packContext, ttf, 0, size, ' ', charData);
				charData.clear();
			}
			stbtt_PackEnd(packContext);
		}
		
		glBindTexture(GL_TEXTURE_2D, textureId);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_ALPHA, DEFAULT_BITMAP_WIDTH, DEFAULT_BITMAP_HEIGHT, 0, GL_ALPHA,
				GL_UNSIGNED_BYTE, bitmap);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		
		metrics = new HashMap<>();
		for (char c : DEFAULT_FONT_CHARS.toCharArray()) {
			
			Rect texRect, vertRect;
			try (MemoryStack stack = MemoryStack.stackPush()) {
				FloatBuffer xBuffer = stack.mallocFloat(1).put(0);
				FloatBuffer yBuffer = stack.mallocFloat(1).put(0);
				STBTTAlignedQuad quad = STBTTAlignedQuad.malloc();
				charData.position(0);
				stbtt_GetPackedQuad(charData, DEFAULT_BITMAP_WIDTH, DEFAULT_BITMAP_HEIGHT, c, xBuffer, yBuffer, quad,
						false);
				
				texRect = new Rect(quad.s0(), quad.t0(), quad.s1(), quad.t1());
				vertRect = new Rect(quad.x0(), quad.y0(), quad.x1(), quad.y1());
			}
			metrics.put(c, new CharacterMetrics(c, texRect, vertRect.getWidth(), vertRect));
		}
	}
	
	public CharacterMetrics getCharacterMetrics(char character) {
		
		return metrics.getOrDefault(character, metrics.getOrDefault('#', metrics.getOrDefault('0', metrics.get('X'))));
		
	}
	
	public void bindTexture() {
		
		glBindTexture(GL_TEXTURE_2D, textureId);
	}
	
	public void unbindTexture() {
		
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public void cleanup() {
		
	}
}
