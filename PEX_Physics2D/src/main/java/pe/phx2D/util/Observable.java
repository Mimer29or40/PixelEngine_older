package pe.phx2D.util;

import java.util.Set;

/**
 * A Subject notifies its {@link Observer} when something changes in the Subject. This can be a change in the value of a {@link Parameter}, or the occurrence of an
 * {@link GenericEvent}. The Subject maintains a list of its Observers. An Observer is connected to the Subject via the {@link Observable#addObserver} method, which is typically
 * called by the Observer's constructor or the entity that creates the Observer.
 * <p>
 * The Subject and Observer interfaces are an implementation of the [Observer design pattern](http://en.wikipedia.org/wiki/Observer_pattern).
 * <p>
 * When a change occurs in the Subject, the {@link Observable#broadcast} method should be called to inform all Observers of the change. For a Parameter, the "setter" method of the
 * Subject should call {@link Observable#broadcastParameter} at the end of the setter method.
 * <p>
 * <p>
 * Example Scenario
 * ----------------
 * <p>
 * Here is an example usage of the Subject and Observer interfaces, along with Parameters and user interface controls.
 * <p>
 * <img src="Subject_Observer_Parameters.svg" alt="Subject/Observer Relationships with Parameters" />
 * <p>
 * The diagram shows a {@link ContactSim} as the Subject. It has a list of Parameters, including a Parameter representing the distance tolerance which determines when objects are
 * in contact. The Subject also has a list of Observers, including {@link NumericControl} which is connected to the distance tolerance {@link ParameterNumber}. In its constructor,
 * the NumericControl adds itself to the list of Observers by calling {@link Observable#addObserver} on the Subject of the ParameterNumber.
 * <p>
 * Whenever the distance tolerance is changed, the Subject should notify each Observer by calling {@link Observable#broadcast}. The Observer can then get the current value by
 * calling `ParameterNumber.getValue`.
 * <p>
 * This design is very decoupled. The Subject knows nothing about the NumericControl except that it is an Observer. The Parameter is unaware of the NumericControl. The
 * NumericControl only knows about the ParameterNumber and that it has a Subject which will provide notification of changes.
 */
public interface Observable extends Printable
{
    /**
     * Return the name of this Subject for scripting purposes.
     *
     * @return the name of this Subject
     */
    String getName();
    
    /**
     * Adds the given Observer to the Subject's set of Observers, so that the Observer will be notified of changes in this Subject. An Observer may call this during its
     * {@link Observer#observe} method.
     *
     * @param observer the Observer to add
     */
    void addObserver(Observer observer);
    
    /**
     * Removes the Observer from the Subject's set of Observers. An Observer may call this during its {@link Observer#observe} method.
     *
     * @param observer the Observer to detach from list of Observers
     */
    void removeObserver(Observer observer);
    
    /**
     * Notifies all Observers that the Subject has changed by calling {@link Observer#observe} on each Observer. An Observer may call {@link Observable#addObserver} or
     * {@link Observable#removeObserver} during its {@link Observer#observe} method.
     *
     * @param evt an ObservableEvent with information relating to the change
     */
    void broadcast(ObservableEvent evt);
    
    /**
     * Notifies all Observers that the Parameter with the given {@link Parameter#getName} has changed by calling {@link Observer#observe} on each Observer.
     *
     * @param name the language-independent or English name of the Parameter that has changed
     *
     * @throws RuntimeException if there is no Parameter with the given name
     */
    void broadcastParameter(String name);
    
    /**
     * Returns a copy of the set of Observers of this Subject.
     *
     * @return a copy of the set of Observers of this Subject.
     */
    Set<Observer> getObservers();
    
    /**
     * Returns the Parameter with the given name.
     *
     * @param name the language-independent or English name of the Parameter
     *
     * @return the Parameter with the given name
     *
     * @throws RuntimeException if there is no Parameter with the given name
     */
    Parameter getParameter(String name);
    
    /**
     * Returns a copy of the set of this Subject's available Parameters.
     *
     * @return a copy of the set of available Parameters for this Subject
     */
    Set<Parameter> getParameters();
    
    /**
     * Returns the ParameterBoolean with the given name.
     *
     * @param name the language-independent or English name of the ParameterBoolean
     *
     * @return the ParameterBoolean with the given name
     *
     * @throws RuntimeException if there is no ParameterBoolean with the given name
     */
    ParameterBoolean getParameterBoolean(String name);
    
    /**
     * Returns the ParameterNumber with the given name.
     *
     * @param name the language-independent or English name of the ParameterNumber
     *
     * @return the ParameterNumber with the given name
     *
     * @throws RuntimeException if there is no ParameterNumber with the given name
     */
    ParameterNumber getParameterNumber(String name);
    
    /**
     * Returns the ParameterString with the given name.
     *
     * @param name the language-independent or English name of the ParameterString
     *
     * @return the ParameterString with the given name
     *
     * @throws RuntimeException if there is no ParameterString with the given name
     */
    ParameterString getParameterString(String name);
}
