package pe;

public abstract class PEX
{
    protected final Profiler profiler;
    
    public PEX(Profiler profiler)
    {
        this.profiler = profiler;
    }
    
    public abstract void beforeSetup();
    
    public abstract void afterSetup();
    
    public abstract void beforeDraw(double elapsedTime);
    
    public abstract void afterDraw(double elapsedTime);
    
    public abstract void beforeDestroy();
    
    public abstract void afterDestroy();
}
