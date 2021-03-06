package net.mmarss.grease.input;

import static org.lwjgl.glfw.GLFW.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * An enumeration of keyboard keys.
 */
public enum Key {
	
	KEY_UNKNOWN(-1),
	KEY_A(GLFW_KEY_A),
	KEY_D(GLFW_KEY_D),
	KEY_R(GLFW_KEY_R),
	KEY_S(GLFW_KEY_S),
	KEY_W(GLFW_KEY_W),
	KEY_ESCAPE(GLFW_KEY_ESCAPE),
	KEY_RIGHT(GLFW_KEY_RIGHT),
	KEY_LEFT(GLFW_KEY_LEFT),
	KEY_DOWN(GLFW_KEY_DOWN),
	KEY_UP(GLFW_KEY_UP),
	KEY_LEFT_SUPER(GLFW_KEY_LEFT_SUPER);
	
	/** The GLFW key ID for this key. Corresponds to the key's virtual code. */
	private final int ID;
	
	/** A map from virtual key codes to keys, used for key lookups. */
	private static final Map< Integer, Key > keyIdMap;
	static {
		Map< Integer, Key > map = new HashMap<>();
		for (Key key : Key.values()) {
			map.put(key.ID, key);
		}
		keyIdMap = Collections.unmodifiableMap(map);
	}
	
	/**
	 * Constructs a key constant with the specified ID.
	 * 
	 * @param id
	 *            the key's virtual key code.
	 */
	private Key(int id) {
		
		ID = id;
	}
	
	/**
	 * @return this key's current physical key code.
	 */
	public int getScancode() {
		
		if (this == KEY_UNKNOWN) {
			return -1;
		}
		
		return glfwGetKeyScancode(ID);
	}
	
	/**
	 * Gets the key corresponding to the specified virtual key code.
	 * 
	 * @param keyId
	 *            the ID of the key to get.
	 * @return the key corresponding to the given key ID.
	 */
	public static Key fromId(int keyId) {
		
		return keyIdMap.getOrDefault(keyId, KEY_UNKNOWN);
	}
}
