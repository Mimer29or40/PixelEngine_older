package pe.neat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Genome
{
    private final Random random;
    
    public final ArrayList<Node>       nodes       = new ArrayList<>();
    public final ArrayList<Connection> connections = new ArrayList<>();
    
    public final int inputCount, outputCount;
    
    public final int biasNode;
    
    public int layerCount;
    
    private int nextNodeID       = 0;
    private int nextConnectionID = 0;
    
    public Genome(int inputCount, int outputCount, Random random)
    {
        this.random = random;
        
        this.inputCount = inputCount;
        this.outputCount = outputCount;
        
        for (int i = 0; i < this.inputCount; i++)
        {
            this.nodes.add(new Node(this.nextNodeID++, 0));
        }
        
        for (int i = 0; i < this.outputCount; i++)
        {
            this.nodes.add(new Node(this.nextNodeID++, 1));
        }
        
        this.biasNode = this.nextNodeID++;
        this.nodes.add(this.inputCount, new Node(this.biasNode, 0));
        
        List<Node> inputNodes  = getInputNodes();
        List<Node> outputNodes = getOutputNodes();
        for (Node input : inputNodes)
        {
            for (Node output : outputNodes)
            {
                this.connections.add(new Connection(input, output, this.random.nextDouble() * 2.0 - 1.0, this.nextConnectionID++));
            }
        }
    }
    
    public Genome(int inputCount, int outputCount)
    {
        this(inputCount, outputCount, new Random());
    }
    
    public void addConnection()
    {
    
    }
    
    private List<Node> getInputNodes()
    {
        return this.nodes.subList(0, this.inputCount);
    }
    
    private List<Node> getOutputNodes()
    {
        int index = 0;
        for (int i = 0, n = this.nodes.size(); i < n; i++)
        {
            if (this.nodes.get(i).layer + 1 == this.layerCount)
            {
                index = i;
                break;
            }
        }
        return this.nodes.subList(index, this.nodes.size());
    }
}
