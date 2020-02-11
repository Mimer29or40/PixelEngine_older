package pe.neat;

public class Counter
{
    private int value;
    
    public Counter(int start)
    {
        this.value = start;
    }
    
    public Counter()
    {
        this(0);
    }
    
    public int get()
    {
        return this.value;
    }
    
    public int inc()
    {
        return this.value++;
    }
}
