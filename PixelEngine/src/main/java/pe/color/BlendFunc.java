package pe.color;

public enum BlendFunc
{
    ZERO((cs, as, cd, ad) -> 0),
    ONE((cs, as, cd, ad) -> 255),
    
    SRC_COLOR((cs, as, cd, ad) -> cs),
    ONE_MINUS_SRC_COLOR((cs, as, cd, ad) -> 255 - cs),
    SRC_ALPHA((cs, as, cd, ad) -> as),
    ONE_MINUS_SRC_ALPHA((cs, as, cd, ad) -> 255 - as),
    
    DEST_COLOR((cs, as, cd, ad) -> cd),
    ONE_MINUS_DEST_COLOR((cs, as, cd, ad) -> 255 - cd),
    DEST_ALPHA((cs, as, cd, ad) -> ad),
    ONE_MINUS_DEST_ALPHA((cs, as, cd, ad) -> 255 - ad),
    ;
    
    private final IBlendFunc function;
    
    BlendFunc(IBlendFunc function)
    {
        this.function = function;
    }
    
    public int apply(int cs, int as, int cd, int ad)
    {
        return this.function.apply(cs, as, cd, ad);
    }
    
    private interface IBlendFunc
    {
        int apply(int cs, int as, int cd, int ad);
    }
}
