package pe.neat;

import java.util.function.Supplier;

public abstract class Organism
{
    public Genome brain;
    
    public final double[] inputs;
    public final double[] outputs;
    
    public double lifeTime = 0.0;
    public double fitness  = 0.0;
    
    public int score      = 0;
    public int generation = 0;
    
    public boolean alive = true;
    
    public Organism(Genome brain)
    {
        this.brain = brain;
        
        this.inputs  = new double[this.brain.inputSize()];
        this.outputs = new double[this.brain.outputSize()];
    }
    
    public Organism(Supplier<Genome> initialGenome)
    {
        this(initialGenome.get());
    }
    
    public abstract void gatherInputs(double elapsedTime);
    
    public void processInputs(double elapsedTime)
    {
        this.lifeTime += elapsedTime;
        
        this.brain.calculate(this.inputs, this.outputs);
        
        int    decision = 0;
        double max      = 0;
        for (int i = 0, n = outputs.length; i < n; i++)
        {
            if (outputs[i] > max)
            {
                max      = outputs[i];
                decision = i;
            }
        }
        makeDecision(elapsedTime, decision);
    }
    
    public abstract void makeDecision(double elapsedTime, int decision);
    
    public abstract void draw(double elapsedTime);
    
    public abstract void calculateFitness();
    
    public Organism copy(Organism other)
    {
        other.brain = this.brain.copy();
        
        other.fitness = this.fitness;
        
        other.generation = this.generation;
        
        return other;
    }
}
