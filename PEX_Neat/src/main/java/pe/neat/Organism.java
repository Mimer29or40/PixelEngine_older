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
    
    protected abstract void gatherInputs(double elapsedTime, boolean userPlaying);
    
    protected void processInputs(double elapsedTime, boolean userPlaying)
    {
        this.lifeTime += elapsedTime;
        
        this.brain.calculate(this.inputs, this.outputs);
        
        makeDecision(elapsedTime, userPlaying);
    }
    
    protected abstract void makeDecision(double elapsedTime, boolean userPlaying);
    
    protected abstract void calculateFitness();
    
    public void update(double elapsedTime, boolean userPlaying)
    {
        gatherInputs(elapsedTime, userPlaying);
        processInputs(elapsedTime, userPlaying);
        calculateFitness();
    }
    
    public abstract void draw(double elapsedTime);
    
    public Organism copy(Organism other)
    {
        other.brain = this.brain.copy();
        
        other.fitness = this.fitness;
        
        other.generation = this.generation;
        
        return other;
    }
}
