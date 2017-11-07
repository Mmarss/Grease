package net.mmarss.grease.geom2d;

import org.joml.Vector2f;
import org.joml.Vector2fc;

/**
 * Represents a single two-dimensional point.
 * 
 * This class is mutable. If an immutable view of it is desired, it can be
 * up-casted to an instance of the {@link Pointc} interface.
 * 
 * This class is a wrapper around JOML's {@link org.joml.Vector2f}.
 */
public class Point implements Pointc {
	
	/**
	 * The internal data of a point.
	 */
	private final Vector2f vec;
	
	/**
	 * Constructs a new point representing the coordinate (0, 0).
	 */
	public Point() {
		
		this(0, 0);
	}
	
	/**
	 * Constructs a new point representing the coordinate (x, y).
	 * 
	 * @param x
	 *            the x-coordinate component of this point.
	 * @param y
	 *            the y-coordinate component of this point.
	 */
	public Point(float x, float y) {
		
		vec = new Vector2f(x, y);
	}
	
	/**
	 * Constructs a new point as a copy of another point.
	 * 
	 * @param orig
	 *            the point to copy.
	 */
	public Point(Point orig) {
		
		this(orig.vec.x, orig.vec.y);
	}
	
	@Override
	public float getX() {
		
		return vec.x;
	}
	
	@Override
	public float getY() {
		
		return vec.y;
	}
	
	/**
	 * @param x
	 *            the x-coordinate component of this point.
	 * @param y
	 *            the y-coordinate component of this point.
	 */
	public void set(float x, float y) {
		
		vec.x = x;
		vec.y = y;
	}
	
	/**
	 * @param x
	 *            the x-coordinate component of this point.
	 */
	public void setX(float x) {
		
		vec.x = x;
	}
	
	/**
	 * @param y
	 *            the y-coordinate component of this point.
	 */
	public void setY(float y) {
		
		vec.y = y;
	}
	
	/**
	 * @return an immutable interface to the internal data of this class.
	 */
	/* package */ Vector2fc getVector2f() {
		
		return vec;
	}
	
	@Override
	public float getDistance(Point other) {
		
		return vec.distance(other.vec);
	}
}
