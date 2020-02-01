package pe.phx2D.util;

import java.util.function.Consumer;

public class GenericObserver implements Observer
{
    static
    {
        Printable.addElement(GenericObserver.class, "subject", v -> v.subject, true);
        Printable.addElement(GenericObserver.class, "purpose", v -> v.purpose, true);
    }
    
    private final Subject                   subject;
    private final Consumer<ObservableEvent> observeFn;
    private final String                    purpose;
    
    /**
     * @param subject     the Subject to observe
     * @param observeFn   function to execute when a SubjectEvent is broadcast by Subject
     * @param opt_purpose Describes what this Observer does, for debugging
     */
    public GenericObserver(Subject subject, Consumer<ObservableEvent> observeFn, String opt_purpose)
    {
        this.subject = subject;
        subject.addObserver(this);
        this.observeFn = observeFn;
        this.purpose = opt_purpose;
    }
    
    public GenericObserver(Subject subject, Consumer<ObservableEvent> observeFn)
    {
        this(subject, observeFn, "");
    }
    
    /**
     * Disconnects this GenericObserver from the Subject.
     */
    public void disconnect()
    {
        this.subject.removeObserver(this);
    }
    
    @Override
    public void observe(ObservableEvent event)
    {
        this.observeFn.accept(event);
    }
}
