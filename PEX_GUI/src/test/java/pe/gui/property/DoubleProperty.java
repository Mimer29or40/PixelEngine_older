package pe.gui.property;

public class DoubleProperty extends Property<Double>
{
    public DoubleProperty()
    {
    
    }
    
    @Override
    protected Double init(Double initial)
    {
        return initial != null ? initial : 0.0;
    }
    
    public DoubleProperty(double initial)
    {
        set(initial);
    }
    
    @Override
    protected Double assignPrev(Double prev, Double value)
    {
        return value;
    }
    
    @Override
    protected Double assignTemp(Double temp, Double newValue)
    {
        return newValue;
    }
    
    @Override
    protected Double assignValue(Double value, Double newValue)
    {
        return newValue;
    }
}
