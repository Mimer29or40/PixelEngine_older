package pe.event;

import pe.Keyboard;

public class EventKeyUp extends Event
{
    public EventKeyUp(Object[] values)
    {
        super(new String[] {""}, values);
    }
    
    public Keyboard.Key button()
    {
        return (Keyboard.Key) this.values[0];
    }
}
