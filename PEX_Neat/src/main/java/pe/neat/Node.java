package pe.neat;

import java.util.Objects;

public class Node
{
    public final int  id;
    public final Type type;
    
    public int layer;
    
    public Node(int id, Type type, int layer)
    {
        this.id = id;
        this.type = type;
        this.layer = layer;
    }
    
    @Override
    public String toString()
    {
        return "Node{id=" + this.id + ", " + this.type + ", layer=" + this.layer + "}";
    }
    
    @Override
    public boolean equals(Object o)
    {
        return this == o || (o instanceof Node && this.id == ((Node) o).id);
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(this.id);
    }
    
    protected Node copy()
    {
        return new Node(this.id, this.type, this.layer);
    }
    
    public enum Type
    {
        INPUT, BIAS, HIDDEN, OUTPUT
    }
}
