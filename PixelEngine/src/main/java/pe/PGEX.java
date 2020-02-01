package pe;

public abstract class PGEX
{
    protected final Profiler profiler;
    
    protected PGEX(Profiler profiler)
    {
        this.profiler = profiler;
    }
    
    public abstract void initialize();
    
    public abstract void beforeUserUpdate(double elapsedTime);
    
    public abstract void afterUserUpdate(double elapsedTime);
    
    public abstract void destroy();
}
