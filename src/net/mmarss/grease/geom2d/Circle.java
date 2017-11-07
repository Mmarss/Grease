package net.mmarss.grease.geom2d;

import org.joml.Intersectionf;
import org.joml.Vector2fc;

/**
 * Represents a two-dimensional circle.
 * 
 * This class is mutable. If an immutable view of it is desired, it can be
 * up-casted to an instance of the {@link Circlec} interface.
 */
public class Circle implements Circlec {
	
	/** The center of the circle, a two-dimensional point. */
	private Point	center;
	/** The radius of the circle. */
	private float	radius;
	
	/**
	 * Constructs a new circle with the given center and radius.
	 * 
	 * @param center
	 *            the center of the new circle.
	 * @param radius
	 *            the radius of the new circle.
	 */
	public Circle(Pointc center, float radius) {
		
		this(center.getX(), center.getY(), radius);
	}
	
	/**
	 * Constructs a new circle with the given center and radius.
	 * 
	 * @param centerX
	 *            the x-component of the circle's center.
	 * @param centerY
	 *            the y-component of the circle's center.
	 * @param radius
	 *            the radius of the new circle.
	 */
	public Circle(float centerX, float centerY, float radius) {
		
		center = new Point(centerX, centerY);
		this.radius = radius;
	}
	
	@Override
	public Pointc getCenter() {
		
		return center;
	}
	
	@Override
	public float getCenterX() {
		
		return center.getX();
	}
	
	@Override
	public float getCenterY() {
		
		return center.getY();
	}
	
	@Override
	public float getRadius() {
		
		return radius;
	}
	
	/**
	 * @param centerX
	 *            the x-component of the center of this circle.
	 * @param centerY
	 *            the y-component of the center of this circle.
	 */
	public void setCenter(float centerX, float centerY) {
		
		center.set(centerX, centerY);
	}
	
	/**
	 * @param radius
	 *            the radius of this circle.
	 */
	public void setRadius(float radius) {
		
		this.radius = radius;
	}
	
	/**
	 * @return an immutable interface to the internal data of this circle's center.
	 */
	/* package */ Vector2fc getCenterVec() {
		
		return center.getVector2f();
	}
	
	@Override
	public float getRadiusSquared() {
		
		return radius * radius;
	}
	
	@Override
	public boolean intersects(Circle other) {
		
		return Intersectionf.testCircleCircle(center.getX(), center.getY(), radius, other.center.getX(),
				other.center.getY(), other.radius);
	}
	
	@Override
	public boolean intersects(Rect other) {
		
		return Intersectionf.testAarCircle(other.getMinX(), other.getMinY(), other.getMaxX(), other.getMaxY(),
				center.getX(), center.getY(), getRadiusSquared());
	}
}
