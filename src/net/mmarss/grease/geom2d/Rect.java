package net.mmarss.grease.geom2d;

import org.joml.Intersectionf;
import org.joml.Vector2fc;

/**
 * Represents an axis-aligned two-dimensional rectangle. This class is
 * immutable.
 */
public class Rect implements Rectc {
	
	/** The minimum corner. */
	private final Pointc	corner0;
	/** The maximum corner. */
	private final Pointc	corner1;
	
	/**
	 * Constructs a new axis-aligned rectangle between the specified points. These
	 * points do not need to be in any particular order.
	 * 
	 * @param corner0
	 *            the first corner of the rectangle, diagonally opposite the second.
	 * @param corner1
	 *            the second corner of the rectangle, diagonally opposite the first.
	 */
	public Rect(Point corner0, Point corner1) {
		
		this(corner0.getX(), corner0.getY(), corner1.getX(), corner1.getY());
	}
	
	/**
	 * Constructs a new axis-aligned rectangle between the points (x0, y0) and (x1,
	 * y1). These points do not need to be in any particular order.
	 * 
	 * @param x0
	 *            the x-coordinate of the first corner of the rectangle.
	 * @param y0
	 *            the y-coordinate of the first corner of the rectangle.
	 * @param x1
	 *            the x-coordinate of the second corner of the rectangle.
	 * @param y1
	 *            the y-coordinate of the second corner of the rectangle.
	 */
	public Rect(float x0, float y0, float x1, float y1) {
		
		corner0 = new Point(Math.min(x0, x1), Math.min(y0, y1));
		corner1 = new Point(Math.max(x0, x1), Math.max(y0, y1));
	}
	
	/**
	 * @return the lower x-axis bound of this rectangle.
	 */
	@Override
	public float getMinX() {
		
		return corner0.getX();
	}
	
	/**
	 * @return the lower y-axis bound of this rectangle.
	 */
	@Override
	public float getMinY() {
		
		return corner0.getY();
	}
	
	/**
	 * @return the upper x-axis bound of this rectangle.
	 */
	@Override
	public float getMaxX() {
		
		return corner1.getX();
	}
	
	/**
	 * @return the upper y-axis bound of this rectangle.
	 */
	@Override
	public float getMaxY() {
		
		return corner1.getY();
	}
	
	/**
	 * @return this rectangle's width; the x-axis span of this rectangle.
	 */
	@Override
	public float getWidth() {
		
		return corner1.getX() - corner0.getX();
	}
	
	/**
	 * @return this rectangle's height; the y-axis span of this rectangle.
	 */
	@Override
	public float getHeight() {
		
		return corner1.getY() - corner0.getY();
	}
	
	/**
	 * @return an immutable interface to the internal data of the minimal corner of
	 *         this rectangle.
	 */
	/* package */ Vector2fc getMinCorner() {
		
		return ((Point) corner0).getVector2f();
	}
	
	/**
	 * @return an immutable interface to the internal data of the maximal corner of
	 *         this rectangle.
	 */
	/* package */ Vector2fc getMaxCorner() {
		
		return ((Point) corner1).getVector2f();
	}
	
	/**
	 * Tests whether this axis-aligned rectangle intersects the given rectangle.
	 * 
	 * @param other
	 *            the rectangle to test intersection with.
	 * @return <code>true</code> if this rectangle intersects the given rectangle,
	 *         or <code>false</code> if not.
	 */
	@Override
	public boolean intersects(Rect other) {
		
		return Intersectionf.testAarAar(getMinCorner(), getMaxCorner(), other.getMinCorner(), other.getMaxCorner());
	}
	
	/**
	 * Tests whether this axis-aligned rectangle intersects the given circle.
	 * 
	 * @param other
	 *            the circle to test intersection with.
	 * @return <code>true</code> if this rectangle intersects the given circle, or
	 *         <code>false</code> if not.
	 */
	@Override
	public boolean intersects(Circle other) {
		
		return Intersectionf.testAarCircle(getMinCorner(), getMaxCorner(), other.getCenterVec(),
				other.getRadiusSquared());
	}
}
