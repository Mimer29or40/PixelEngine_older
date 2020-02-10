package pe.event;

public class EventMouseEntered extends Event
{
    public EventMouseEntered(Object[] values)
    {
        super(new String[] {"entered"}, values);
    }
    
    public boolean entered()
    {
        return (boolean) this.values[0];
    }
}
