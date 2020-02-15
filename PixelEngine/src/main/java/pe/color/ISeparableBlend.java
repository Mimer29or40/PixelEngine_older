package pe.color;

public interface ISeparableBlend extends IBlend
{
    double blendValue(double cb, double cs);
    
    default Color blend(int rb, int gb, int bb, int ab, int rs, int gs, int bs, int as, Color dest)
    {
        // dest.r((255 - ab) * rs + ab * (int) (255 * blendValue((double) rb / 255D, (double) rs / 255D)));
        // dest.g((255 - ab) * gs + ab * (int) (255 * blendValue((double) gb / 255D, (double) gs / 255D)));
        // dest.b((255 - ab) * bs + ab * (int) (255 * blendValue((double) bb / 255D, (double) bs / 255D)));
        // dest.a((255 - ab) * as + ab * (int) (255 * blendValue((double) ab / 255D, (double) as / 255D)));
        dest.r(blendValue((double) rb / 255D, (double) rs / 255D));
        dest.g(blendValue((double) gb / 255D, (double) gs / 255D));
        dest.b(blendValue((double) bb / 255D, (double) bs / 255D));
        dest.a(blendValue((double) ab / 255D, (double) as / 255D));
        return dest;
    }
}
