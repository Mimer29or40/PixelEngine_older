package pe.color;

import pe.PixelEngine;

/**
 * Interface to a read-only view of a Color.
 *
 * @author Ryan Smith
 */
public interface Colorc
{
    /**
     * @return the value of the r component
     */
    int r();
    
    /**
     * @return the value of the g component
     */
    int g();
    
    /**
     * @return the value of the b component
     */
    int b();
    
    /**
     * @return the value of the a component
     */
    int a();
    
    /**
     * Get the value of the specified component of this color.
     *
     * @param component the component, within <code>[0..3]</code>
     * @return the value
     * @throws IllegalArgumentException if <code>component</code> is not within <code>[0..3]</code>
     */
    int get(int component) throws IllegalArgumentException;
    
    /**
     * Compare the color components of <code>this</code> color with the given <code>(r, g, b, a)</code>
     * and return whether all of them are equal.
     *
     * @param r the r component to compare to
     * @param g the g component to compare to
     * @param b the b component to compare to
     * @param a the a component to compare to
     * @return <code>true</code> if all the color components are equal
     */
    boolean equals(int r, int g, int b, int a);
    
    /**
     * Compare the color components of <code>this</code> color with the given <code>(r, g, b, 255)</code>
     * and return whether all of them are equal.
     *
     * @param r the r component to compare to
     * @param g the g component to compare to
     * @param b the b component to compare to
     * @return <code>true</code> if all the color components are equal
     */
    default boolean equals(int r, int g, int b)
    {
        return equals(r, g, b, 255);
    }
    
    /**
     * Compare the color components of <code>this</code> color with the given <code>(g, g, g, a)</code>
     * and return whether all of them are equal.
     *
     * @param grey the r, g, and b component to compare to
     * @param a    the a component to compare to
     * @return <code>true</code> if all the color components are equal
     */
    default boolean equals(int grey, int a)
    {
        return equals(grey, grey, grey, a);
    }
    
    /**
     * Compare the color components of <code>this</code> color with the given <code>(g, g, g, 255)</code>
     * and return whether all of them are equal.
     *
     * @param grey the r, g, and b component to compare to
     * @return <code>true</code> if all the color components are equal
     */
    default boolean equals(int grey)
    {
        return equals(grey, grey, grey, 255);
    }
    
    /**
     * Blends the supplied <code>(r, g, b, a)</code> (source) with <code>this</code> (backdrop) according
     * to the blend function and stores these values in <code>dest</code>.
     *
     * @param r    the r component of source
     * @param g    the g component of source
     * @param b    the b component of source
     * @param a    the a component of source
     * @param func the function to blend the colors
     * @param dest will hold the result
     * @return dest
     */
    Color blend(int r, int g, int b, int a, IBlend func, Color dest);
    
    /**
     * Blends the supplied <code>(r, g, b, 255)</code> (source) with <code>this</code> (backdrop) according
     * to the blend function and stores these values in <code>dest</code>. Source a is assumed to
     * be 255.
     *
     * @param r    the r component of source
     * @param g    the g component of source
     * @param b    the b component of source
     * @param func the function to blend the colors
     * @param dest will hold the result
     * @return dest
     */
    default Color blend(int r, int g, int b, IBlend func, Color dest)
    {
        return blend(r, g, b, 255, func, dest);
    }
    
    /**
     * Blends the supplied <code>(g, g, g, a)</code> (source) with <code>this</code> (backdrop)
     * according to the blend function and stores these values in <code>dest</code>. Source a is assumed to
     * be 255.
     *
     * @param grey the r, g, and b component of source
     * @param a    the a component of source
     * @param func the function to blend the colors
     * @param dest will hold the result
     * @return dest
     */
    default Color blend(int grey, int a, IBlend func, Color dest)
    {
        return blend(grey, grey, grey, a, func, dest);
    }
    
    /**
     * Blends the supplied <code>(g, g, g, 255)</code> (source) with <code>this</code> (backdrop)
     * according to the blend function and stores these values in <code>dest</code>. Source a is assumed to
     * be 255.
     *
     * @param grey the r, g, and b component of source
     * @param func the function to blend the colors
     * @param dest will hold the result
     * @return dest
     */
    default Color blend(int grey, IBlend func, Color dest)
    {
        return blend(grey, grey, grey, 255, func, dest);
    }
    
    /**
     * Blends the supplied color (source) with <code>this</code> (backdrop) according to the blend
     * function and stores these values in <code>dest</code>.
     *
     * @param source the source color
     * @param func   the function to blend the colors
     * @param dest   will hold the result
     * @return dest
     */
    default Color blend(Colorc source, IBlend func, Color dest)
    {
        return blend(source.r(), source.g(), source.b(), source.a(), func, dest);
    }
    
    /**
     * @return 32-bit integer representation of the color
     */
    int toInt();
    
    /**
     * @return the hue of the color [0..359]
     */
    int hue();
    
    /**
     * @return the saturation of the color [0..255]
     */
    int saturation();
    
    /**
     * @return the brightness of the color [0..255]
     */
    int brightness();
    
    /**
     * Determine the component with the biggest absolute value.
     *
     * @return the component, within <code>[0..255]</code>
     */
    int maxComponent();
    
    /**
     * Determine the component with the middle (towards zero) absolute value.
     *
     * @return the component, within <code>[0..255]</code>
     */
    int midComponent();
    
    /**
     * Determine the component with the smallest (towards zero) absolute value.
     *
     * @return the component, within <code>[0..255]</code>
     */
    int minComponent();
    
    /**
     * Determine the component with the biggest absolute value.
     *
     * @return the component index, within <code>[0..2]</code>
     */
    int maxComponentIndex();
    
    /**
     * Determine the component with the middle (towards zero) absolute value.
     *
     * @return the component index, within <code>[0..2]</code>
     */
    int midComponentIndex();
    
    /**
     * Determine the component with the smallest (towards zero) absolute value.
     *
     * @return the component index, within <code>[0..2]</code>
     */
    int minComponentIndex();
    
    /**
     * Negate this color and store the result in <code>dest</code>.
     *
     * @param dest will hold the result
     * @return dest
     */
    Color negate(Color dest);
    
    /**
     * Scales this color and stores the result in <code>dest</code>.
     *
     * @param x    scale
     * @param dest will hold the result
     * @return dest
     */
    default Color scale(double x, Color dest)
    {
        return scale(x, false, dest);
    }
    
    /**
     * Scales this color and stores the result in <code>dest</code>.
     *
     * @param x     scale
     * @param alpha flag to scale the alpha (default: false)
     * @param dest  will hold the result
     * @return dest
     */
    Color scale(double x, boolean alpha, Color dest);
    
    /**
     * Blend this color with another color and store the result in <code>result</code>.
     *
     * @param other  the other color
     * @param result the result
     * @return result
     */
    default Color blend(Color other, Color result)
    {
        return blend(other, PixelEngine.blend(), result);
    }
    
    /**
     * Negate this color and store the result in <code>result</code>.
     *
     * @param other  the other color
     * @param func   the function that will blend the two colors
     * @param result the result
     * @return result
     */
    Color blend(Color other, IBlend func, Color result);
    
    // /**
    //  * Add the supplied color to this one and store the result in
    //  * <code>dest</code>.
    //  *
    //  * @param v    the color to add
    //  * @param dest will hold the result
    //  * @return dest
    //  */
    // Vector2i add(Vector2ic v, Vector2i dest);
    //
    // /**
    //  * Increment the components of this color by the given values and store the
    //  * result in <code>dest</code>.
    //  *
    //  * @param x    the x component to add
    //  * @param y    the y component to add
    //  * @param dest will hold the result
    //  * @return dest
    //  */
    // Vector2i add(int x, int y, Vector2i dest);
    
    // /**
    //  * Subtract the supplied color from this one and store the result in
    //  * <code>dest</code>.
    //  *
    //  * @param v    the color to subtract
    //  * @param dest will hold the result
    //  * @return dest
    //  */
    // Vector2i sub(Vector2ic v, Vector2i dest);
    //
    // /**
    //  * Decrement the components of this color by the given values and store the
    //  * result in <code>dest</code>.
    //  *
    //  * @param x    the x component to subtract
    //  * @param y    the y component to subtract
    //  * @param dest will hold the result
    //  * @return dest
    //  */
    // Vector2i sub(int x, int y, Vector2i dest);
    
    // /**
    //  * Multiply all components of this {@link Vector2ic} by the given scalar
    //  * value and store the result in <code>dest</code>.
    //  *
    //  * @param scalar the scalar to multiply this color by
    //  * @param dest   will hold the result
    //  * @return dest
    //  */
    // Vector2i mul(int scalar, Vector2i dest);
    //
    // /**
    //  * Multiply the supplied color by this one and store the result in
    //  * <code>dest</code>.
    //  *
    //  * @param v    the color to multiply
    //  * @param dest will hold the result
    //  * @return dest
    //  */
    // Vector2i mul(Vector2ic v, Vector2i dest);
    //
    // /**
    //  * Multiply the components of this color by the given values and store the
    //  * result in <code>dest</code>.
    //  *
    //  * @param x    the x component to multiply
    //  * @param y    the y component to multiply
    //  * @param dest will hold the result
    //  * @return dest
    //  */
    // Vector2i mul(int x, int y, Vector2i dest);
    
    // /**
    //  * Return the length squared of this color.
    //  *
    //  * @return the length squared
    //  */
    // long lengthSquared();
    //
    // /**
    //  * Return the length of this color.
    //  *
    //  * @return the length
    //  */
    // double length();
    //
    // /**
    //  * Return the distance between this Vector and <code>v</code>.
    //  *
    //  * @param v the other color
    //  * @return the distance
    //  */
    // double distance(Vector2ic v);
    //
    // /**
    //  * Return the distance between <code>this</code> color and <code>(x, y)</code>.
    //  *
    //  * @param x the x component of the other color
    //  * @param y the y component of the other color
    //  * @return the euclidean distance
    //  */
    // double distance(int x, int y);
    //
    // /**
    //  * Return the square of the distance between this color and <code>v</code>.
    //  *
    //  * @param v the other color
    //  * @return the squared of the distance
    //  */
    // long distanceSquared(Vector2ic v);
    //
    // /**
    //  * Return the square of the distance between <code>this</code> color and
    //  * <code>(x, y)</code>.
    //  *
    //  * @param x the x component of the other color
    //  * @param y the y component of the other color
    //  * @return the square of the distance
    //  */
    // long distanceSquared(int x, int y);
    //
    // /**
    //  * Return the grid distance in between (aka 1-Norm, Minkowski or Manhattan distance)
    //  * <code>(x, y)</code>.
    //  *
    //  * @param v the other color
    //  * @return the grid distance
    //  */
    // long gridDistance(Vector2ic v);
    //
    // /**
    //  * Return the grid distance in between (aka 1-Norm, Minkowski or Manhattan distance)
    //  * <code>(x, y)</code>.
    //  *
    //  * @param x the x component of the other color
    //  * @param y the y component of the other color
    //  * @return the grid distance
    //  */
    // long gridDistance(int x, int y);
    //
    // /**
    //  * Set the components of <code>dest</code> to be the component-wise minimum of this and the other color.
    //  *
    //  * @param v    the other color
    //  * @param dest will hold the result
    //  * @return dest
    //  */
    // Vector2i min(Vector2ic v, Vector2i dest);
    //
    // /**
    //  * Set the components of <code>dest</code> to be the component-wise maximum of this and the other color.
    //  *
    //  * @param v    the other color
    //  * @param dest will hold the result
    //  * @return dest
    //  */
    // Vector2i max(Vector2ic v, Vector2i dest);
}
