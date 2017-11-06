package net.mmarss.grease.graphics;

/**
 * A renderer class, in charge of drawing to a window.
 */
public abstract class Renderer {
	
	/** The width of the window client area, in pixels. */
	private int		width;
	/** The height of the window client area, in pixels. */
	private int		height;
	/** Whether the window has been resized since the last render cycle. */
	private boolean	resized;
	
	/**
	 * Initializes this renderer. A window will call this method from the window
	 * thread, before calling any render methods.
	 * 
	 * This method does nothing by default, and should be overridden by subclasses
	 * if it is useful.
	 */
	public void init() {}
	
	/**
	 * Prepares the renderer for a new render cycle.
	 * 
	 * This method does nothing by default, and can be overridden by subclasses if
	 * it is helpful.
	 */
	public void preRender() {}
	
	/**
	 * Renders whatever this renderer is responsible for rendering.
	 */
	public abstract void render();
	
	/**
	 * Closes off this renderer's render cycle.
	 * 
	 * This method does nothing by default, and can be overridden by subclasses if
	 * it is helpful.
	 */
	public void postRender() {}
	
	/**
	 * Cleans up any resources created or used by this renderer. This method should
	 * mirror the <code>init</code> method.
	 * 
	 * This method does nothing by default, and can be overridden by subclasses if
	 * it is helpful.
	 */
	public void cleanup() {}
	
	/**
	 * Called when the window client area is resized, from the window thread. This
	 * method is called before a render cycle.
	 * 
	 * This method does nothing by default, and can be overridden by subclasses if
	 * it is helpful.
	 * 
	 * @param width
	 *            the new width of the window client area.
	 * @param height
	 *            the new height of the window client area.
	 */
	public void onResize(int width, int height) {}
	
	/**
	 * @return the current width of the window client area.
	 */
	protected int getWidth() {
		
		return width;
	}
	
	/**
	 * @return the current height of the window client area.
	 */
	protected int getHeight() {
		
		return height;
	}
	
	/**
	 * @return whether the window client area was resized since the last render
	 *         cycle.
	 */
	protected boolean wasResized() {
		
		return resized;
	}
	
	/**
	 * Sets the initial size of the window client area, without triggering a resize
	 * event.
	 * 
	 * @param width
	 *            the canvas width.
	 * @param height
	 *            the canvas height.
	 */
	public final void setInitialSize(int width, int height) {
		
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Tells the renderer to resize itself. Called after postRender and before
	 * preRender, from the render thread.
	 * 
	 * @param width
	 *            the new canvas width.
	 * @param height
	 *            the new canvas height.
	 */
	public final void resize(int width, int height) {
		
		this.width = width;
		this.height = height;
		resized = true;
		onResize(width, height);
	}
	
	/**
	 * Closes off the current render cycle, clearing any events that were triggered
	 * this cycle.
	 */
	public final void finalizeFrame() {
		
		resized = false;
	}
}
