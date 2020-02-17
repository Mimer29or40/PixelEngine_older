package pe.neat;

import java.util.function.Supplier;

public class Organism
{
    public final Genome brain;
    
    public double lifeTime = 0.0;
    public double fitness  = 0.0;
    
    public Organism(Supplier<Genome> initialGenome)
    {
        this.brain = initialGenome.get();
    }
    
    public Organism()
    {
        this.brain = new Genome();
    }
}
