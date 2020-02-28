package pe;

import pe.event.Event;

import java.util.ArrayList;
import java.util.List;

public class EventsTest extends PixelEngine
{
    static List<String> events = new ArrayList<>();
    
    static void addEvent(String event)
    {
        events.add(event);
        events.remove(0);
    }
    
    @Override
    protected boolean setup()
    {
        int lines = (screenHeight() - 2) / 8;
        for (int i = 0; i < lines; i++) events.add("");
    
        Events.subscribe(Events.INPUT_EVENTS, this::onInputEvent);
    
        return true;
    }
    
    public void onInputEvent(Event event)
    {
        addEvent(event.toString());
    }
    
    @Override
    protected boolean draw(double elapsedTime)
    {
        renderer().clear();
        
        int nLog = 0;
        for (String s : events)
        {
            int c = (int) map(nLog, 0, events.size() - 1, 60, 255);
            renderer().stroke(c, c, c);
            renderer().text(2, nLog * 8 + 2, s);
            nLog++;
        }
    
        // for (Event event : Events.get())
        // for (Event event : Events.get(EventMouseButtonDragged.class, EventMouseButtonDown.class))
        // for (Event event : Events.get(EventKeyboardKeyTyped.class))
        // for (Event event : Events.get(Events.MOUSE_EVENTS))
        // {
        //     println(event.toString());
        // }
    
        if (Keyboard.SPACE.down()) Window.fullscreen(!Window.fullscreen());
    
        return true;
    }
    
    public static void main(String[] args)
    {
        start(new EventsTest(), 400, 200);
    }
}
