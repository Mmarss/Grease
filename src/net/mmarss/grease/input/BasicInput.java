package net.mmarss.grease.input;

/**
 * Provides only the most essential methods of an input manager.
 */
public interface BasicInput {
	
	/**
	 * Checks whether the specified key is currently pressed.
	 * 
	 * @param key
	 *            the key to check the state for.
	 * @return <code>true</code> if the specified key is currently down.
	 */
	public boolean isKeyDown(Key key);
	
	/**
	 * Gets the specified key's current state.
	 * 
	 * @param key
	 *            the key to check the state for.
	 * @return a key state corresponding to the specified key's state.
	 */
	public KeyState getKeyState(Key key);
}
