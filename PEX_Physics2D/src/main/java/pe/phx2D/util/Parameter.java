package pe.phx2D.util;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Provides access to a value of a {@link Observable} and meta-data such as name, a set of possible values and more. Part of the [Observer design pattern]
 * (http://en.wikipedia.org/wiki/Observer_pattern) which ensures that change notification happens regardless of how the value was changed.
 * <p>
 * It is very easy to connect a Parameter to a user interface control like {@link NumericControl}, {@link ChoiceControl}, or {@link CheckBoxControl}.
 * <p>
 * Parameter helps to minimize the knowledge that classes have about each other. For example, a NumericControl can display and modify the ParameterNumber of a Subject without
 * knowing anything about the Subject other than it implements the Subject interface.
 * <p>
 * <p>
 * Getter and Setter Methods
 * -------------------------
 * A Parameter operates by calling *getter* and *setter* methods on its Subject. These methods are specified to the Parameter's constructor, and used in the Parameter'
 * {@link Parameter#getValue} and {@link Parameter#setValue} methods. We assume that the Subject's *setter* method will perform notification of changes via
 * {@link Observable#broadcastParameter}.
 * <p>
 * Here are examples of *getter* and *setter* methods showing how the Parameter is broadcast in the *setter* method of the Subject.
 * <p>
 * public double getMass()
 * {
 * return this.block.getMass();
 * }
 * <p>
 * public void setMass(double value)
 * {
 * this.block.setMass(value);
 * this.broadcastParameter(SingleSpringSim.en.MASS);
 * }
 * <p>
 * Here is an example showing how the *getter* and *setter* methods are specified when creating a ParameterNumber. This is from the {@link SingleSpringSim} constructor:
 * <p>
 * addParameter(new ParameterNumber(this, SingleSpringSim.en.MASS, SingleSpringSim.i18n.MASS, this::getMass, this::setMass));
 * <p>
 * <p>
 * Choices and Values
 * ------------------
 * A Parameter can have a set of choices and values. If they are specified, then the Parameter value is only allowed to be set to one of those values.
 * <p>
 * + {@link Parameter#getValues} returns a list of *values* that the Parameter can be set to.
 * <p>
 * + {@link Parameter#getChoices} returns a corresponding list of localized (translated) strings which are shown to the user as the *choices* for this Parameter, typically in a
 * user interface menu such as {@link ChoiceControl}.
 * <p>
 * When the set of Parameter choices is changed, a GenericEvent should be broadcast with the name {@link Parameter#CHOICES_MODIFIED}. Then any control that is displaying the
 * available choices can update its display.
 */
public abstract class Parameter<T> extends ObservableEvent<T>
{
    /**
     * Name of event signifying that the set of values and choices returned by {@link Parameter#getValues} and {@link Parameter#getChoices} has been modified: choices may have
     * been added or removed, or the name of choices changed.
     */
    public static final String CHOICES_MODIFIED = "CHOICES_MODIFIED";
    
    protected Supplier<T> getter;
    protected Consumer<T> setter;
    protected boolean     isComputed;
    protected Set<String> choices;
    protected List<T>     values;
    
    /**
     * @param subject     The Subject to which this ObservableEvent refers.
     * @param name        The name of this Parameter, either the language-independent name for scripting purposes or the localized name for display to user.
     * @param getter      A function with no arguments that returns the value of this Parameter
     * @param setter      A function with one argument that sets the value of this Parameter
     * @param opt_choices The strings corresponding to the values (optional)
     * @param opt_values  The values corresponding to the choices that the parameter can be set to (optional)
     */
    public Parameter(Observable subject, String name, Supplier<T> getter, Consumer<T> setter, Collection<String> opt_choices, Collection<T> opt_values)
    {
        super(subject, name, null);
        
        this.getter = getter;
        this.setter = setter;
        this.isComputed = false;
        this.choices = new HashSet<>();
        this.values = new ArrayList<>();
        
        if (opt_choices != null)
        {
            if (opt_values == null) throw new RuntimeException("values not defined");
            setChoicesImpl(opt_choices, opt_values);
        }
    }
    
    public Parameter(Observable subject, String name, Supplier<T> getter, Consumer<T> setter)
    {
        this(subject, name, getter, setter, null, null);
    }
    
    @Override
    public T getValue()
    {
        return this.getter.get();
    }
    
    /**
     * Sets the value of this Parameter.
     *
     * @param value the value to set this Parameter to
     */
    public void setValue(T value)
    {
        if (!getValue().equals(value))
        {
            this.setter.accept(value);
        }
    }
    
    /**
     * Sets the value of this Parameter after converting the given string to the
     * appropriate type (boolean, number or string).
     *
     * @param value the value to set this Parameter to, in string form
     *
     * @throws RuntimeException if the string cannot be converted to the needed type
     */
    public abstract void setFromString(String value);
    
    /**
     * Returns whether the value is being automatically computed; i.e. setting the value of this Parameter has no effect.
     * <p>
     * Examples of automatically computed Parameters: the variables that give the current energy of a simulation. Another example is when the size of a graph's SimView is under
     * control of an {@link AutoScale}.
     *
     * @return whether the value is being automatically computed
     */
    public boolean isComputed()
    {
        return this.isComputed;
    }
    
    /**
     * Sets whether the value is being automatically computed.
     *
     * @param value whether the value is being automatically computed.
     *
     * @see Parameter#isComputed
     */
    public void setComputed(boolean value)
    {
        this.isComputed = value;
    }
    
    /**
     * Returns the strings corresponding to the possible values from {@link Parameter#getValues}.
     *
     * @return the strings corresponding to the possible values
     */
    public Set<String> getChoices()
    {
        return new HashSet<>(this.choices);
    }
    
    /**
     * Returns the set of values corresponding to {@link #getChoices} that this Parameter
     * can be set to.
     *
     * @return set of values that this Parameter can be set to, in string form.
     */
    public List<T> getValues()
    {
        return new ArrayList<>(this.values);
    }
    
    /**
     * Sets the choices and values associated with this Parameter.
     *
     * @param choices strings giving name of each choice
     * @param values  the values corresponding to each choice
     *
     * @throws RuntimeException if {@param choices} is of different length than {@param values}
     */
    public void setChoices(Collection<String> choices, Collection<T> values)
    {
        setChoicesImpl(choices, values);
        getSubject().broadcast(new Event<>(getSubject(), Parameter.CHOICES_MODIFIED, this));
    }
    
    /**
     * Sets the choices and values associated with this ParameterBoolean.
     *
     * @param choices strings giving name of each choice
     * @param values  the boolean values corresponding to each choice
     *
     * @throws RuntimeException if {@param choices} is of different length than {@param values}
     */
    protected void setChoicesImpl(Collection<String> choices, Collection<T> values)
    {
        if (values.size() != choices.size()) throw new RuntimeException("choices and values not same length");
        this.choices.clear();
        this.choices.addAll(choices);
        this.values.clear();
        this.values.addAll(values);
    }
}
