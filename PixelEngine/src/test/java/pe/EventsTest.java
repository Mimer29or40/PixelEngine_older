package pe;

import pe.event.Event;

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
        clear();
    
        // for (Event event : Events.get(EventMouseDragged.class, EventButtonDown.class))
        // for (Event event : Events.get(EventKeyTyped.class))
        for (Event event : Events.get(Events.MOUSE_EVENTS))
        // for (Event event : Events.get())
        {
            print(event.toString());
        }
    
        if (Keyboard.SPACE.down()) Window.fullscreen(!Window.fullscreen());
    
        return true;
    }
    
    public static void main(String[] args)
    {
        start(new EventsTest());
    }
}
