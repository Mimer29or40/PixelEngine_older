package pe.neat;

import java.util.ArrayList;
import java.util.Objects;

public class Node
{
    public static final IActivator SIGMOID = value -> 1.0 / (1.0 + Math.exp(-4.9 * value));
    public static final IActivator STEP    = value -> value < 0.0 ? -1.0 : 1.0;
    
    public final int  id;
    public final Type type;
    
    public int layer;
    
    public IActivator activator = SIGMOID;
    
    private final ArrayList<Double> inputValues = new ArrayList<>();
    private       double            outputValue = 0.0;
    private       boolean           calculate   = true;
    
    public final ArrayList<Connection> outputConnections = new ArrayList<>();
    
    public Node(int id, Type type, int layer)
    {
        this.id    = id;
        this.type  = type;
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
    
    public void reset()
    {
        this.inputValues.clear();
        this.outputValue = 0.0;
        // this.outputConnections.clear();
        
        this.calculate = true;
    }
    
    public double getOutputValue()
    {
        return this.outputValue;
    }
    
    public void feedInput(double inputValue)
    {
        this.inputValues.add(inputValue);
        
        this.calculate = true;
    }
    
    public void calculate()
    {
        if (this.calculate)
        {
            double inputSum = 0.0;
            for (double inputValue : this.inputValues) inputSum += inputValue;
            this.outputValue = this.activator.activate(inputSum);
            
            this.calculate = false;
        }
    }
    
    public void engage(Genome genome)
    {
        calculate();
        
        for (Connection connection : this.outputConnections)
        {
            if (connection.enabled)
            {
                genome.getNode(connection.out).feedInput(this.outputValue * connection.weight);
            }
        }
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
