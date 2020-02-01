package pe.phx2D.util;

/**
 * A simple implementation of an ObservableEvent, represents an event that has occurred in a Subject.
 */
public class Event<T> extends ObservableEvent<T>
{
    public Event(Observable subject, String name, T value)
    {
        super(subject, name, value);
    }
}
