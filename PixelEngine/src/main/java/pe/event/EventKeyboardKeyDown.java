package pe.event;

import pe.Keyboard;

public class EventKeyboardKeyDown extends Event
{
    public EventKeyboardKeyDown(Object[] values)
    {
        super(new String[] {""}, values);
    }
    
    public Keyboard.Key key()
    {
        return (Keyboard.Key) this.values[0];
    }
}
