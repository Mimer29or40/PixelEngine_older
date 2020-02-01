package pe.phx2D.util;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ParameterDouble extends ParameterNumber<Double>
{
    public ParameterDouble(Observable subject, String name, Supplier<Double> getter, Consumer<Double> setter, Collection<String> opt_choices, Collection<Double> opt_values)
    {
        super(subject, name, getter, setter, opt_choices, opt_values);
    }
    
    public ParameterDouble(Observable subject, String name, Supplier<Double> getter, Consumer<Double> setter)
    {
        super(subject, name, getter, setter);
    }
    
    @Override
    public void setFromString(String value)
    {
        setValue(Double.parseDouble(value));
    }
    
    @Override
    protected boolean validateLowerLimit(Double newLimit)
    {
        return newLimit < getValue() || newLimit < this.upperLimit;
    }
    
    @Override
    protected boolean validateUpperLimit(Double newLimit)
    {
        return getValue() < newLimit || this.lowerLimit < newLimit;
    }
    
    @Override
    protected boolean validate(Double value)
    {
        return this.lowerLimit == null || this.upperLimit == null || this.lowerLimit <= value && value <= this.upperLimit;
    }
}
