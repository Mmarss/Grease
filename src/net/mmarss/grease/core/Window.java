package net.mmarss.grease.core;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_FORWARD_COMPAT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwHideWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import net.mmarss.grease.exception.GreaseException;
import net.mmarss.grease.exception.GreaseRuntimeException;
import net.mmarss.grease.exception.GreaseSystemException;
import net.mmarss.grease.exception.GreaseWindowException;
import net.mmarss.grease.input.InputManager;
import net.mmarss.grease.input.Key;
import net.mmarss.grease.input.KeyAction;
import net.mmarss.grease.input.KeyModifier;

/**
 * A class managing a single window. This library does not support creating
 * multiple windows, so this class is implemented using the singleton pattern.
 */
public class Window {
	
	/** The number of milliseconds between input polls. */
	private static final int	INPUT_POLL_RATE		= 10;
	/** The target number of frames per second. */
	private static final int	RENDER_FRAME_RATE	= 60;
	
	/** The singleton instance of the window class. */
	private static Window instance = null;
	
	/** The GLFW window handle, a C pointer. */
	private long windowHandle;
	
	/** The title of the window. */
	private String		title;
	/** The window dimensions. */
	private WindowSize	windowSize;
	
	/** The main renderer called each render cycle. */
	private Renderer		renderer;
	/** An optional initialization method called once from the window thread. */
	private InitMethod		initMethod		= null;
	/** An optional update method called from the window thread each frame. */
	private UpdateMethod	updateMethod	= null;
	/** An optional cleanup method called once from the window thread. */
	private CleanupMethod	cleanupMethod	= null;
	
	/** The input manager for this window. */
	private InputManager inputManager;
	
	/** A method used to programmatically change the window's visibility. */
	private Runnable	setVisibility	= null;
	/** A method used to programmatically change the window's size. */
	private Runnable	setSize			= null;
	/**
	 * Whether the window was resized this render cycle, either by the user or
	 * programmatically.
	 */
	private boolean		resized			= false;
	
	/** Represents a window's dimensions. */
	private class WindowSize {
		
		/** This window's width, in pixels. */
		private int	width;
		/** This window's height, in pixels. */
		private int	height;
	}
	
	/** Represents an initialization method. */
	@FunctionalInterface
	/* package */ interface InitMethod {
		
		/**
		 * Initializes any resources that need to be initialized from the window thread.
		 */
		public void init();
	}
	
	/** Represents an update method. */
	@FunctionalInterface
	/* package */ interface UpdateMethod {
		
		/** Updates the game state to prepare for another call to the renderer. */
		public void update();
	}
	
	/** Represents a cleanup method. */
	@FunctionalInterface
	/* package */ interface CleanupMethod {
		
		/**
		 * Cleans up any resources that need to be closed, unlocked or otherwise freed
		 * as the window is closed.
		 */
		public void cleanup();
	}
	
	/**
	 * Constructs a new instance of the window class. This constructor is private,
	 * following the singleton design pattern.
	 * 
	 * @param width
	 *            the width of the window, in pixels.
	 * @param height
	 *            the height of the window, in pixels.
	 * @param title
	 *            the window title to give to the operating system.
	 * @param renderer
	 *            the renderer with which to draw into the window.
	 * @param initMethod
	 *            an optional initialization method called from the window thread.
	 * @param updateMethod
	 *            an optional update method called from the window thread.
	 * @param cleanupMethod
	 *            an optional cleanup method called from the window thread.
	 */
	private Window(int width, int height, String title, Renderer renderer, InitMethod initMethod,
			UpdateMethod updateMethod, CleanupMethod cleanupMethod) {
		
		windowSize = new WindowSize();
		windowSize.width = width;
		windowSize.height = height;
		this.title = title;
		
		this.renderer = renderer;
		this.initMethod = initMethod;
		this.updateMethod = updateMethod;
		this.cleanupMethod = cleanupMethod;
		
		inputManager = new InputManager();
	}
	
	/**
	 * Creates the singleton instance of the window class. Throws a runtime
	 * exception if called twice.
	 * 
	 * @param width
	 *            the width of the window, in pixels.
	 * @param height
	 *            the height of the window, in pixels.
	 * @param title
	 *            the window title to give to the operating system.
	 * @param renderer
	 *            the renderer with which to draw into the window.
	 */
	public static void createInstance(int width, int height, String title, Renderer renderer) {
		
		createInstance(width, height, title, renderer, null, null, null);
	}
	
	/**
	 * Creates the singleton instance of the window class. Throws a runtime
	 * exception if called twice.
	 * 
	 * @param width
	 *            the width of the window, in pixels.
	 * @param height
	 *            the height of the window, in pixels.
	 * @param title
	 *            the window title to give to the operating system.
	 * @param renderer
	 *            the renderer with which to draw into the window.
	 * @param initMethod
	 *            an optional initialization method called from the window thread.
	 * @param updateMethod
	 *            an optional update method called from the window thread.
	 * @param cleanupMethod
	 *            an optional cleanup method called from the window thread.
	 */
	/* package */ static void createInstance(int width, int height, String title, Renderer renderer,
			InitMethod initMethod, UpdateMethod updateMethod, CleanupMethod cleanupMethod) {
		
		if (instance != null) {
			throw new GreaseRuntimeException("It is only legal to call Window.createInstance once.");
		}
		
		instance = new Window(width, height, title, renderer, initMethod, updateMethod, cleanupMethod);
	}
	
	/**
	 * Gets the singleton instance of the <code>Window</code> class.
	 * 
	 * @return the singleton instance of this window, or <code>null</code> if the
	 *         instance has not been created or has been destroyed.
	 */
	public static Window getInstance() {
		
		return instance;
	}
	
	/**
	 * Executes the window.
	 */
	public void run() {
		
		windowLifecycle();
	}
	
	/**
	 * Makes the window visible.
	 */
	public void show() {
		
		setVisibility = () -> glfwShowWindow(windowHandle);
	}
	
	/**
	 * Hides the window.
	 */
	public void hide() {
		
		setVisibility = () -> glfwHideWindow(windowHandle);
	}
	
	/**
	 * Sets the window dimensions.
	 * 
	 * @param width
	 *            the new window width, in pixels.
	 * @param height
	 *            the new window height, in pixels.
	 */
	public void setSize(int width, int height) {
		
		setSize = () -> {
			glfwSetWindowSize(windowHandle, width, height);
			windowSize.width = width;
			windowSize.height = height;
			resized = true;
		};
	}
	
	/**
	 * Tells this window to close. Can be called from any thread.
	 */
	public void close() {
		
		glfwSetWindowShouldClose(windowHandle, true);
	}
	
	/**
	 * @return the input manager for this window, which is used to access the input
	 *         state at any point.
	 */
	public InputManager getInputManager() {
		
		return inputManager;
	}
	
	/**
	 * Creates, starts, runs and terminates the window. Called by <code>run</code>,
	 * optionally in a new thread.
	 */
	private void windowLifecycle() {
		
		try {
			// Initialize
			init(); // Initialize the window
			
			renderer.setInitialSize(windowSize.width, windowSize.height);
			if (initMethod != null) {
				initMethod.init();
			}
			renderer.init(); // Initialize the main renderer
			
			show(); // Show the window
			
			// Run
			loop(); // Run the main loop
			
		} catch (GreaseException e) {
			
			e.printStackTrace();
			
		} finally {
			
			// Terminate
			renderer.cleanup();
			if (cleanupMethod != null) {
				cleanupMethod.cleanup();
			}
			cleanup();
		}
	}
	
	/**
	 * Creates the window.
	 * 
	 * @throws GreaseSystemException
	 *             if GLFW initialization fails.
	 * @throws GreaseWindowException
	 *             if the window could not be created.
	 */
	private void init() throws GreaseSystemException, GreaseWindowException {
		
		// Set up the error callback to print to System.err
		GLFWErrorCallback.createPrint(System.err).set();
		
		// Initialize GLFW
		if (!glfwInit()) {
			throw new GreaseSystemException("Unable to initialize GLFW");
		}
		
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
		
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // Don't show until everything is initialized
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // Allow resizing
		
		// Create the window
		windowHandle = glfwCreateWindow(windowSize.width, windowSize.height, title, NULL, NULL);
		
		if (windowHandle == NULL) {
			throw new GreaseWindowException("Failed to create the GLFW window");
		}
		
		// Place the window in the center of the screen
		centerWindow();
		
		// Configure the input callbacks
		initCallbacks();
		
		// Activate the OpenGL rendering context in this thread
		glfwMakeContextCurrent(windowHandle);
		
		// Enable v-sync
		glfwSwapInterval(1);
		
		// Connect OpenGL to the window
		GL.createCapabilities();
		
		// Set the clear color
		GL11.glClearColor(0.020f, 0.094f, 0.271f, 1.0f);
	}
	
	/**
	 * Executes the main program loop for the window thread.
	 */
	private void loop() {
		
		Timer inputSyncTimer = new Timer(INPUT_POLL_RATE / 1000.0d).start();
		Timer renderSyncTimer = new Timer(1.0d / RENDER_FRAME_RATE).start();
		
		// Run the main loop until the app should close
		while (!glfwWindowShouldClose(windowHandle)) {
			
			while (!renderSyncTimer.isTriggered()) {
				// Poll for window events, and asynchronous calls to this class
				pollEvents();
				inputSyncTimer.sleepAndRestart();
			}
			renderSyncTimer.restart();
			
			// Update for input handling
			if (updateMethod != null) {
				updateMethod.update();
			}
			
			// Update window size
			if (resized) {
				renderer.resize(windowSize.width, windowSize.height);
			}
			
			// Render the frame
			renderer.preRender();
			renderer.render();
			renderer.postRender();
			
			renderer.finalizeFrame();
			
			// Swap the color buffers
			glfwSwapBuffers(windowHandle);
		}
	}
	
	/**
	 * Destroys the window and terminates GLFW.
	 */
	private void cleanup() {
		
		// Free the window callbacks
		glfwFreeCallbacks(windowHandle);
		// Destroy the window
		glfwDestroyWindow(windowHandle);
		
		// Terminate GLFW
		glfwTerminate();
		glfwSetErrorCallback(null).free();
		
		instance = null;
	}
	
	/**
	 * Polls any events passed to the window from the operating system.
	 */
	private void pollEvents() {
		
		// Show/hide the window if requested
		if (setVisibility != null) {
			setVisibility.run();
		}
		
		// Set the window size if requested
		if (setSize != null) {
			setSize.run();
		}
		
		// Poll for window events, calling the relevant callbacks
		glfwPollEvents();
	}
	
	/**
	 * Centers this window on the screen.
	 */
	private void centerWindow() {
		
		// Get the display properties of the primary monitor
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		
		// Center the window
		glfwSetWindowPos(windowHandle, (vidmode.width() - windowSize.width) / 2,
				(vidmode.height() - windowSize.height) / 2);
	}
	
	/**
	 * Registers the GLFW callbacks that this window wants called.
	 */
	private void initCallbacks() {
		
		glfwSetKeyCallback(windowHandle, (window, key, scancode, action, modifiers) -> {
			inputManager.keyCallback(Key.fromId(key), KeyAction.fromCode(action), KeyModifier.fromBitField(modifiers));
		});
		
		glfwSetFramebufferSizeCallback(windowHandle, (window, width, height) -> {
			windowSize.width = width;
			windowSize.height = height;
			resized = true;
		});
	}
}
