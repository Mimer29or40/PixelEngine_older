package pe.color;

public enum BlendEquation
{
    ADD(Integer::sum),
    
    SUBTRACT((s, d) -> s - d),
    REVERSE_SUBTRACT((s, d) -> d - s),
    
    MIN(Math::min),
    MAX(Math::max),
    ;
    
    private final IBlendEquation function;
    
    BlendEquation(IBlendEquation function)
    {
        this.function = function;
    }
    
    public int apply(int s, int d)
    {
        return this.function.apply(s, d);
    }
    
    private interface IBlendEquation
    {
        int apply(int s, int d);
    }
}
