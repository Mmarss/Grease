package net.mmarss.grease.input;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_REPEAT;

/**
 * An enumeration of key actions.
 */
public enum KeyAction {
	
	/** A key has changed state from up to down. */
	KEY_PRESSED,
	/** A key has changed state from down to up. */
	KEY_RELEASED,
	/**
	 * A key is still pressed, and the operating system has therefore registered the
	 * keystroke again.
	 */
	KEY_REPEATED;
	
	/**
	 * Converts a GLFW action code into its corresponding enum element.
	 * 
	 * @param code
	 *            the GLFW action code representing a particular action.
	 * @return the key action corresponding to the specified code, or
	 *         <code>null</code> if the code is invalid.
	 */
	public static KeyAction fromCode(int code) {
		
		switch (code) {
		case GLFW_PRESS:
			return KEY_PRESSED;
		case GLFW_RELEASE:
			return KEY_RELEASED;
		case GLFW_REPEAT:
			return KEY_REPEATED;
		default:
			return null;
		}
	}
}
