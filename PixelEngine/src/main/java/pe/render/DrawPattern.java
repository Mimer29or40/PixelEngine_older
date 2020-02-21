package pe.render;

@SuppressWarnings("unused")
public class DrawPattern
{
    private final int initial;
    private       int pattern;
    
    public DrawPattern(int pattern)
    {
        this.initial = this.pattern = pattern;
    }
    
    public void reset()
    {
        this.pattern = this.initial;
    }
    
    public boolean shouldDraw()
    {
        this.pattern = (this.pattern << 1) | (this.pattern >>> 31);
        return (this.pattern & 1) != 0;
    }
    
    public static final DrawPattern SOLID  = new DrawPattern(0xFFFFFFFF);
    public static final DrawPattern DOTTED = new DrawPattern(0xAAAAAAAA);
    public static final DrawPattern DASHED = new DrawPattern(0xF0F0F0F0);
}
