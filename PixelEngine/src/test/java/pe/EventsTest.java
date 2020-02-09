package pe;

import pe.event.Event;
import pe.event.EventKeyTyped;

public class EventsTest extends PixelEngine
{
    @Override
    protected boolean onUserCreate()
    {
        return true;
    }
    
    @Override
    protected boolean onUserUpdate(double elapsedTime)
    {
        // for (Event event : Events.get(EventMouseDragged.class, EventButtonDown.class))
        for (Event event : Events.get(EventKeyTyped.class))
        // for (Event event : Events.get())
        {
            print(event.toString());
        }
        return true;
    }
    
    public static void main(String[] args)
    {
        start(new EventsTest());
    }
}
