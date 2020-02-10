package pe.event;

public class EventWindowFocused extends Event
{
    public EventWindowFocused(Object[] values)
    {
        super(new String[] {"focused"}, values);
    }
    
    public boolean focused()
    {
        return (boolean) this.values[0];
    }
}
