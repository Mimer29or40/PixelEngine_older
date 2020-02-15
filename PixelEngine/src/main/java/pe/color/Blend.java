package pe.color;

import static pe.PixelEngine.println;

public class Blend
{
    public static final IBlend ALPHA = (rb, gb, bb, ab, rs, gs, bs, as, dest) -> {
        int a = ab * (255 - as);
        dest.r((rs * as + rb * a) / 255);
        dest.g((gs * as + gb * a) / 255);
        dest.b((bs * as + bb * a) / 255);
        dest.a((as + a) / 255);
        return dest;
    };
    
    public static final ISeparableBlend NORMAL      = (cb, cs) -> cs;
    public static final ISeparableBlend MULTIPLY    = (cb, cs) -> cb * cs;
    public static final ISeparableBlend SCREEN      = (cb, cs) -> 1 - ((1 - cb) * (1 - cs));
    public static final ISeparableBlend DARKEN      = Math::min;
    public static final ISeparableBlend LIGHTEN     = Math::max;
    public static final ISeparableBlend COLOR_DODGE = (cb, cs) -> cb == 0 ? 0 : cs == 1 ? 1 : Math.min(1, cb / (1 - cs));
    public static final ISeparableBlend COLOR_BURN  = (cb, cs) -> cb == 1 ? 1 : cs == 0 ? 0 : 1 - Math.min(1, (1 - cb) / cs);
    public static final ISeparableBlend HARD_LIGHT  = (cb, cs) -> cs <= 0.5 ? MULTIPLY.blendValue(cb, 2 * cs) : SCREEN.blendValue(cb, 2 * cs - 1);
    public static final ISeparableBlend SOFT_LIGHT  = (cb, cs) -> {
        if (cs <= 0.5)
        {
            return cb - (1 - 2 * cs) * cb * (1 - cb);
        }
        else
        {
            double d = cb <= 0.25 ? ((16 * cb - 12) * cb + 4) * cb : Math.sqrt(cb);
            return cb + (2 * cs - 1) * (d - cb);
        }
    };
    public static final ISeparableBlend OVERLAY     = (cb, cs) -> HARD_LIGHT.blendValue(cs, cb);
    public static final ISeparableBlend DIFFERENCE  = (cb, cs) -> Math.abs(cb - cs);
    public static final ISeparableBlend EXCLUSION   = (cb, cs) -> cb + cs - 2 * cb * cs;
    
    public static void main(String[] args)
    {
        println(NORMAL.blend(200, 200, 200, 200, 100, 100, 100, 100, new Color()));
        println(MULTIPLY.blend(200, 200, 200, 200, 100, 100, 100, 100, new Color()));
        println(SCREEN.blend(200, 200, 200, 200, 100, 100, 100, 100, new Color()));
        println(OVERLAY.blend(200, 200, 200, 200, 100, 100, 100, 100, new Color()));
        println(DARKEN.blend(200, 200, 200, 200, 100, 100, 100, 100, new Color()));
        println(LIGHTEN.blend(200, 200, 200, 200, 100, 100, 100, 100, new Color()));
        println(COLOR_DODGE.blend(200, 200, 200, 200, 100, 100, 100, 100, new Color()));
        println(COLOR_BURN.blend(200, 200, 200, 200, 100, 100, 100, 100, new Color()));
        println(HARD_LIGHT.blend(200, 200, 200, 200, 100, 100, 100, 100, new Color()));
        println(SOFT_LIGHT.blend(200, 200, 200, 200, 100, 100, 100, 100, new Color()));
        println(DIFFERENCE.blend(200, 200, 200, 200, 100, 100, 100, 100, new Color()));
        println(EXCLUSION.blend(200, 200, 200, 200, 100, 100, 100, 100, new Color()));
    }
}
