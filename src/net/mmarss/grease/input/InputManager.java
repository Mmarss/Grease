package net.mmarss.grease.input;

import java.util.EnumSet;
import java.util.Set;

/**
 * Manages the input state.
 */
public class InputManager implements BasicInput {
	
	/**
	 * A collection of currently pressed keys.
	 */
	private Set< Key > keysDown;
	
	public InputManager() {
		
		keysDown = EnumSet.noneOf(Key.class);
	}
	
	@Override
	public boolean isKeyDown(Key key) {
		
		return keysDown.contains(key);
	}
	
	@Override
	public KeyState getKeyState(Key key) {
		
		return isKeyDown(key) ? KeyState.KEY_DOWN : KeyState.KEY_UP;
	}
	
	/**
	 * Registers a change to the input state with this input manager.
	 * 
	 * @param key
	 *            the key on which an action has been triggered.
	 * @param keyAction
	 *            the action that was triggered for a particular key.
	 * @param modifiers
	 *            the modifier keys that are currently pressed.
	 */
	public void keyCallback(Key key, KeyAction keyAction, Set< KeyModifier > modifiers) {
		
		switch (keyAction) {
		case KEY_PRESSED:
			keysDown.add(key);
			break;
		case KEY_RELEASED:
			keysDown.remove(key);
			break;
		case KEY_REPEATED:
			keysDown.add(key); // In case the KEY_PRESSED action was sent to a different window
			break;
		}
	}
}
