package pe.color;

import pe.PixelEngine;

import java.util.Objects;

@SuppressWarnings("unused")
public class Color implements Colorc
{
    public static final Colorc WHITE      = new Color(255, 255, 255);
    public static final Colorc LIGHT_GREY = new Color(191, 191, 191);
    public static final Colorc GREY       = new Color(127, 127, 127);
    public static final Colorc DARK_GREY  = new Color(63, 63, 63);
    public static final Colorc BLACK      = new Color(0, 0, 0);
    
    public static final Colorc BACKGROUND_GREY = new Color(51, 51, 51);
    
    public static final Colorc LIGHTEST_RED = new Color(255, 191, 191);
    public static final Colorc LIGHTER_RED  = new Color(255, 127, 127);
    public static final Colorc LIGHT_RED    = new Color(255, 63, 63);
    public static final Colorc RED          = new Color(255, 0, 0);
    public static final Colorc DARK_RED     = new Color(191, 0, 0);
    public static final Colorc DARKER_RED   = new Color(127, 0, 0);
    public static final Colorc DARKEST_RED  = new Color(63, 0, 0);
    
    public static final Colorc LIGHTEST_YELLOW = new Color(255, 255, 191);
    public static final Colorc LIGHTER_YELLOW  = new Color(255, 255, 127);
    public static final Colorc LIGHT_YELLOW    = new Color(255, 255, 63);
    public static final Colorc YELLOW          = new Color(255, 255, 0);
    public static final Colorc DARK_YELLOW     = new Color(191, 191, 0);
    public static final Colorc DARKER_YELLOW   = new Color(127, 127, 0);
    public static final Colorc DARKEST_YELLOW  = new Color(63, 63, 0);
    
    public static final Colorc LIGHTEST_GREEN = new Color(191, 255, 191);
    public static final Colorc LIGHTER_GREEN  = new Color(127, 255, 127);
    public static final Colorc LIGHT_GREEN    = new Color(63, 255, 63);
    public static final Colorc GREEN          = new Color(0, 255, 0);
    public static final Colorc DARK_GREEN     = new Color(0, 191, 0);
    public static final Colorc DARKER_GREEN   = new Color(0, 127, 0);
    public static final Colorc DARKEST_GREEN  = new Color(0, 63, 0);
    
    public static final Colorc LIGHTEST_CYAN = new Color(191, 255, 255);
    public static final Colorc LIGHTER_CYAN  = new Color(127, 255, 255);
    public static final Colorc LIGHT_CYAN    = new Color(63, 255, 255);
    public static final Colorc CYAN          = new Color(0, 255, 255);
    public static final Colorc DARK_CYAN     = new Color(0, 191, 191);
    public static final Colorc DARKER_CYAN   = new Color(0, 127, 127);
    public static final Colorc DARKEST_CYAN  = new Color(0, 63, 63);
    
    public static final Colorc LIGHTEST_BLUE = new Color(191, 191, 255);
    public static final Colorc LIGHTER_BLUE  = new Color(127, 127, 255);
    public static final Colorc LIGHT_BLUE    = new Color(63, 63, 255);
    public static final Colorc BLUE          = new Color(0, 0, 255);
    public static final Colorc DARK_BLUE     = new Color(0, 0, 191);
    public static final Colorc DARKER_BLUE   = new Color(0, 0, 127);
    public static final Colorc DARKEST_BLUE  = new Color(0, 0, 63);
    
    public static final Colorc LIGHTEST_MAGENTA = new Color(255, 191, 255);
    public static final Colorc LIGHTER_MAGENTA  = new Color(255, 127, 255);
    public static final Colorc LIGHT_MAGENTA    = new Color(255, 63, 255);
    public static final Colorc MAGENTA          = new Color(255, 0, 255);
    public static final Colorc DARK_MAGENTA     = new Color(191, 0, 191);
    public static final Colorc DARKER_MAGENTA   = new Color(127, 0, 127);
    public static final Colorc DARKEST_MAGENTA  = new Color(63, 0, 63);
    
    public static final Colorc BLANK = new Color(0, 0, 0, 0);
    
    private static final double PR = 0.299;
    private static final double PG = 0.587;
    private static final double PB = 0.114;
    
    private int r, g, b, a;
    
    public Color(Number r, Number g, Number b, Number a)
    {
        r(r).g(g).b(b).a(a);
    }
    
    public Color(Number r, Number g, Number b)
    {
        this(r, g, b, 255);
    }
    
    public Color(Number grey, Number a)
    {
        this(grey, grey, grey, a);
    }
    
    public Color(Number grey)
    {
        this(grey, grey, grey, 255);
    }
    
    public Color(Colorc color)
    {
        this(color.r(), color.g(), color.b(), color.a());
    }
    
    public Color()
    {
        this(0, 0, 0, 255);
    }
    
    private Color thisOrNew()
    {
        return this;
    }
    
    /**
     * @return the value of the r component
     */
    @Override
    public int r()
    {
        return this.r;
    }
    
    public Color r(Number r)
    {
        this.r = toColorInt(r);
        return this;
    }
    
    /**
     * @return the value of the g component
     */
    @Override
    public int g()
    {
        return this.g;
    }
    
    public Color g(Number g)
    {
        this.g = toColorInt(g);
        return this;
    }
    
    /**
     * @return the value of the b component
     */
    @Override
    public int b()
    {
        return this.b;
    }
    
    public Color b(Number b)
    {
        this.b = toColorInt(b);
        return this;
    }
    
    /**
     * @return the value of the a component
     */
    @Override
    public int a()
    {
        return this.a;
    }
    
    public Color a(Number a)
    {
        this.a = toColorInt(a);
        return this;
    }
    
    public Color set(Number r, Number g, Number b, Number a)
    {
        return r(r).g(g).b(b).a(a);
    }
    
    public Color set(Number r, Number g, Number b)
    {
        return set(r, g, b, 255);
    }
    
    public Color set(Number grey, Number a)
    {
        return set(grey, grey, grey, a);
    }
    
    public Color set(Number grey)
    {
        return set(grey, grey, grey, 255);
    }
    
    public Color set(Colorc p)
    {
        return set(p.r(), p.g(), p.b(), p.a());
    }
    
    /**
     * Get the value of the specified component of this color.
     *
     * @param component the component, within <code>[0..3]</code>
     * @return the value
     * @throws IllegalArgumentException if <code>component</code> is not within <code>[0..3]</code>
     */
    @Override
    public int get(int component) throws IllegalArgumentException
    {
        switch (component)
        {
            case 0:
                return this.r;
            case 1:
                return this.g;
            case 2:
                return this.b;
            case 3:
                return this.a;
            default:
                throw new IllegalArgumentException();
        }
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Colorc)) return false;
        Colorc color = (Colorc) o;
        return equals(color.r(), color.g(), color.b(), color.a());
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(r(), g(), b(), a());
    }
    
    @Override
    public String toString()
    {
        return "Color{r=" + r() + ", g=" + g() + ", b=" + b() + ", a=" + a() + "}";
    }
    
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
    @Override
    public boolean equals(int r, int g, int b, int a)
    {
        return r() == r && g() == g && b() == b && a() == a;
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
    @Override
    public Color blend(int r, int g, int b, int a, IBlend func, Color dest)
    {
        return null;
    }
    
    /**
     * @return 32-bit integer representation of the color
     */
    @Override
    public int toInt()
    {
        return r() | (g() << 8) | (b() << 16) | (a() << 24);
    }
    
    /**
     * @return the hue of the color [0..359]
     */
    public int hue()
    {
        if (r() == g() && g() == b()) return 0;
    
        int max = maxComponent();
        int mid = midComponent();
        int min = minComponent();
    
        int mix = 255 / 6 * (mid - min) * 255 / (max - min);
    
        switch (maxComponentIndex())
        {
            case 0:
                return 6 * 255 / 6 + (minComponentIndex() == 2 ? -mix : mix);
            case 1:
                return 2 * 255 / 6 + (minComponentIndex() == 0 ? -mix : mix);
            case 2:
                return 4 * 255 / 6 + (minComponentIndex() == 1 ? -mix : mix);
        }
        return 0;
    }
    
    /**
     * @return the saturation of the color [0..255]
     */
    public int saturation()
    {
        if (r() == g() && g() == b()) return 0;
    
        int max = maxComponent();
        int min = minComponent();
    
        return 255 - (min * 255 / max);
    }
    
    /**
     * @return the luminosity of the color
     */
    @Override
    public int brightness()
    {
        return (int) (Math.sqrt(r() * r() * PR + g() * g() * PG + b() * b() * PB));
    }
    
    /**
     * Determine the component with the biggest absolute value.
     *
     * @return the component, within <code>[0..255]</code>
     */
    @Override
    public int maxComponent()
    {
        return get(maxComponentIndex());
    }
    
    /**
     * Determine the component with the middle (towards zero) absolute value.
     *
     * @return the component, within <code>[0..255]</code>
     */
    public int midComponent()
    {
        return get(midComponentIndex());
    }
    
    /**
     * Determine the component with the smallest (towards zero) absolute value.
     *
     * @return the component, within <code>[0..255]</code>
     */
    @Override
    public int minComponent()
    {
        return get(minComponentIndex());
    }
    
    /**
     * Determine the component with the biggest absolute value.
     *
     * @return the component index, within <code>[0..2]</code>
     */
    @Override
    public int maxComponentIndex()
    {
        if (r() >= g() && r() >= b()) return 0;
        if (g() >= r() && g() >= b()) return 1;
        if (b() >= r() && b() >= g()) return 2;
        return 0;
    }
    
    /**
     * Determine the component with the middle (towards zero) absolute value.
     *
     * @return the component index, within <code>[0..2]</code>
     */
    public int midComponentIndex()
    {
        int min = minComponentIndex();
        int max = maxComponentIndex();
        if (min == 0) return max == 1 ? 2 : 1;
        if (min == 1) return max == 0 ? 2 : 0;
        if (min == 2) return max == 0 ? 1 : 0;
        return 0;
    }
    
    /**
     * Determine the component with the smallest (towards zero) absolute value.
     *
     * @return the component index, within <code>[0..2]</code>
     */
    @Override
    public int minComponentIndex()
    {
        if (r() <= g() && r() <= b()) return 0;
        if (g() <= r() && g() <= b()) return 1;
        if (b() <= r() && b() <= g()) return 2;
        return 0;
    }
    
    /**
     * Negate this color.
     *
     * @return a color holding the result
     */
    public Color negate()
    {
        return negate(thisOrNew());
    }
    
    /**
     * Negate this color and store the result in <code>dest</code>.
     *
     * @param dest will hold the result
     * @return dest
     */
    @Override
    public Color negate(Color dest)
    {
        dest.r(255 - r());
        dest.g(255 - g());
        dest.b(255 - b());
        return dest;
    }
    
    /**
     * Scales this color in place
     *
     * @param x scale
     * @return this
     */
    public Color scale(double x)
    {
        return scale(x, thisOrNew());
    }
    
    /**
     * Scales this color in place
     *
     * @param x     scale
     * @param alpha flag to scale the alpha (default: false)
     * @return this
     */
    public Color scale(double x, boolean alpha)
    {
        return scale(x, alpha, thisOrNew());
    }
    
    /**
     * Scales this color and stores the result in <code>dest</code>.
     *
     * @param x     scale
     * @param alpha flag to scale the alpha (default: false)
     * @param dest  will hold the result
     * @return dest
     */
    public Color scale(double x, boolean alpha, Color dest)
    {
        dest.r((int) (r() * x));
        dest.g((int) (g() * x));
        dest.b((int) (b() * x));
        if (alpha) dest.a((int) (a() * x));
        return dest;
    }
    
    /**
     * Blend this color with another color in place
     *
     * @param other the other color
     * @return dest
     */
    public Color blend(Color other)
    {
        return blend(other, thisOrNew());
    }
    
    /**
     * Negate this color and store the result in <code>dest</code>.
     *
     * @param other the other color
     * @param func  the function that will blend the two colors
     * @return dest
     */
    public Color blend(Color other, IBlend func)
    {
        return blend(other, func, thisOrNew());
    }
    
    /**
     * Negate this color and store the result in <code>dest</code>.
     *
     * @param other  the other color
     * @param func   the function that will blend the two colors
     * @param result will hold the result
     * @return dest
     */
    public Color blend(Color other, IBlend func, Color result)
    {
        return func.blend(this, other, result);
    }
    
    /**
     * Sets this color to the value described by a 32-bit integer.
     *
     * @param x the 32-bit integer
     * @return this
     */
    public Color fromInt(int x)
    {
        return set(x, x >> 8, x >> 16, x >> 24);
    }
    
    private static int toColorInt(Number x)
    {
        int value = x instanceof Float ? (int) ((float) x * 255) : x instanceof Double ? (int) ((double) x * 255) : (int) x;
        // if (value > 255) value = 255;
        // if (value < 0) value = 0;
        return value & 0xFF;
    }
    
    public static Color random(int lower, int upper, boolean alpha, Color out)
    {
        out.r(PixelEngine.random().nextInt(lower, upper));
        out.g(PixelEngine.random().nextInt(lower, upper));
        out.b(PixelEngine.random().nextInt(lower, upper));
        if (alpha) out.a(PixelEngine.random().nextInt(lower, upper));
        return out;
    }
    
    public static Color random(int lower, int upper, Color out)
    {
        return random(lower, upper, false, out);
    }
    
    public static Color random(int lower, int upper, boolean alpha)
    {
        return random(lower, upper, alpha, new Color());
    }
    
    public static Color random(int lower, int upper)
    {
        return random(lower, upper, false, new Color());
    }
    
    public static Color random(int upper, Color out)
    {
        return random(0, upper, out);
    }
    
    public static Color random(int upper, boolean alpha)
    {
        return random(0, upper, alpha, new Color());
    }
    
    public static Color random(int upper)
    {
        return random(0, upper, new Color());
    }
    
    public static Color random(Color out)
    {
        return random(0, 255, out);
    }
    
    public static Color random(boolean alpha)
    {
        return random(0, 255, alpha, new Color());
    }
    
    public static Color random()
    {
        return random(0, 255, false, new Color());
    }
}
