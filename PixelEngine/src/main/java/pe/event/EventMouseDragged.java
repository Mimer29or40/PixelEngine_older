package pe.event;

import pe.Mouse;

public class EventMouseDragged extends Event
{
    public EventMouseDragged(Object[] values)
    {
        super(new String[] {"", "dragX", "dragY", "x", "y", "relX", "relY"}, values);
    }
    
    public Mouse.Button button()
    {
        return (Mouse.Button) this.values[0];
    }
    
    public int dragX()
    {
        return (int) this.values[1];
    }
    
    public int dragY()
    {
        return (int) this.values[2];
    }
    
    public int x()
    {
        return (int) this.values[3];
    }
    
    public int y()
    {
        return (int) this.values[4];
    }
    
    public int relX()
    {
        return (int) this.values[5];
    }
    
    public int relY()
    {
        return (int) this.values[6];
    }
}
