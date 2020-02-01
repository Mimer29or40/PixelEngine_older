package pe.gui.property;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public abstract class Property<T>
{
    private T prev;
    private T temp;
    private T value;
    
    public Property()
    {
        this.prev = init(null);
        this.temp = init(null);
        this.value = init(null);
    }
    
    public Property(T initial)
    {
        this.prev = init(initial);
        this.temp = init(initial);
        this.value = init(initial);
    }
    
    protected abstract T init(T initial);
    
    public T get()
    {
        return value;
    }
    
    public void set(T newValue)
    {
        if (!Objects.equals(this.value, newValue))
        {
            this.prev = assignPrev(this.prev, this.value);
            this.temp = assignTemp(this.temp, newValue);
            if (validate(this.prev, this.temp))
            {
                this.value = assignValue(this.value, newValue);
                changed(this.prev, this.value);
            }
        }
    }
    
    protected abstract T assignPrev(T prev, T value);
    
    protected abstract T assignTemp(T temp, T newValue);
    
    protected abstract T assignValue(T value, T newValue);
    
    protected boolean validate(T prev, T value)
    {
        for (Validate<T> validator : this.validators) if (!validator.validate(prev, value)) return false;
        return true;
    }
    
    protected void changed(T prev, T value)
    {
        for (Changed<T> listener : this.listeners) listener.changed(prev, value);
    }
    
    private final Set<Validate<T>> validators = new HashSet<>();
    
    public boolean addValidator(Validate<T> validator)
    {
        return this.validators.add(validator);
    }
    
    public boolean removeValidator(Validate<T> validator)
    {
        return this.validators.remove(validator);
    }
    
    private final Set<Changed<T>> listeners = new HashSet<>();
    
    public boolean addListener(Changed<T> listener)
    {
        return this.listeners.add(listener);
    }
    
    public boolean removeListener(Changed<T> listener)
    {
        return this.listeners.remove(listener);
    }
    
    private final Set<Property<T>> bindings = new HashSet<>();
    
    public interface Validate<T>
    {
        boolean validate(T prev, T value);
    }
    
    public interface Changed<T>
    {
        void changed(T prev, T value);
    }
}
