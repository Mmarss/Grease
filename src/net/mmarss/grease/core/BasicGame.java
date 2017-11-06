package net.mmarss.grease.core;

import net.mmarss.grease.graphics.Graphics2d;
import net.mmarss.grease.input.BasicInput;

/**
 * An abstract class representing a simple game's execution life cycle. When
 * creating games using this library, you should start with a concrete subclass
 * of this class.
 */
public abstract class BasicGame {
	
	/**
	 * Initializes the game data. This method is guaranteed to be called from the
	 * window thread, and so should be used to load renderable resources. Be aware
	 * that the program remains unresponsive while this method is executing, so its
	 * execution shouldn't take more than a few seconds. For simple games, this
	 * isn't an issue.
	 */
	public abstract void init();
	
	/**
	 * Updates the game state. This method is never called before <code>init</code>,
	 * and always before a corresponding call to <code>render</code>.
	 * 
	 * @param delta
	 *            the time in milliseconds since the last time this method was
	 *            called. On the first call to update, this is the number of
	 *            milliseconds since initialization was completed.
	 * @param input
	 *            an object representing the user input to the application since the
	 *            last call to update. This includes the current state of the
	 *            computer peripherals.
	 */
	public abstract void update(double delta, BasicInput input);
	
	/**
	 * Renders the game state. This method is called each frame after a
	 * corresponding <code>update</code>. Drawing should be done through the
	 * graphics object.
	 * 
	 * @param g
	 *            the graphics object. This can be used to render 2-dimensional
	 *            objects to the window.
	 */
	public abstract void render(Graphics2d g);
	
	/**
	 * Cleans up any created or locked resources. This will be called after the
	 * window is closed. It is called from the window thread, and once it is called,
	 * <code>update</code> and <code>render</code> are guaranteed to never be called
	 * again.
	 */
	public abstract void cleanup();
}
