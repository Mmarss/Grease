package net.mmarss.grease.geom2d;

public interface Rectc {
	
	/**
	 * @return the lower x-axis bound of this rectangle.
	 */
	public float getMinX();
	
	/**
	 * @return the lower y-axis bound of this rectangle.
	 */
	public float getMinY();
	
	/**
	 * @return the upper x-axis bound of this rectangle.
	 */
	public float getMaxX();
	
	/**
	 * @return the upper y-axis bound of this rectangle.
	 */
	public float getMaxY();
	
	/**
	 * @return this rectangle's width; the x-axis span of this rectangle.
	 */
	public float getWidth();
	
	/**
	 * @return this rectangle's height; the y-axis span of this rectangle.
	 */
	public float getHeight();
	
	/**
	 * Tests whether this axis-aligned rectangle intersects the given rectangle.
	 * 
	 * @param other
	 *            the rectangle to test intersection with.
	 * @return <code>true</code> if this rectangle intersects the given rectangle,
	 *         or <code>false</code> if not.
	 */
	public boolean intersects(Rect other);
	
	/**
	 * Tests whether this axis-aligned rectangle intersects the given circle.
	 * 
	 * @param other
	 *            the circle to test intersection with.
	 * @return <code>true</code> if this rectangle intersects the given circle, or
	 *         <code>false</code> if not.
	 */
	public boolean intersects(Circle other);
}
