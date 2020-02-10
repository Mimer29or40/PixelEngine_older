package pe.event;

import pe.Keyboard;

public class EventKeyDown extends Event
{
    public EventKeyDown(Object[] values)
    {
        super(new String[] {""}, values);
    }
    
    public Keyboard.Key button()
    {
        return (Keyboard.Key) this.values[0];
    }
}
