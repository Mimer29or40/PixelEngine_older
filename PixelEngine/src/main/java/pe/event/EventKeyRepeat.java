package pe.event;

import pe.Keyboard;

public class EventKeyRepeat extends Event
{
    public EventKeyRepeat(Object[] values)
    {
        super(new String[] {""}, values);
    }
    
    public Keyboard.Key button()
    {
        return (Keyboard.Key) this.values[0];
    }
}
