package pe.phx2D.util;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Provides access to a numeric value of a {@link Observable}. Has options for setting number of significant digits to show, and upper/lower limit on value. Default is 3
 * significant digits, lower limit of zero, and upper limit is infinity.
 *
 * @see Parameter
 */
public class ParameterString extends Parameter<String>
{
    static
    {
        Printable.addElement(ParameterString.class, "subject", ObservableEvent::getSubject, false);
        Printable.addElement(ParameterString.class, "name", ObservableEvent::getName, true);
        Printable.addElement(ParameterString.class, "isComputed", Parameter::isComputed, false);
        Printable.addElement(ParameterString.class, "choices", ParameterString::getChoices, false);
    }
    
    protected int maxLength;
    protected int suggestedLength = 20;
    
    protected Function<String, String> inputFunction;
    
    public ParameterString(Observable subject, String name, Supplier<String> getter, Consumer<String> setter, Collection<String> opt_choices, Collection<String> opt_values)
    {
        super(subject, name, getter, setter, opt_choices, opt_values);
    }
    
    public ParameterString(Observable subject, String name, Supplier<String> getter, Consumer<String> setter)
    {
        this(subject, name, getter, setter, null, null);
    }
    
    @Override
    public void setFromString(String value)
    {
        setValue(value);
    }
    
    /**
     * Returns the maximum length of the string. {@link ParameterString#setValue} will throw an Error if trying to set a string longer than this.
     *
     * @return the maximum length of the string
     */
    public int getMaxLength()
    {
        return this.maxLength;
    }
    
    /**
     * Sets the maximum length of the string. {@link ParameterString#setValue} will throw an RuntimeException if trying to set a string longer than this.
     *
     * @param len the maximum length of the string
     *
     * @return this Parameter for chaining setters
     *
     * @throws RuntimeException if the max length is less than length of current value of this Parameter.
     */
    public ParameterString setMaxLength(int len)
    {
        if (len < getValue().length()) throw new RuntimeException("too long");
        this.maxLength = len;
        return this;
    }
    
    /**
     * Returns the suggested length of string when making a user interface control.
     *
     * @return the suggested length of string when making a control
     */
    public int getSuggestedLength()
    {
        return this.suggestedLength;
    }
    
    /**
     * Sets the suggested length of string when making a user interface control.
     *
     * @param len suggested length of string to show
     *
     * @return this Parameter for chaining setters
     */
    public ParameterString setSuggestedLength(int len)
    {
        this.suggestedLength = len;
        return this;
    }
    
    /**
     * Set a function which transforms the input string passed to {@link ParameterString#setValue}. For example, a function to transform strings to uppercase.
     *
     * @param inputFunction function to be used to transform input passed to {@link ParameterString#setValue}
     *
     * @return this Parameter for chaining setters
     */
    public ParameterString setInputFunction(Function<String, String> inputFunction)
    {
        this.inputFunction = inputFunction;
        return this;
    }
    
    @Override
    public void setValue(String value)
    {
        if (this.inputFunction != null) value = this.inputFunction.apply(value);
        if (value.length() > this.maxLength) throw new Error("string too long: " + value + " maxLength=" + this.maxLength);
        
        if (this.values.size() > 0 && !this.values.contains(value))
        {
            throw new RuntimeException('"' + value + "\" is not an allowed value among: [" + this.values.stream().map(Object::toString).collect(Collectors.joining(", ")) + "]");
        }
        super.setValue(value);
    }
}
