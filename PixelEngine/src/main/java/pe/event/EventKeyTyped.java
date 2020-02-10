package pe.event;

public class EventKeyTyped extends Event
{
    public EventKeyTyped(Object[] values)
    {
        super(new String[] {"char"}, values);
    }
    
    public char charTyped()
    {
        return (char) this.values[0];
    }
}
