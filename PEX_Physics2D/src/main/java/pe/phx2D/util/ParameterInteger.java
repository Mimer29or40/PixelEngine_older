package pe.phx2D.util;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ParameterInteger extends ParameterNumber<Integer>
{
    public ParameterInteger(Observable subject, String name, Supplier<Integer> getter, Consumer<Integer> setter, Collection<String> opt_choices, Collection<Integer> opt_values)
    {
        super(subject, name, getter, setter, opt_choices, opt_values);
    }
    
    public ParameterInteger(Observable subject, String name, Supplier<Integer> getter, Consumer<Integer> setter)
    {
        super(subject, name, getter, setter);
    }
    
    @Override
    public void setFromString(String value)
    {
        setValue(Integer.parseInt(value));
    }
    
    @Override
    protected boolean validateLowerLimit(Integer newLimit)
    {
        return newLimit < getValue() || newLimit < this.upperLimit;
    }
    
    @Override
    protected boolean validateUpperLimit(Integer newLimit)
    {
        return getValue() < newLimit || this.lowerLimit < newLimit;
    }
    
    @Override
    protected boolean validate(Integer value)
    {
        return this.lowerLimit == null || this.upperLimit == null || this.lowerLimit <= value && value <= this.upperLimit;
    }
}
