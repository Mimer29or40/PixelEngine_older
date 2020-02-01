package pe.phx2D.model;

import pe.phx2D.util.Printable;
import pe.phx2D.util.Util;
import pe.vector.Vector2;

import java.util.Objects;

/**
 * An immutable Axis Aligned Bounding Box whose boundaries are stored with double floating point precision.
 * <p>
 * Note that for AABB we regard the vertical coordinate as **increasing upwards**, so the top coordinate is greater than the bottom coordinate. This is in contrast to screen
 * coordinates where vertical coordinates increase downwards.
 */
public class AABB implements Printable
{
    public static final AABB EMPTY_RECT = new AABB(0, 0, 0, 0).markImmutable();
    
    static
    {
        Printable.addElement(AABB.class, "left", AABB::getLeft, true);
        Printable.addElement(AABB.class, "bottom", AABB::getBottom, true);
        Printable.addElement(AABB.class, "right", AABB::getRight, true);
        Printable.addElement(AABB.class, "top", AABB::getTop, true);
    }
    
    protected double left, bottom, right, top;
    
    private boolean immutable;
    
    /**
     * @param left   left side of AABB, must be less than right
     * @param bottom bottom of AABB, must be less than top
     * @param right  right side of AABB
     * @param top    top of AABB
     *
     * @throws RuntimeException when left > right or bottom > top
     */
    public AABB(double left, double bottom, double right, double top)
    {
        setLeft(left).setBottom(bottom).setRight(right).setTop(top);
        validate();
    }
    
    public AABB()
    {
        this(0, 0, 0, 0);
    }
    
    public AABB(Vector2 point1, Vector2 point2)
    {
        this(Math.min(point1.x(), point2.x()), Math.min(point1.y(), point2.y()), Math.max(point1.x(), point2.x()), Math.max(point1.y(), point2.y()));
    }
    
    public AABB(AABB aabb)
    {
        this(aabb.getLeft(), aabb.getBottom(), aabb.getRight(), aabb.getTop());
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof AABB)) return false;
        AABB aabb = (AABB) o;
        return Double.compare(aabb.getLeft(), getLeft()) == 0 &&
               Double.compare(aabb.getBottom(), getBottom()) == 0 &&
               Double.compare(aabb.getRight(), getRight()) == 0 &&
               Double.compare(aabb.getTop(), getTop()) == 0;
    }
    
    /**
     * Returns `true` if this AABB is nearly equal to another AABB. The optional tolerance value corresponds to the `epsilon` in {@link Util#close}, so the actual tolerance
     * used depends on the magnitude of the numbers being compared.
     *
     * @param rect          the AABB to compare with
     * @param opt_tolerance optional tolerance for equality test
     *
     * @return true` if this AABB is nearly equal to another AABB
     */
    public boolean nearEqual(AABB rect, double opt_tolerance)
    {
        return Util.close(getLeft(), rect.getLeft(), opt_tolerance) && Util.close(getBottom(), rect.getBottom(), opt_tolerance) && Util.close(getRight(),
                                                                                                                                              rect.getRight(),
                                                                                                                                              opt_tolerance) && Util.close(getTop(),
                                                                                                                                                                           rect.getTop(),
                                                                                                                                                                           opt_tolerance);
    }
    
    public boolean nearEqual(AABB rect)
    {
        return Util.close(getLeft(), rect.getLeft()) && Util.close(getBottom(), rect.getBottom()) && Util.close(getRight(), rect.getRight()) && Util.close(getTop(), rect.getTop());
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(getLeft(), getBottom(), getRight(), getTop());
    }
    
    protected AABB getDefault()
    {
        return isImmutable() ? new AABB() : this;
    }
    
    protected void validate()
    {
        if (getLeft() > getRight()) throw new RuntimeException("AABB: left > right " + getLeft() + " > " + getRight());
        if (getBottom() > getTop()) throw new RuntimeException("AABB: bottom > top " + getBottom() + " > " + getTop());
    }
    
    public boolean isImmutable()
    {
        return this.immutable;
    }
    
    public AABB markImmutable()
    {
        this.immutable = true;
        return this;
    }
    
    /**
     * Returns the smallest horizontal coordinate of this AABB
     *
     * @return smallest horizontal coordinate of this AABB
     */
    public double getLeft()
    {
        return this.left;
    }
    
    public AABB setLeft(double left)
    {
        if (isImmutable()) throw new UnsupportedOperationException("AABB is Immutable");
        this.left = left;
        validate();
        return this;
    }
    
    /**
     * Returns the smallest vertical coordinate of this AABB
     *
     * @return {number} smallest vertical coordinate  of this AABB
     */
    public double getBottom()
    {
        return this.bottom;
    }
    
    public AABB setBottom(double bottom)
    {
        if (isImmutable()) throw new UnsupportedOperationException("AABB is Immutable");
        this.bottom = bottom;
        validate();
        return this;
    }
    
    /**
     * Returns the largest horizontal coordinate of this AABB
     *
     * @return largest horizontal coordinate of this AABB
     */
    public double getRight()
    {
        return this.right;
    }
    
    public AABB setRight(double right)
    {
        if (isImmutable()) throw new UnsupportedOperationException("AABB is Immutable");
        this.right = right;
        validate();
        return this;
    }
    
    /**
     * Returns the largest vertical coordinate of this AABB
     *
     * @return largest vertical coordinate of this AABB
     */
    public double getTop()
    {
        return this.top;
    }
    
    public AABB setTop(double top)
    {
        if (isImmutable()) throw new UnsupportedOperationException("AABB is Immutable");
        this.top = top;
        validate();
        return this;
    }
    
    /**
     * Returns the horizontal width of this AABB
     *
     * @return horizontal width of this AABB
     */
    public double getWidth()
    {
        return getRight() - getLeft();
    }
    
    public AABB setWidth(double width)
    {
        return setRight(getLeft() + width);
    }
    
    /**
     * Returns the vertical height of this AABB
     *
     * @return vertical height of this AABB
     */
    public double getHeight()
    {
        return getTop() - getBottom();
    }
    
    public AABB setHeight(double height)
    {
        return setTop(getBottom() + height);
    }
    
    /**
     * Returns the horizontal coordinate of center of this AABB.
     *
     * @return horizontal coordinate of center of this AABB
     */
    public double getCenterX()
    {
        return (getLeft() + getRight()) * 0.5;
    }
    
    public AABB setCenterX(int x)
    {
        double halfWidth = getWidth() * 0.5;
        return setLeft(x - halfWidth).setRight(x + halfWidth);
    }
    
    /**
     * Returns the vertical coordinate of center of this AABB.
     *
     * @return vertical coordinate of center of this AABB
     */
    public double getCenterY()
    {
        return (getBottom() + getTop()) * 0.5;
    }
    
    public AABB setCenterY(int y)
    {
        double halfHeight = getHeight() * 0.5;
        return setBottom(y - halfHeight).setTop(y + halfHeight);
    }
    
    public AABB set(double left, double bottom, double right, double top)
    {
        return setLeft(left).setBottom(bottom).setRight(right).setTop(top);
    }
    
    public AABB set(AABB aabb)
    {
        return setLeft(aabb.getLeft()).setBottom(aabb.getBottom()).setRight(aabb.getRight()).setTop(aabb.getTop());
    }
    
    public AABB copy()
    {
        return new AABB(this);
    }
    
    /**
     * Returns `true` if the given point is within this aabb.
     *
     * @param point the point to test
     *
     * @return `true` if the point is within this aabb, or exactly on an edge
     */
    public boolean contains(Vector2 point)
    {
        return getLeft() <= point.x() && point.x() <= getRight() && getBottom() <= point.y() && point.y() <= getTop();
    }
    
    /**
     * Returns `true` if width or height of this AABB are zero (within given tolerance).
     *
     * @param opt_tolerance optional tolerance for the test; a width or height smaller than this is regarded as zero; default is 1E-16
     *
     * @return `true` if width or height of this AABB are zero (within given tolerance)
     */
    public boolean isEmpty(double opt_tolerance)
    {
        return getWidth() < opt_tolerance || getHeight() < opt_tolerance;
    }
    
    public boolean isEmpty(Double opt_tolerance)
    {
        return isEmpty(1E-16);
    }
    
    /**
     * Returns true if the line between the two points might be visible in the rectangle.
     *
     * @param p1 first end point of line
     * @param p2 second end point of line
     *
     * @return true if the line between the two points might be visible in the rectangle
     */
    public boolean intersects(Vector2 p1, Vector2 p2)
    {
        // if either point is inside the rect, then line is visible
        if (contains(p1) || contains(p2)) return true;
        
        // if both points are "outside" one of the rectangle sides, then line is not visible
        if (p1.x() < getLeft() && p2.x() < getLeft()) return false;
        if (getRight() < p1.x() && getRight() < p2.x()) return false;
        if (p1.y() < getBottom() && p2.y() < getBottom()) return false;
        if (getTop() < p1.y() || getTop() < p2.y()) return false;
        
        // if lines intersect either vertical lines
        double slope      = (p2.y() - p1.y()) / (p2.x() - p1.x());
        double intersect1 = slope * (getLeft() - p1.x()) + p1.y();
        double intersect2 = slope * (getRight() - p1.x()) + p1.y();
        
        return (getBottom() <= intersect1 && intersect1 <= getTop()) || (getBottom() <= intersect2 && intersect2 <= getTop());
    }
    
    /**
     * Returns a copy of this AABB expanded by the given margin in x and y dimension.
     *
     * @param marginX the margin to add at left and right
     * @param marginY the margin to add at top and bottom; if undefined then `marginX` is used for both x and y dimension
     *
     * @return a AABB with same center as this AABB, but expanded or contracted
     */
    public AABB expand(double marginX, double marginY, AABB out)
    {
        if (out == null) out = new AABB();
        out.setLeft(getLeft() - marginX);
        out.setBottom(getBottom() - marginY);
        out.setRight(getRight() + marginX);
        out.setTop(getTop() + marginX);
        return out;
    }
    
    public AABB expand(double marginX, double marginY)
    {
        return expand(marginX, marginY, getDefault());
    }
    
    public AABB expand(double margin, AABB out)
    {
        return expand(margin, margin, out);
    }
    
    public AABB expand(double margin)
    {
        return expand(margin, margin, getDefault());
    }
    
    /**
     * Returns a copy of this rectangle translated by the given amount.
     *
     * @param x horizontal amount to translate by, or Vector to translate by
     * @param y vertical amount to translate by; required when `x` is a number.
     *
     * @return a copy of this rectangle translated by the given amount
     */
    public AABB translate(double x, double y, AABB out)
    {
        if (out == null) out = new AABB();
        out.setLeft(getLeft() + x);
        out.setBottom(getBottom() + y);
        out.setRight(getRight() + x);
        out.setTop(getTop() + y);
        return out;
    }
    
    public AABB translate(double x, double y)
    {
        return translate(x, y, getDefault());
    }
    
    public AABB translate(Vector2 v, AABB out)
    {
        return translate(v.x(), v.y(), out);
    }
    
    public AABB translate(Vector2 v)
    {
        return translate(v.x(), v.y(), getDefault());
    }
    
    /**
     * Returns a copy of this AABB expanded by the given factors in both x and y
     * dimension. Expands (or contracts) about the center of this AABB by the given
     * expansion factor in x and y dimensions.
     *
     * @param factorX the factor to expand width by; 1.1 gives a 10 percent
     *                expansion; 0.9 gives a 10 percent contraction
     * @param factorY factor to expand height by; if undefined then `factorX` is
     *                used for both x and y dimension
     *
     * @return a AABB with same center as this AABB, but expanded or contracted
     */
    public AABB scale(double factorX, double factorY, AABB out)
    {
        if (out == null) out = new AABB();
        double x0 = getCenterX(), y0 = getCenterY();
        double w  = getWidth(), h = getHeight();
        out.setLeft(x0 - (factorX * w) * 0.5);
        out.setBottom(y0 - (factorY * h) * 0.5);
        out.setRight(x0 + (factorX * w) * 0.5);
        out.setTop(y0 + (factorY * h) * 0.5);
        return out;
    }
    
    public AABB scale(double factorX, double factorY)
    {
        return scale(factorX, factorY, getDefault());
    }
    
    public AABB scale(double factor, AABB out)
    {
        return scale(factor, factor, out);
    }
    
    public AABB scale(double factor)
    {
        return scale(factor, factor, getDefault());
    }
    
    /**
     * Returns a rectangle that is the intersection of this and another rectangle.
     *
     * @param rect the other rectangle to form the intersection with
     *
     * @return the intersection of this and the other rectangle, possibly
     * an empty rectangle.
     */
    public AABB intersection(AABB rect)
    {
        double left   = Math.max(getLeft(), rect.getLeft());
        double bottom = Math.max(getBottom(), rect.getBottom());
        double right  = Math.min(getRight(), rect.getRight());
        double top    = Math.min(getTop(), rect.getTop());
        return left > right || bottom > top ? AABB.EMPTY_RECT : new AABB(left, bottom, right, top);
    }
    
    /**
     * Returns a rectangle that is the union of this and another rectangle.
     *
     * @param rect the other rectangle to form the union with
     *
     * @return the union of this and the other rectangle
     */
    public AABB union(AABB rect)
    {
        return new AABB(Math.min(getLeft(), rect.getLeft()), Math.min(getBottom(), rect.getBottom()), Math.max(getRight(), rect.getRight()), Math.max(getTop(), rect.getTop()));
    }
    
    /**
     * Returns a rectangle that is the union of this rectangle and a point
     *
     * @param point the point to form the union with
     *
     * @return the union of this rectangle and the point
     */
    public AABB union(Vector2 point)
    {
        return new AABB(Math.min(getLeft(), point.x()), Math.min(getBottom(), point.y()), Math.max(getRight(), point.x()), Math.max(getTop(), point.y()));
    }
    
    /**
     * Returns a AABB centered at the given point with given height and width.
     *
     * @param center center of the AABB
     * @param width  width of the AABB
     * @param height height of the AABB
     *
     * @return a AABB centered at the given point with given height and width
     */
    public static AABB make(Vector2 center, double width, double height)
    {
        return new AABB(center.x() - width * 0.5, center.y() - height * 0.5, center.x() + width * 0.5, center.y() + height * 0.5);
    }
    
    /**
     * Returns a AABB centered at the given point with given size.
     *
     * @param center center of the AABB
     * @param size   width and height as a Vector
     *
     * @return a AABB centered at the given point with given size
     */
    public static AABB make(Vector2 center, Vector2 size)
    {
        return new AABB(center.x() - size.x() * 0.5, center.y() - size.y() * 0.5, center.x() + size.x() * 0.5, center.y() + size.y() * 0.5);
    }
}
