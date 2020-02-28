package pe;

public abstract class PEX
{
    protected final Profiler profiler;
    
    protected boolean enabled = true;
    
    public PEX(Profiler profiler)
    {
        this.profiler = profiler;
    }
    
    public void enable()
    {
        this.enabled = true;
    }
    
    public void disable()
    {
        this.enabled = false;
    }
    
    public boolean isEnabled()
    {
        return this.enabled;
    }
    
    public abstract void beforeSetup();
    
    public abstract void afterSetup();
    
    public abstract void beforeDraw(double elapsedTime);
    
    public abstract void afterDraw(double elapsedTime);
    
    public abstract void beforeDestroy();
    
    public abstract void afterDestroy();
}
