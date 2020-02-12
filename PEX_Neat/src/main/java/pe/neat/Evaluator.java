package pe.neat;

import java.util.ArrayList;
import java.util.Comparator;

public class Evaluator
{
    public final Settings settings;
    public final Counter  nodeInnovation;
    public final Counter  connInnovation;
    
    // public final IEvaluator evaluator;
    
    private final ArrayList<Genome> genomes = new ArrayList<>();
    private       Genome            fittest = null;
    
    private final ArrayList<Genome> lastGen = new ArrayList<>();
    private final ArrayList<Genome> nextGen = new ArrayList<>();
    
    public Evaluator(Settings settings, Counter nodeInnovation, Counter connInnovation)
    {
        this.settings       = settings;
        this.nodeInnovation = nodeInnovation;
        this.connInnovation = connInnovation;
        
        
    }
    
    public void evaluateGeneration()
    {
        this.lastGen.clear();
        this.lastGen.addAll(this.genomes);
        
        for (Genome genome : this.genomes)
        {
            // genome.fitness = this.evaluator.evaluate(genome);
        }
        
        this.genomes.sort(Comparator.comparingDouble(o -> o.fitness));
        
        Genome champion = this.genomes.get(0);
        if (this.fittest == null || champion.fitness > this.fittest.fitness) this.fittest = champion;
        
        this.genomes.retainAll(this.genomes.subList(0, this.genomes.size() / 10));
        
        this.nextGen.clear();
        this.nextGen.add(champion);
        
        while (this.nextGen.size() < this.settings.populationSize)
        {
        
        }
        
        this.genomes.clear();
        this.genomes.addAll(this.nextGen);
    }
}
