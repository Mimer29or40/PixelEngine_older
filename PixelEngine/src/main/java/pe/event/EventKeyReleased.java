package pe.event;

import pe.Keyboard;

public class EventKeyReleased extends Event
{
    public EventKeyReleased(Object[] values)
    {
        super(new String[] {""}, values);
    }
    
    public Keyboard.Key getButton()
    {
        return (Keyboard.Key) this.values[0];
    }
}
