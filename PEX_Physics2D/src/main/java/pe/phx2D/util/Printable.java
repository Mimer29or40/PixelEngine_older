package pe.phx2D.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * An object that has a minimal string representation via its {@link Printable#toShortString} method.
 * <p>
 * When writing a {@link Object#toString} method, use {@link Printable#toShortString} on objects that are Printable. This is mainly needed to avoid infinite loops, such as when an
 * object prints a {@link Observable} or {@link Observer}.
 * <p>
 * This can also make printing an *array of Printable objects* more practical because we only print minimal identity information, rather than the full {@link Object#toString}
 * representation which would have too much information and be unreadable.
 */
public interface Printable
{
    Map<Class, Map<String, Function>> shortElements = new HashMap<>();
    Map<Class, Map<String, Function>> longElements  = new HashMap<>();
    
    /**
     * Registers a property for a class that will print when {@link Object#toString} is called.
     */
    static <T> void addElement(Class<T> clazz, String name, Function<? extends T, Object> value, boolean printShort)
    {
        if (printShort)
        {
            shortElements.putIfAbsent(clazz, new LinkedHashMap<>());
            shortElements.get(clazz).put(name, value);
        }
        longElements.putIfAbsent(clazz, new LinkedHashMap<>());
        longElements.get(clazz).put(name, value);
    }
    
    /**
     * Returns a minimal string representation of this object, usually giving just identity information like the class name and name of the object.
     * <p>
     * For an object whose main purpose is to represent another Printable object, it is recommended to include the result of calling this on that other object. For example,
     * calling this on a DisplayShape might return something like this:
     * <p>
     * DisplayShape{polygon:Polygon{'chain3'}}
     *
     * @return a minimal string representation of this object.
     */
    default String toShortString()
    {
        return String.format("%s{%s}", getClass().getSimpleName(), getObjectString(this, shortElements));
    }
    
    /**
     * Returns a full string representation of this object.
     * <p>
     * DisplayShape{polygon:Polygon{'chain3'}}
     *
     * @return a full string representation of this object.
     */
    default String toLongString()
    {
        return String.format("%s{%s}", getClass().getSimpleName(), getObjectString(this, longElements));
    }
    
    /**
     * Private method which searches for registered properties to print
     *
     * @return The objects parameters formatted
     */
    static String getObjectString(final Object object, final Map<Class, Map<String, Function>> elements)
    {
        Class clazz = object.getClass();
        while (clazz != null)
        {
            if (elements.containsKey(clazz))
            {
                final Map<String, Function> objectMap = elements.get(clazz);
                return objectMap.entrySet().stream().map(e -> {
                    Object        obj = e.getValue().apply(object);
                    StringBuilder b   = new StringBuilder(e.getKey()).append(": ");
                    if (obj instanceof Printable)
                    {
                        b.append(((Printable) obj).toShortString());
                    }
                    else if (obj instanceof Number)
                    {
                        b.append(Util.NF((Number) obj));
                    }
                    else if (obj instanceof Collection)
                    {
                        b.append("[");
                        b.append(((Collection) obj).stream().map(o -> o instanceof Printable ? ((Printable) o).toShortString() : o.toString()).collect(Collectors.joining(", ")));
                        b.append("]");
                    }
                    else
                    {
                        b.append(obj);
                    }
                    return b.toString();
                }).collect(Collectors.joining(", "));
            }
            clazz = clazz.getSuperclass();
        }
        return "";
    }
}
