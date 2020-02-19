package pe.color;

@SuppressWarnings("unused")
public class Blend implements IBlend
{
    private BlendFunc     sourceFactor  = BlendFunc.SRC_ALPHA;
    private BlendFunc     destFactor    = BlendFunc.ONE_MINUS_SRC_ALPHA;
    private BlendEquation blendEquation = BlendEquation.ADD;
    
    public BlendFunc sourceFactor()
    {
        return this.sourceFactor;
    }
    
    public BlendFunc destFactor()
    {
        return this.destFactor;
    }
    
    public void blendFunc(BlendFunc sourceFactor, BlendFunc destFactor)
    {
        this.sourceFactor = sourceFactor;
        this.destFactor   = destFactor;
    }
    
    public BlendEquation blendEquation()
    {
        return this.blendEquation;
    }
    
    public void blendEquation(BlendEquation blendEquation)
    {
        this.blendEquation = blendEquation;
    }
    
    public Color blend(int rs, int gs, int bs, int as, int rd, int gd, int bd, int ad, Color result)
    {
        int sr = this.sourceFactor.apply(rs, as, rd, ad);
        int sg = this.sourceFactor.apply(gs, as, gd, ad);
        int sb = this.sourceFactor.apply(bs, as, bd, ad);
        int sa = this.sourceFactor.apply(as, as, ad, ad);
        
        int dr = this.destFactor.apply(rs, as, rd, ad);
        int dg = this.destFactor.apply(gs, as, gd, ad);
        int db = this.destFactor.apply(bs, as, bd, ad);
        int da = this.destFactor.apply(as, as, ad, ad);
        
        result.r(this.blendEquation.apply(rs * sr, rd * dr) / 255);
        result.g(this.blendEquation.apply(gs * sg, gd * dg) / 255);
        result.b(this.blendEquation.apply(bs * sb, bd * db) / 255);
        result.a(this.blendEquation.apply(as * sa, ad * da) / 255);
        
        return result;
    }
}
