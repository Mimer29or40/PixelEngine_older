package pe.phx2D.util;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Provides access to a numeric value of a {@link Observable}. Has options for setting number of significant digits to show, and upper/lower limit on value. Default is 3
 * significant digits, lower limit of zero, and upper limit is infinity.
 *
 * @see Parameter
 */
public abstract class ParameterNumber<T extends Number> extends Parameter<T>
{
    static
    {
        Printable.addElement(ParameterNumber.class, "subject", ObservableEvent::getSubject, false);
        Printable.addElement(ParameterNumber.class, "name", ObservableEvent::getName, true);
        Printable.addElement(ParameterNumber.class, "isComputed", Parameter::isComputed, false);
        Printable.addElement(ParameterNumber.class, "lowerLimit", ParameterNumber::getLowerLimit, false);
        Printable.addElement(ParameterNumber.class, "upperLimit", ParameterNumber::getUpperLimit, false);
        Printable.addElement(ParameterNumber.class, "decimalPlaces", ParameterNumber::getDecimalPlaces, false);
        Printable.addElement(ParameterNumber.class, "sigDigits", ParameterNumber::getSigDigits, false);
        Printable.addElement(ParameterNumber.class, "choices", ParameterNumber::getChoices, false);
        Printable.addElement(ParameterNumber.class, "values", ParameterNumber::getValues, false);
    }
    
    protected int sigDigits;
    protected int decimalPlaces;
    
    protected T lowerLimit;
    protected T upperLimit;
    
    public ParameterNumber(Observable subject, String name, Supplier<T> getter, Consumer<T> setter, Collection<String> opt_choices, Collection<T> opt_values)
    {
        super(subject, name, getter, setter, opt_choices, opt_values);
    }
    
    public ParameterNumber(Observable subject, String name, Supplier<T> getter, Consumer<T> setter)
    {
        this(subject, name, getter, setter, null, null);
    }
    
    /**
     * Returns the suggested number of significant digits to show, see {@link ParameterNumber#setSigDigits}.
     *
     * @return suggested number of significant digits to show
     */
    public int getSigDigits()
    {
        return this.sigDigits;
    }
    
    /**
     * Sets suggested number of significant digits to show. This affects the number of decimal places that are displayed.
     * <p>
     * Examples: if significant digits is 3, then we would show numbers as: 12345, 1234, 123, 12.3, 1.23, 0.123, 0.0123, 0.00123.
     *
     * @param sigDigits suggested number of significant digits to show
     *
     * @return this Parameter for chaining setters
     */
    public ParameterNumber<T> setSigDigits(int sigDigits)
    {
        this.sigDigits = sigDigits;
        return this;
    }
    
    /**
     * Returns the suggested number of decimal places to show or –1 if variable.
     *
     * @return suggested number of decimal places to show or –1 if variable
     */
    public int getDecimalPlaces()
    {
        return this.decimalPlaces;
    }
    
    /**
     * Sets suggested number of decimal places to show.
     *
     * @param decimals suggested number of decimal places to show, or –1 if variable
     *
     * @return this Parameter for chaining setters
     */
    public ParameterNumber<T> setDecimalPlaces(int decimals)
    {
        this.decimalPlaces = decimals;
        return this;
    }
    
    /**
     * Returns the lower limit; the Parameter value is not allowed to be less than this,
     * {@link #setValue} will throw an Error in that case.
     *
     * @return the lower limit of the Parameter value
     */
    public T getLowerLimit()
    {
        return this.lowerLimit;
    }
    
    /**
     * Sets the lower limit; the Parameter value is not allowed to be less than this, {@link ParameterNumber#setValue} will throw an Error in that case.
     *
     * @param lowerLimit the lower limit of the Parameter value
     *
     * @return this Parameter for chaining setters
     *
     * @throws RuntimeException if the value is currently less than the lower limit, or the lower limit is not a number
     */
    public ParameterNumber<T> setLowerLimit(T lowerLimit)
    {
        if (!validateLowerLimit(lowerLimit)) throw new RuntimeException("out of range: " + lowerLimit + " value=" + getValue() + " upper=" + this.upperLimit);
        this.lowerLimit = lowerLimit;
        return this;
    }
    
    /**
     * Returns the upper limit; the Parameter value is not allowed to be greater than
     * this, {@link #setValue} will throw an Error in that case.
     *
     * @return the upper limit of the Parameter value
     */
    public T getUpperLimit()
    {
        return this.upperLimit;
    }
    
    /**
     * Sets the upper limit; the Parameter value is not allowed to be more than this,
     * {@link #setValue} will throw an Error in that case.
     *
     * @param upperLimit the upper limit of the Parameter value
     *
     * @return this Parameter for chaining setters
     *
     * @throws RuntimeException if the value is currently greater than the upper limit, or the upper
     *                          limit is not a number
     */
    public ParameterNumber setUpperLimit(T upperLimit)
    {
        if (!validateUpperLimit(upperLimit))
        {
            throw new RuntimeException("out of range: " + upperLimit + " value=" + getValue() + " lower=" + this.lowerLimit);
        }
        this.upperLimit = upperLimit;
        return this;
    }
    
    @Override
    public void setValue(T value)
    {
        if (!validate(value)) throw new Error("out of range. " + value + " is not between " + this.lowerLimit + " and " + this.upperLimit);
        if (this.values.size() > 0 && !this.values.contains(value))
        {
            throw new Error(value + " is not an allowed value among: [" + this.values.stream().map(Object::toString).collect(Collectors.joining(", ")) + "]");
        }
        super.setValue(value);
    }
    
    protected abstract boolean validateLowerLimit(T newLimit);

    protected abstract boolean validateUpperLimit(T newLimit);

    protected abstract boolean validate(T value);
}
