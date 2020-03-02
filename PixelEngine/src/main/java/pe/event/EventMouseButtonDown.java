package pe.event;

import org.joml.Vector2i;
import org.joml.Vector2ic;
import pe.Mouse;

public class EventMouseButtonDown extends Event
{
    public EventMouseButtonDown(Object[] values)
    {
        super(new String[] {"", "pos"}, values);
    }
    
    public Mouse.Button button()
    {
        return (Mouse.Button) this.values[0];
    }
    
    public Vector2ic pos()
    {
        return (Vector2i) this.values[1];
    }
    
    public int x()
    {
        return pos().x();
    }
    
    public int y()
    {
        return pos().y();
    }
}
