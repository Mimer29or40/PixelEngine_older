package pe.neat.cb;

import static pe.PixelEngine.random;

public class ConnectionGene
{
    public Node    fromNode;
    public Node    toNode;
    public double  weight;
    public boolean enabled = true;
    public int     innovationNo;//each connection is given a innovation number to compare genomes
    
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //constructor
    public ConnectionGene(Node from, Node to, double w, int inno)
    {
        fromNode     = from;
        toNode       = to;
        weight       = w;
        innovationNo = inno;
    }
    
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //changes the weight
    public void mutateWeight()
    {
        double rand2 = random().nextDouble();
        if (rand2 < 0.1)
        {//10% of the time completely change the weight
            weight = random().nextDouble(-1, 1);
        }
        else
        {//otherwise slightly change it
            weight += random().nextGaussian() / 50;
            //keep weight between bounds
            if (weight > 1)
            {
                weight = 1;
            }
            if (weight < -1)
            {
                weight = -1;
                
            }
        }
    }
    
    //----------------------------------------------------------------------------------------------------------
    //returns a copy of this connectionGene
    public ConnectionGene clone(Node from, Node to)
    {
        ConnectionGene clone = new ConnectionGene(from, to, weight, innovationNo);
        clone.enabled = enabled;
        
        return clone;
    }
}
