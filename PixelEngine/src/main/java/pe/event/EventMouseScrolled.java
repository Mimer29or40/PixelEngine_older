package pe.event;

public class EventMouseScrolled extends Event
{
    public EventMouseScrolled(Object[] values)
    {
        super(new String[] {"x", "y"}, values);
    }
    
    public int x()
    {
        return (int) this.values[0];
    }
    
    public int y()
    {
        return (int) this.values[1];
    }
}
