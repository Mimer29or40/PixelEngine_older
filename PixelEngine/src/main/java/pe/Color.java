package pe;

import java.util.Objects;

@SuppressWarnings("unused")
public class Color
{
    public static final Color WHITE            = new Color(255, 255, 255, true);
    public static final Color LIGHT_GREY       = new Color(191, 191, 191, true);
    public static final Color GREY             = new Color(127, 127, 127, true);
    public static final Color DARK_GREY        = new Color(63, 63, 63, true);
    public static final Color BLACK            = new Color(0, 0, 0, true);
    public static final Color BACKGROUND_GREY  = new Color(51, 51, 51, true);
    public static final Color LIGHTEST_RED     = new Color(255, 191, 191, true);
    public static final Color LIGHTER_RED      = new Color(255, 127, 127, true);
    public static final Color LIGHT_RED        = new Color(255, 63, 63, true);
    public static final Color RED              = new Color(255, 0, 0, true);
    public static final Color DARK_RED         = new Color(191, 0, 0, true);
    public static final Color DARKER_RED       = new Color(127, 0, 0, true);
    public static final Color DARKEST_RED      = new Color(63, 0, 0, true);
    public static final Color LIGHTEST_YELLOW  = new Color(255, 255, 191, true);
    public static final Color LIGHTER_YELLOW   = new Color(255, 255, 127, true);
    public static final Color LIGHT_YELLOW     = new Color(255, 255, 63, true);
    public static final Color YELLOW           = new Color(255, 255, 0, true);
    public static final Color DARK_YELLOW      = new Color(191, 191, 0, true);
    public static final Color DARKER_YELLOW    = new Color(127, 127, 0, true);
    public static final Color DARKEST_YELLOW   = new Color(63, 63, 0, true);
    public static final Color LIGHTEST_GREEN   = new Color(191, 255, 191, true);
    public static final Color LIGHTER_GREEN    = new Color(127, 255, 127, true);
    public static final Color LIGHT_GREEN      = new Color(63, 255, 63, true);
    public static final Color GREEN            = new Color(0, 255, 0, true);
    public static final Color DARK_GREEN       = new Color(0, 191, 0, true);
    public static final Color DARKER_GREEN     = new Color(0, 127, 0, true);
    public static final Color DARKEST_GREEN    = new Color(0, 63, 0, true);
    public static final Color LIGHTEST_CYAN    = new Color(191, 255, 255, true);
    public static final Color LIGHTER_CYAN     = new Color(127, 255, 255, true);
    public static final Color LIGHT_CYAN       = new Color(63, 255, 255, true);
    public static final Color CYAN             = new Color(0, 255, 255, true);
    public static final Color DARK_CYAN        = new Color(0, 191, 191, true);
    public static final Color DARKER_CYAN      = new Color(0, 127, 127, true);
    public static final Color DARKEST_CYAN     = new Color(0, 63, 63, true);
    public static final Color LIGHTEST_BLUE    = new Color(191, 191, 255, true);
    public static final Color LIGHTER_BLUE     = new Color(127, 127, 255, true);
    public static final Color LIGHT_BLUE       = new Color(63, 63, 255, true);
    public static final Color BLUE             = new Color(0, 0, 255, true);
    public static final Color DARK_BLUE        = new Color(0, 0, 191, true);
    public static final Color DARKER_BLUE      = new Color(0, 0, 127, true);
    public static final Color DARKEST_BLUE     = new Color(0, 0, 63, true);
    public static final Color LIGHTEST_MAGENTA = new Color(255, 191, 255, true);
    public static final Color LIGHTER_MAGENTA  = new Color(255, 127, 255, true);
    public static final Color LIGHT_MAGENTA    = new Color(255, 63, 255, true);
    public static final Color MAGENTA          = new Color(255, 0, 255, true);
    public static final Color DARK_MAGENTA     = new Color(191, 0, 191, true);
    public static final Color DARKER_MAGENTA   = new Color(127, 0, 127, true);
    public static final Color DARKEST_MAGENTA  = new Color(63, 0, 63, true);
    public static final Color BLANK            = new Color(0, 0, 0, 0, true);
    private             int   r, g, b, a;
    private boolean immutable;
    
    public Color(Number r, Number g, Number b, Number a, boolean immutable)
    {
        r(r).g(g).b(b).a(a);
        
        this.immutable = immutable;
    }
    
    public Color(Number r, Number g, Number b, Number a)
    {
        this(r, b, g, a, false);
    }
    
    public Color(Number r, Number g, Number b, boolean immutable)
    {
        this(r, g, b, 255, immutable);
    }
    
    public Color(Number r, Number g, Number b)
    {
        this(r, g, b, 255, false);
    }
    
    public Color(Number grey, Number a, boolean immutable)
    {
        this(grey, grey, grey, a, immutable);
    }
    
    public Color(Number grey, Number a)
    {
        this(grey, grey, grey, a, false);
    }
    
    public Color(Number grey, boolean immutable)
    {
        this(grey, grey, grey, 255, immutable);
    }
    
    public Color(Number grey)
    {
        this(grey, grey, grey, 255, false);
    }
    
    public Color()
    {
        this(0, 0, 0, 255);
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
    
    private static int toColorInt(Number x)
    {
        int value = x instanceof Float ? (int) ((float) x * 255.0) : x instanceof Double ? (int) ((double) x * 255.0) : (int) x;
        return value & 0xFF;
    }
    
    @Override
    public String toString()
    {
        return String.format("Color[r:%s g:%s b:%s a:%s]", r(), g(), b(), a());
    }
    
    @Override
    public boolean equals(Object o)
    {
        return o instanceof Color && ((Color) o).r() == r() && ((Color) o).g() == g() && ((Color) o).b() == b() && ((Color) o).a() == a();
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(r(), g(), b(), a());
    }
    
    public boolean isImmutable()
    {
        return this.immutable;
    }
    
    public Color setImmutable()
    {
        this.immutable = true;
        return this;
    }
    
    public int r()
    {
        return this.r;
    }
    
    public Color r(Number r)
    {
        if (isImmutable()) throw new UnsupportedOperationException("Color is immutable");
        this.r = toColorInt(r);
        return this;
    }
    
    public int g()
    {
        return this.g;
    }
    
    public Color g(Number g)
    {
        if (isImmutable()) throw new UnsupportedOperationException("Color is immutable");
        this.g = toColorInt(g);
        return this;
    }
    
    public int b()
    {
        return this.b;
    }
    
    public Color b(Number b)
    {
        if (isImmutable()) throw new UnsupportedOperationException("Color is immutable");
        this.b = toColorInt(b);
        return this;
    }
    
    public int a()
    {
        return this.a;
    }
    
    public Color a(Number a)
    {
        if (isImmutable()) throw new UnsupportedOperationException("Color is immutable");
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
    
    public Color set(Color p)
    {
        return set(p.r(), p.g(), p.b(), p.a());
    }
    
    public Color fromInt(int x)
    {
        return set(x, x >> 8, x >> 16, x >> 24);
    }
    
    public int toInt()
    {
        return r() | (g() << 8) | (b() << 16) | (a() << 24);
    }
    
    public Color copy(Color out)
    {
        return out.set(this);
    }
    
    public Color copy()
    {
        return copy(new Color());
    }
    
    public Color scale(double x)
    {
        return r((int) (r() * x)).g((int) (g() * x)).b((int) (b() * x)).a((int) (a() * x));
    }
    
    public Color blend(Color other, IBlend func, Color out)
    {
        return out.set(func.blend(this, other));
    }
    
    public Color blend(Color other, IBlend func)
    {
        return blend(other, func, new Color());
    }
    
    public Color blend(Color other, Color out)
    {
        double a = (double) a() / 255.0;
        double c = 1.0 - a;
        out.r(a * r() + c * other.r());
        out.g(a * g() + c * other.g());
        out.b(a * b() + c * other.b());
        out.a(a * a() + c * other.a());
        return out;
    }
    
    public Color blend(Color other)
    {
        return blend(other, new Color());
    }
}
