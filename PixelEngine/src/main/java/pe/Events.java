package pe;

import pe.event.Event;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

public class Events
{
    private static final HashMap<Class<? extends Event>, ArrayList<Event>> EVENTS = new HashMap<>();
    
    public static ArrayList<Event> get()
    {
        ArrayList<Event> events = new ArrayList<>();
        Events.EVENTS.values().forEach(events::addAll);
        return events;
    }
    
    @SafeVarargs
    public static ArrayList<Event> get(Class<? extends Event>... eventTypes)
    {
        ArrayList<Event> events = new ArrayList<>();
        for (Class<? extends Event> eventType : eventTypes)
        {
            Events.EVENTS.computeIfAbsent(eventType, e -> new ArrayList<>());
            events.addAll(Events.EVENTS.get(eventType));
        }
        return events;
    }
    
    public static void post(Class<? extends Event> eventType, Object... arguments)
    {
        try
        {
            Events.EVENTS.computeIfAbsent(eventType, e -> new ArrayList<>());
            Events.EVENTS.get(eventType).add(eventType.getDeclaredConstructor(Object[].class).newInstance(new Object[] {arguments}));
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ignored)
        {
        
        }
    }
    
    public static void clear()
    {
        Events.EVENTS.clear();
    }
}
