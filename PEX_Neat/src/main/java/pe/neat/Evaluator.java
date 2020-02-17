package pe.neat;

import pe.Random;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Supplier;

public class Evaluator
{
    public final Random   random;
    public final Settings settings;
    public final Counter  nodeInnovation;
    public final Counter  connInnovation;
    
    public final Function<Genome, Double> fitnessCalculator;
    
    public final ArrayList<Genome> genomes = new ArrayList<>();
    public       Genome            fittest = null;
    
    private final ArrayList<Genome> lastGen = new ArrayList<>();
    private final ArrayList<Genome> nextGen = new ArrayList<>();
    
    public Evaluator(Random random, Settings settings, Supplier<Genome> generator, Function<Genome, Double> fitnessCalculator, Counter nodeInnovation,
                     Counter connInnovation)
    {
        this.random         = random;
        this.settings       = settings;
        this.nodeInnovation = nodeInnovation;
        this.connInnovation = connInnovation;
        
        this.fitnessCalculator = fitnessCalculator;
        
        for (int i = 0; i < this.settings.populationSize; i++)
        {
            this.genomes.add(generator.get());
        }
    }
    
    public void evaluateGeneration()
    {
        this.lastGen.clear();
        this.lastGen.addAll(this.genomes);
        
        for (Genome genome : this.genomes)
        {
            genome.fitness = this.fitnessCalculator.apply(genome);
        }
    
        this.genomes.sort(Comparator.comparingDouble(o -> -o.fitness));
        
        Genome champion = this.genomes.get(0);
        if (this.fittest == null || champion.fitness > this.fittest.fitness) this.fittest = champion;
    
        if (this.genomes.size() > 10) this.genomes.retainAll(this.genomes.subList(0, Math.max(this.genomes.size() / 10, 1)));
        
        this.nextGen.clear();
        this.nextGen.add(champion);
        
        while (this.nextGen.size() < this.settings.populationSize)
        {
            Genome child;
            if (this.random.nextDouble() < this.settings.asexualReproductionRate)
            {
                Genome parent = this.random.nextIndex(this.genomes);
                child = parent.copy();
            }
            else
            {
                Genome parent1 = this.random.nextIndex(this.genomes);
                Genome parent2 = this.random.nextIndex(this.genomes);
                // TODO - Determine fittest parent;
                child = Genome.crossover(this.random, parent1, parent2, this.settings.disabledGeneInheritingRate);
            }
            if (this.random.nextDouble() < this.settings.weightMutationRate)
            {
                child.weightMutation(this.random, this.settings.weightPerturbingRate);
            }
            if (this.random.nextDouble() < this.settings.nodeMutationRate)
            {
                child.nodeMutation(this.random, this.nodeInnovation, this.connInnovation);
            }
            if (this.random.nextDouble() < this.settings.connectionMutationRate)
            {
                child.connectionMutation(this.random, this.connInnovation, 100);
            }
            // child.minimizeLayers();
            this.nextGen.add(child);
        }
        
        this.genomes.clear();
        this.genomes.addAll(this.nextGen);
    }
}
