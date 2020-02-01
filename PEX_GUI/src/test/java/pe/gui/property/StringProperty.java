package pe.gui.property;

public class StringProperty extends Property<String>
{
    public StringProperty()
    {
        super();
    }
    
    public StringProperty(String initial)
    {
        super(initial);
    }
    
    @Override
    protected String init(String initial)
    {
        return initial != null ? initial : "";
    }
    
    @Override
    protected String assignPrev(String prev, String value)
    {
        return value;
    }
    
    @Override
    protected String assignTemp(String temp, String newValue)
    {
        return newValue;
    }
    
    @Override
    protected String assignValue(String value, String newValue)
    {
        return newValue;
    }
}
