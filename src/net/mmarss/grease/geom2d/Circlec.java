package net.mmarss.grease.geom2d;

/**
 * Represents an immutable interface to a two-dimensional circle.
 */
public interface Circlec {
	
	/**
	 * @return the center of this circle.
	 */
	public Pointc getCenter();
	
	/**
	 * @return the x-component of the center of this circle.
	 */
	public float getCenterX();
	
	/**
	 * @return the y-component of the center of this circle.
	 */
	public float getCenterY();
	
	/**
	 * @return the radius of this circle.
	 */
	public float getRadius();
	
	/**
	 * @return the radius of this circle, squared.
	 */
	public float getRadiusSquared();
	
	/**
	 * Tests whether this circle intersects the given circle.
	 * 
	 * @param other
	 *            the circle to test intersection with.
	 * @return <code>true</code> if this circle intersects the given circle, or
	 *         <code>false</code> if not.
	 */
	public boolean intersects(Circle other);
	
	/**
	 * Tests whether this circle intersects the given axis-aligned rectangle.
	 * 
	 * @param other
	 *            the rectangle to test intersection with.
	 * @return <code>true</code> if this circle intersects the given rectangle, or
	 *         <code>false</code> if not.
	 */
	public boolean intersects(Rect other);
}
