package pe.phx2D.util;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Provides access to a numeric value of a {@link Observable}. Has options for setting number of significant digits to show, and upper/lower limit on value. Default is 3
 * significant digits, lower limit of zero, and upper limit is infinity.
 *
 * @see Parameter
 */
public class ParameterBoolean extends Parameter<Boolean>
{
    static
    {
        Printable.addElement(ParameterBoolean.class, "subject", ObservableEvent::getSubject, false);
        Printable.addElement(ParameterBoolean.class, "name", ObservableEvent::getName, true);
        Printable.addElement(ParameterBoolean.class, "isComputed", Parameter::isComputed, false);
        Printable.addElement(ParameterBoolean.class, "choices", ParameterBoolean::getChoices, false);
    }
    
    public ParameterBoolean(Observable subject, String name, Supplier<Boolean> getter, Consumer<Boolean> setter, Collection<String> opt_choices, Collection<Boolean> opt_values)
    {
        super(subject, name, getter, setter, opt_choices, opt_values);
    }
    
    public ParameterBoolean(Observable subject, String name, Supplier<Boolean> getter, Consumer<Boolean> setter)
    {
        this(subject, name, getter, setter, null, null);
    }
    
    @Override
    public void setFromString(String value)
    {
        String v = value.toLowerCase();
        if (v.equals("true") || v.equals("false"))
        {
            setValue(v.equals("true"));
            return;
        }
        throw new RuntimeException("non-boolean value: " + value);
    }
}
