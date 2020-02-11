package pe.neat;

import java.util.Objects;

public class Connection
{
    public final int id;
    public final Node in;
    public final Node out;
    
    public double  weight;
    public boolean enabled;
    
    public Connection(int id, Node in, Node out, double weight, boolean enabled)
    {
        this.id = id;
        this.in = in;
        this.out = out;
        
        this.weight = weight;
        this.enabled = enabled;
    }
    
    @Override
    public String toString()
    {
        return "Connection{id=" + this.id + ", " + this.in.toString() + "->" + this.out.toString() + ", weight=" + this.weight + ", enabled=" + this.enabled + "}";
    }
    
    @Override
    public boolean equals(Object o)
    {
        return this == o || (o instanceof Connection && this.id == ((Connection) o).id);
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(this.id);
    }
    
    protected Connection copy()
    {
        return new Connection(this.id, this.in, this.out, this.weight, this.enabled);
    }
}
