package pe.event;

import pe.Keyboard;

public class EventKeyPressed extends Event
{
    public EventKeyPressed(Object[] values)
    {
        super(new String[] {""}, values);
    }
    
    public Keyboard.Key getButton()
    {
        return (Keyboard.Key) this.values[0];
    }
}
