package pe.event;

import pe.Keyboard;

public class EventKeyHeld extends Event
{
    public EventKeyHeld(Object[] values)
    {
        super(new String[] {""}, values);
    }
    
    public Keyboard.Key button()
    {
        return (Keyboard.Key) this.values[0];
    }
}
