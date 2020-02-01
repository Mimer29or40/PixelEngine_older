package pe.phx2D.util;

public abstract class Nameable implements Printable
{
    static
    {
        Printable.addElement(Nameable.class, "name", Nameable::getName, true);
    }
    
    private final String name;
    
    /**
     * @param name Name of the object, either the language-independent name for scripting purposes or the localized name for display to user.
     */
    public Nameable(String name)
    {
        this.name = Util.validName(Util.toName(name));
    }
    
    public String getName()
    {
        return this.name;
    }
    
    /**
     * Whether this Nameable has the given name, adjusting for the transformation to a language independent form of the name, as is done by {@link Util#toName}.
     *
     * @param name the English or language-independent version of the name
     *
     * @return whether this ObservableEvent has the given name (adjusted to language-independent form)
     */
    public boolean nameEquals(String name)
    {
        return getName().equals(Util.toName(name));
    }
}
