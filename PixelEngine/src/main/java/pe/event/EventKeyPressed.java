package pe.event;

import pe.Keyboard;

public class EventKeyPressed extends Event
{
    public EventKeyPressed(Object[] values)
    {
        super(new String[] {"", "double"}, values);
    }
    
    public Keyboard.Key button()
    {
        return (Keyboard.Key) this.values[0];
    }
    
    public boolean doublePressed()
    {
        return (boolean) this.values[1];
    }
}
