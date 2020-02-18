package pe.neat;

import java.util.function.Supplier;

public class Organism
{
    public final Genome brain;
    
    public final double[] inputs;
    public final double[] outputs;
    
    public double lifeTime = 0.0;
    public double fitness  = 0.0;
    
    public Organism(Supplier<Genome> initialGenome)
    {
        this.brain = initialGenome.get();
        
        this.inputs  = new double[this.brain.inputSize()];
        this.outputs = new double[this.brain.outputSize()];
    }
    
    public Organism()
    {
        this.brain = new Genome();
        
        this.inputs  = new double[this.brain.inputSize()];
        this.outputs = new double[this.brain.outputSize()];
    }
    
    public void gatherInputs()
    {
    
    }
    
    public void processInputs()
    {
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
        makeDecision(decision);
    }
    
    public void makeDecision(int decision)
    {
    
    }
}
