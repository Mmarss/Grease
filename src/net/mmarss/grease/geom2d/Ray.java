package net.mmarss.grease.geom2d;

import org.joml.Intersectionf;
import org.joml.Vector2f;
import org.joml.Vector2fc;

/**
 * Represents a two-dimensional ray. This class is immutable.
 */
public class Ray {
	
	/** The origin of the ray. */
	private final Vector2fc	origin;
	/** The direction in which the ray points, which is normalized. */
	private final Vector2fc	dir;
	
	/**
	 * Constructs a new ray from the point (0, 0) in the given direction. The given
	 * direction will be normalized when it is stored.
	 * 
	 * @param dx
	 *            the x-component of the ray direction.
	 * @param dy
	 *            the y-component of the ray direction.
	 */
	public Ray(float dx, float dy) {
		
		this(new Point(), dx, dy);
	}
	
	/**
	 * Constructs a new ray from the given origin in the given direction. The given
	 * direction will be normalized when it is stored.
	 * 
	 * @param origin
	 *            the origin of the ray.
	 * @param dx
	 *            the x-component of the ray direction.
	 * @param dy
	 *            the y-component of the ray direction.
	 */
	public Ray(Pointc origin, float dx, float dy) {
		
		this.origin = new Vector2f(origin.getX(), origin.getY());
		dir = new Vector2f(dx, dy).normalize();
	}
	
	/**
	 * Tests whether this ray intersects the given circle.
	 * 
	 * @param other
	 *            the circle to test intersection with.
	 * @return <code>true</code> if this ray intersects the given circle, or
	 *         <code>false</code> if not.
	 */
	public boolean intersects(Circle other) {
		
		return Intersectionf.testRayCircle(origin, dir, other.getCenterVec(), other.getRadiusSquared());
	}
	
	/**
	 * Tests whether this ray intersects the given axis-aligned rectangle.
	 * 
	 * @param other
	 *            the rectangle to test intersection with.
	 * @return <code>true</code> if this ray intersects the given rectangle, or
	 *         <code>false</code> if not.
	 */
	public boolean intersects(Rect other) {
		
		return Intersectionf.testRayAar(origin, dir, other.getMinCorner(), other.getMaxCorner());
	}
}
