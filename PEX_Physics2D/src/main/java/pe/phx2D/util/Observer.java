package pe.phx2D.util;

/**
 * An Observer is notified whenever something changes in a {@link Observable} it is observing. The change can be in the value of a Subject's {@link Parameter}, or the occurrence
 * of an event such as a {@link Event}. When a change occurs in the Subject, the {@link Observable#broadcast} method calls the Observer's {@link Observer#observe} method.
 * <p>
 * The Observer is connected to the Subject via the {@link Observable#addObserver} method. This is typically done in the Observer's constructor or by the entity that creates the
 * Observer.
 * <p>
 * Implements the [Observer design pattern](http://en.wikipedia.org/wiki/Observer_pattern).
 *
 * @see Observable
 */
public interface Observer extends Printable
{
    /**
     * Notifies this Observer that a change has occurred in the Subject.
     *
     * @param event contains information about what has changed in the Subject: typically either a one-time GenericEvent, or a change to the value of a Parameter
     */
    void observe(ObservableEvent event);
}
