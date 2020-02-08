package pe.event;

import pe.Keyboard;

public class EventKeyRepeated extends Event
{
    public EventKeyRepeated(Object[] values)
    {
        super(new String[] {""}, values);
    }
    
    public Keyboard.Key getButton()
    {
        return (Keyboard.Key) this.values[0];
    }
}
