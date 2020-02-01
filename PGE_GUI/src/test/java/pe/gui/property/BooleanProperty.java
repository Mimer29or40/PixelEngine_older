package pe.gui.property;

public class BooleanProperty extends Property<Boolean>
{
    public BooleanProperty()
    {
        super();
    }
    
    public BooleanProperty(boolean initial)
    {
        super(initial);
    }
    
    @Override
    protected Boolean init(Boolean initial)
    {
        return initial != null ? initial : false;
    }
    
    @Override
    protected Boolean assignPrev(Boolean prev, Boolean value)
    {
        return value;
    }
    
    @Override
    protected Boolean assignTemp(Boolean temp, Boolean newValue)
    {
        return newValue;
    }
    
    @Override
    protected Boolean assignValue(Boolean value, Boolean newValue)
    {
        return newValue;
    }
}
