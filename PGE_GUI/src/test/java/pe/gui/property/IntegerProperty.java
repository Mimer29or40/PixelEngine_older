package pe.gui.property;

public class IntegerProperty extends Property<Integer>
{
    public IntegerProperty()
    {
        super();
    }
    
    public IntegerProperty(int initial)
    {
        super(initial);
    }
    
    @Override
    protected Integer init(Integer initial)
    {
        return initial != null ? initial : 0;
    }
    
    @Override
    protected Integer assignPrev(Integer prev, Integer value)
    {
        return value;
    }
    
    @Override
    protected Integer assignTemp(Integer temp, Integer newValue)
    {
        return newValue;
    }
    
    @Override
    protected Integer assignValue(Integer value, Integer newValue)
    {
        return newValue;
    }
}
