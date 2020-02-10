package pe.event;

public class EventMouseMoved extends Event
{
    public EventMouseMoved(Object[] values)
    {
        super(new String[] {"x", "y", "relX", "relY"}, values);
    }
    
    public int x()
    {
        return (int) this.values[0];
    }
    
    public int y()
    {
        return (int) this.values[1];
    }
    
    public int relX()
    {
        return (int) this.values[2];
    }
    
    public int relY()
    {
        return (int) this.values[3];
    }
}
