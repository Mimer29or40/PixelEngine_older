package pe.util;

import java.util.Objects;

@SuppressWarnings("unused")
public class Pair<A, B>
{
    public final A a;
    public final B b;
    
    public Pair(A a, B b)
    {
        this.a = a;
        this.b = b;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Pair)) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(this.a, pair.a) && Objects.equals(this.b, pair.b);
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(this.a, this.b);
    }
    
    @Override
    public String toString()
    {
        return "Pair{" + this.a + ", " + this.b + '}';
    }
}
