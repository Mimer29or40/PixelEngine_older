package pe.gui.property;

import pe.Color;

public class PixelProperty extends Property<Color>
{
    public PixelProperty()
    {
        super();
    }
    
    public PixelProperty(Color initial)
    {
        super(initial);
    }
    
    @Override
    protected Color init(Color initial)
    {
        return initial != null ? initial : new Color();
    }
    
    @Override
    protected Color assignPrev(Color prev, Color value)
    {
        return prev.set(value);
    }
    
    @Override
    protected Color assignTemp(Color temp, Color newValue)
    {
        return temp.set(newValue);
    }
    
    @Override
    protected Color assignValue(Color value, Color newValue)
    {
        return value.set(newValue);
    }
}
