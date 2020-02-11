package pe.event;

import pe.Mouse;

public class EventMouseButtonClicked extends Event
{
    public EventMouseButtonClicked(Object[] values)
    {
        super(new String[] {"", "x", "y", "double"}, values);
    }
    
    public Mouse.Button button()
    {
        return (Mouse.Button) this.values[0];
    }
    
    public int x()
    {
        return (int) this.values[1];
    }
    
    public int y()
    {
        return (int) this.values[2];
    }
    
    public boolean doubleClicked()
    {
        return (boolean) this.values[3];
    }
}
