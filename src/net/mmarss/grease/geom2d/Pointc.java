package net.mmarss.grease.geom2d;

/**
 * Represents an immutable interface to a two-dimensional point.
 */
public interface Pointc {
	
	/**
	 * @return the x-coordinate component of this point.
	 */
	public float getX();
	
	/**
	 * @return the y-coordinate component of this point.
	 */
	public float getY();
	
	/**
	 * Determines the Euclidean distance from this point to another point.
	 * 
	 * @param other
	 *            the point to calculate the distance to.
	 * @return the distance from this point to the other point.
	 */
	public float getDistance(Point other);
}
