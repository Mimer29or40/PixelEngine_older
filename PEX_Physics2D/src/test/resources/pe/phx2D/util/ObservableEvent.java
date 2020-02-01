package pe.phx2D.util;

/**
 * Provides information about an event that has happened to a {@link Observable}. An ObservableEvent has a name, a value, and can identify which Subject broadcast the event.
 *
 * @see Observable
 */
public abstract class ObservableEvent<T> extends Nameable
{
    static
    {
        Printable.addElement(ObservableEvent.class, "subject", ObservableEvent::getSubject, false);
        Printable.addElement(ObservableEvent.class, "name", ObservableEvent::getName, true);
        Printable.addElement(ObservableEvent.class, "value", ObservableEvent::getValue, false);
    }
    
    private Observable subject;
    private T          value;
    
    /**
     * @param subject The Subject to which this ObservableEvent refers.
     * @param name    Name of this ObservableEvent, either the language-independent name for scripting purposes or the localized name for display to user.
     * @param value   The value of this ObservableEvent, or null if there is no assigned value.
     */
    public ObservableEvent(Observable subject, String name, T value)
    {
        super(name);
        
        this.subject = subject;
        this.value = value;
    }
    
    @Override
    public String toString()
    {
        return toLongString();
    }
    
    public Observable getSubject()
    {
        return this.subject;
    }
    
    public T getValue()
    {
        return this.value;
    }
}
