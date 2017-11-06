package net.mmarss.grease.input;

import static org.lwjgl.glfw.GLFW.GLFW_MOD_ALT;
import static org.lwjgl.glfw.GLFW.GLFW_MOD_CONTROL;
import static org.lwjgl.glfw.GLFW.GLFW_MOD_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_MOD_SUPER;

import java.util.EnumSet;
import java.util.Set;

/**
 * An enumeration of keyboard modifier keys.
 */
public enum KeyModifier {
	
	/** The shift key. */
	SHIFT,
	/** The ctrl key. */
	CONTROL,
	/** The alt/command key. */
	ALT,
	/** The super/windows key. */
	SUPER;
	
	/**
	 * Creates a set of modifier keys registered in the given GLFW bit field.
	 * 
	 * @param modifierBitField
	 *            the bit field of currently pressed modifier keys.
	 * @return a set of modifier keys that are currently pressed.
	 */
	public static Set< KeyModifier > fromBitField(int modifierBitField) {
		
		EnumSet< KeyModifier > mods = EnumSet.noneOf(KeyModifier.class);
		
		if ((modifierBitField & GLFW_MOD_SHIFT) != 0) {
			mods.add(SHIFT);
		}
		if ((modifierBitField & GLFW_MOD_CONTROL) != 0) {
			mods.add(CONTROL);
		}
		if ((modifierBitField & GLFW_MOD_ALT) != 0) {
			mods.add(ALT);
		}
		if ((modifierBitField & GLFW_MOD_SUPER) != 0) {
			mods.add(SUPER);
		}
		
		return mods;
	}
}
