package pe.phx2D.util;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ParameterFloat extends ParameterNumber<Float>
{
    public ParameterFloat(Observable subject, String name, Supplier<Float> getter, Consumer<Float> setter, Collection<String> opt_choices, Collection<Float> opt_values)
    {
        super(subject, name, getter, setter, opt_choices, opt_values);
    }
    
    public ParameterFloat(Observable subject, String name, Supplier<Float> getter, Consumer<Float> setter)
    {
        super(subject, name, getter, setter);
    }
    
    @Override
    public void setFromString(String value)
    {
        setValue(Float.parseFloat(value));
    }
    
    @Override
    protected boolean validateLowerLimit(Float newLimit)
    {
        return newLimit < getValue() || newLimit < this.upperLimit;
    }
    
    @Override
    protected boolean validateUpperLimit(Float newLimit)
    {
        return getValue() < newLimit || this.lowerLimit < newLimit;
    }
    
    @Override
    protected boolean validate(Float value)
    {
        return this.lowerLimit == null || this.upperLimit == null || this.lowerLimit <= value && value <= this.upperLimit;
    }
}
