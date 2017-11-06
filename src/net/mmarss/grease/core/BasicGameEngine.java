package net.mmarss.grease.core;

import net.mmarss.grease.exception.GreaseInvalidArgumentException;
import net.mmarss.grease.graphics.Graphics2d;

/**
 * A game engine sitting behind a {@link BasicGame}. This class manages the
 * execution thread, the operating system window, user input and the game
 * renderer.
 */
public class BasicGameEngine {
	
	/** The window title used by the game if no other title is specified. */
	private static final String	GAME_DEFAULT_WINDOW_TITLE	= "Basic Grease Game";
	/**
	 * The initial window width used by the game if no other dimensions are
	 * specified.
	 */
	private static final int	GAME_DEFAULT_WINDOW_WIDTH	= 800;
	/**
	 * The initial window height used by the game if no other dimensions are
	 * specified.
	 */
	private static final int	GAME_DEFAULT_WINDOW_HEIGHT	= 600;
	
	/**
	 * Constructs a new <code>BasicGameEngine</code>, running a
	 * <code>BasicGame</code>. This constructor uses default values for the window
	 * dimensions and window title.
	 * 
	 * @param game
	 *            the game to be executed by this <code>BasicGameEngine</code>.
	 */
	public BasicGameEngine(BasicGame game) {
		
		this(game, GAME_DEFAULT_WINDOW_TITLE, GAME_DEFAULT_WINDOW_WIDTH, GAME_DEFAULT_WINDOW_HEIGHT);
	}
	
	/**
	 * Constructs a new <code>BasicGameEngine</code>, running a
	 * <code>BasicGame</code>. This constructor uses default values for the window
	 * dimensions.
	 * 
	 * @param game
	 *            the game to be executed by this <code>BasicGameEngine</code>.
	 * @param title
	 *            the title of the window, shown by the operating system in the
	 *            window title bar.
	 */
	public BasicGameEngine(BasicGame game, String title) {
		
		this(game, title, GAME_DEFAULT_WINDOW_WIDTH, GAME_DEFAULT_WINDOW_HEIGHT);
	}
	
	/**
	 * Constructs a new <code>BasicGameEngine</code>, running a
	 * <code>BasicGame</code>.
	 * 
	 * @param game
	 *            the game to be executed by this <code>BasicGameEngine</code>.
	 * @param title
	 *            the title of the window, shown by the operating system in the
	 *            window title bar.
	 * @param width
	 *            the width of the window, in pixels.
	 * @param height
	 *            the height of the window, in pixels.
	 */
	public BasicGameEngine(BasicGame game, String title, int width, int height) {
		
		// Check argument validity
		if (game == null) {
			throw new GreaseInvalidArgumentException("game", null);
		}
		
		if (width <= 0) {
			throw new GreaseInvalidArgumentException("width", width, "Window dimensions must be positive.");
		}
		
		if (height <= 0) {
			throw new GreaseInvalidArgumentException("height", height, "Window dimensions must be positive.");
		}
		
		createWindow(game, title, width, height);
	}
	
	/**
	 * Starts this game engine. This method will not return until the game is
	 * closed.
	 */
	public void run() {
		
		Window.getInstance().run();
	}
	
	/**
	 * Tells this game engine to halt. This may not occur immediately, but will soon
	 * after this method is called. This method is thread-safe.
	 */
	public void stop() {
		
		Window.getInstance().close();
	}
	
	/**
	 * Creates the game window.
	 * 
	 * @param game
	 *            the game to be executed by this <code>BasicGameEngine</code>.
	 * @param title
	 *            the title of the window, shown by the operating system in the
	 *            window title bar.
	 * @param width
	 *            the width of the window, in pixels.
	 * @param height
	 *            the height of the window, in pixels.
	 */
	private void createWindow(BasicGame game, String title, int width, int height) {
		
		Graphics2d graphics2d = new Graphics2d() {
			
			@Override
			public void render() {
				
				game.render(this);
				super.render();
			}
		};
		
		Timer frameTimer = new Timer();
		
		// Create the window
		Window.createInstance(width, height, title, graphics2d, () -> {
			// Initialize any resources
			game.init();
			frameTimer.start();
		}, () -> {
			// Update the game state
			game.update(frameTimer.restart() * 1000, Window.getInstance().getInputManager());
		}, () -> {
			// Clean up any resources
			game.cleanup();
		});
	}
}
