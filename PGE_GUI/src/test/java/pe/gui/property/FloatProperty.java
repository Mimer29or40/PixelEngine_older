package pe.gui.property;

public class FloatProperty extends Property<Float>
{
    public FloatProperty()
    {
        super();
    }
    
    public FloatProperty(float initial)
    {
        super(initial);
    }
    
    @Override
    protected Float init(Float initial)
    {
        return initial != null ? initial : 0;
    }
    
    @Override
    protected Float assignPrev(Float prev, Float value)
    {
        return value;
    }
    
    @Override
    protected Float assignTemp(Float temp, Float newValue)
    {
        return newValue;
    }
    
    @Override
    protected Float assignValue(Float value, Float newValue)
    {
        return newValue;
    }
}
