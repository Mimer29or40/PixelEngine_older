package pe.event;

import pe.Mouse;

public class EventMouseButtonDown extends Event
{
    public EventMouseButtonDown(Object[] values)
    {
        super(new String[] {"", "x", "y"}, values);
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
}
