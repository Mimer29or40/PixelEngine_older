package pe.color;

public interface IBlend
{
    Color blend(int rb, int gb, int bb, int ab, int rs, int gs, int bs, int as, Color dest);
    
    default Color blend(int rb, int gb, int bb, int ab, Colorc s, Color dest)
    {
        return blend(rb, gb, bb, ab, s.r(), s.g(), s.b(), s.a(), dest);
    }
    
    default Color blend(Colorc b, int rs, int gs, int bs, int as, Color dest)
    {
        return blend(b.r(), b.g(), b.b(), b.a(), rs, gs, bs, as, dest);
    }
    
    default Color blend(Colorc b, Colorc s, Color dest)
    {
        return blend(b.r(), b.g(), b.b(), b.a(), s.r(), s.g(), s.b(), s.a(), dest);
    }
}
