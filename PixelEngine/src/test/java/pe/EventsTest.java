package pe;

import pe.event.Event;
import pe.event.EventWindowFullscreen;
import pe.event.EventWindowMoved;
import pe.event.EventWindowResized;

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
    
        drawLine(0, 0, Mouse.x(), Mouse.y());
        // for (Event event : Events.get(EventMouseDragged.class, EventButtonDown.class))
        // for (Event event : Events.get(EventKeyTyped.class))
        for (Event event : Events.get(EventWindowFullscreen.class, EventWindowMoved.class, EventWindowResized.class))
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
