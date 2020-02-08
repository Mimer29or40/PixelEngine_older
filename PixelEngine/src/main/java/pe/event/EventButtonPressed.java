package pe.event;

import pe.Mouse;

public class EventButtonPressed extends Event
{
    public EventButtonPressed(Object[] values)
    {
        super(new String[] {"", "x", "y"}, values);
    }
    
    public Mouse.Button getButton()
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
