package pe.neat;

import java.util.ArrayList;

public class Node
{
    public final int id;
    
    public int layer;
    
    public final ArrayList<Double> inputs = new ArrayList<>();
    public       double            output;
    
    public Node(int id, int layer)
    {
        this.id = id;
        this.layer = layer;
    }
}
