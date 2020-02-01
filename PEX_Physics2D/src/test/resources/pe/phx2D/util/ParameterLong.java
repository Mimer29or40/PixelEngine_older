package pe.phx2D.util;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ParameterLong extends ParameterNumber<Long>
{
    public ParameterLong(Observable subject, String name, Supplier<Long> getter, Consumer<Long> setter, Collection<String> opt_choices, Collection<Long> opt_values)
    {
        super(subject, name, getter, setter, opt_choices, opt_values);
    }
    
    public ParameterLong(Observable subject, String name, Supplier<Long> getter, Consumer<Long> setter)
    {
        super(subject, name, getter, setter);
    }
    
    @Override
    public void setFromString(String value)
    {
        setValue(Long.parseLong(value));
    }
    
    @Override
    protected boolean validateLowerLimit(Long newLimit)
    {
        return newLimit < getValue() || newLimit < this.upperLimit;
    }
    
    @Override
    protected boolean validateUpperLimit(Long newLimit)
    {
        return getValue() < newLimit || this.lowerLimit < newLimit;
    }
    
    @Override
    protected boolean validate(Long value)
    {
        return this.lowerLimit == null || this.upperLimit == null || this.lowerLimit <= value && value <= this.upperLimit;
    }
}
