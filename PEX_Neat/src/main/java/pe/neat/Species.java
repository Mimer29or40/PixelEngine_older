package pe.neat;

import pe.Random;

import java.util.ArrayList;
import java.util.Comparator;

import static pe.PixelEngine.println;

public class Species
{
    private final ArrayList<Organism> organisms = new ArrayList<>();
    
    public Organism champion;
    public double   bestFitness;
    public double   averageFitness;
    
    private int staleness;
    
    public Species() {}
    
    public Species(Organism organism)
    {
        this.organisms.add(organism);
        this.champion = organism;
    }
    
    public boolean sameSpecies(Organism organism)
    {
        if (this.organisms.isEmpty() || this.champion == null) return false;
        
        return Genome.compatibilityDistance(this.champion.brain, organism.brain,
                                            PEX_Neat.EXCESS_GENE_WEIGHT,
                                            PEX_Neat.DISJOINT_GENE_WEIGHT,
                                            PEX_Neat.AVERAGE_WEIGHT_DIFFERENCE_WEIGHT) < PEX_Neat.SPECIES_DISTANCE;
    }
    
    public void addOrganism(Organism organism)
    {
        if (this.champion == null || organism.fitness > this.champion.fitness) this.champion = organism;
        this.organisms.add(organism);
    }
    
    public void sort()
    {
        this.organisms.sort(Comparator.comparingDouble(o -> -o.fitness));
        if (this.organisms.isEmpty())
        {
            println("fucking");
            this.staleness = 200;
            return;
        }
        
        if (this.organisms.get(0).fitness > this.bestFitness) //if new best player
        {
            this.staleness   = 0;
            this.bestFitness = this.organisms.get(0).fitness;
            this.champion    = this.organisms.get(0);
        }
        else // if no new best player
        {
            this.staleness++;
        }
    }
    
    public void setAverage()
    {
        if (this.organisms.isEmpty())
        {
            this.averageFitness = 0.0;
            return;
        }
        float sum = 0;
        for (Organism organism : this.organisms)
        {
            sum += organism.fitness;
        }
        this.averageFitness = sum / this.organisms.size();
    }
    
    /**
     * gets baby from the players in this species
     * <p>
     * // * @param innovationHistory
     * // * @return
     */
    //
    // public Organism giveMeBaby(ArrayList<connectionHistory> innovationHistory)
    public Organism giveMeBaby(Random random)
    {
        // Organism baby;
        // if (random.nextDouble() < 0.25)
        // {//25% of the time there is no crossover and the child is simply a clone of a random(ish) player
        //     baby = selectPlayer(random).copy(new Or);
        // }
        // else
        // {//75% of the time do crossover
        //
        //     //get 2 random(ish) parents
        //     Organism parent1 = selectPlayer(random);
        //     Organism parent2 = selectPlayer(random);
        //
        //     //the crossover function expects the highest fitness parent to be the object and the lowest as the argument
        //     if (parent1.fitness < parent2.fitness)
        //     {
        //         baby = parent2.crossover(parent1);
        //     }
        //     else
        //     {
        //         baby = parent1.crossover(parent2);
        //     }
        // }
        // baby.brain.mutate(innovationHistory);//mutate that baby brain
        return null;
    }
    
    /**
     * selects a player based on it fitness
     */
    public Organism selectPlayer(Random random)
    {
        double fitnessSum = 0;
        for (Organism value : this.organisms) fitnessSum += value.fitness;
        
        double rand = random.nextDouble(fitnessSum);
        
        double runningSum = 0;
        for (Organism organism : this.organisms)
        {
            runningSum += organism.fitness;
            if (runningSum > rand) return organism;
        }
        //unreachable code to make the parser happy
        return this.organisms.get(0);
    }
    
    /**
     * kills off bottom half of the species
     */
    void cull()
    {
        if (this.organisms.size() > 2)
        {
            for (int i = this.organisms.size() / 2, n = this.organisms.size(); i < n; i++)
            {
                this.organisms.remove(i);
                i--;
            }
        }
    }
    
    /**
     * in order to protect unique players, the fitnesses of each player is divided by the number of players in the species that that player belongs to
     */
    void fitnessSharing()
    {
        for (Organism organism : this.organisms)
        {
            organism.fitness /= this.organisms.size();
        }
    }
}
