package pe.neat;

import pe.Random;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Supplier;

import static pe.PixelEngine.println;

public class Population
{
    public final Random random = new Random();
    
    public final Counter nodeInno = new Counter();
    public final Counter connInno = new Counter();
    
    public final int populationSize;
    
    public final ArrayList<Organism> organisms = new ArrayList<>();
    public final Supplier<Organism>  organismFactory;
    
    public Organism champion;
    
    public boolean massExtinctionEvent = false;
    
    public int generation = 0;
    
    public Population(int size, Supplier<Organism> organismFactory)
    {
        this.populationSize  = size;
        this.organismFactory = organismFactory;
        
        for (int i = 0; i < size; i++)
        {
            Organism organism = organismFactory.get();
            organism.brain.mutate(this.random, this.nodeInno, this.connInno, PEX_Neat.WEIGHT_PERTURBING_RATE,
                                  PEX_Neat.WEIGHT_MUTATION_RATE, PEX_Neat.NODE_MUTATION_RATE, PEX_Neat.CONNECTION_MUTATION_RATE);
            this.organisms.add(organism);
        }
    }
    
    public void updateGeneration(double elapsedTime)
    {
        for (Organism organism : this.organisms)
        {
            if (organism.alive)
            {
                organism.gatherInputs(elapsedTime);
                organism.processInputs(elapsedTime);
                // if (!showNothing && (!showBest || i == 0))
                // {
                //     organism.draw(elapsedTime);
                // }
                organism.calculateFitness();
            }
        }
    }
    
    /**
     * @return True if all organisms are dead
     */
    public boolean done()
    {
        for (Organism organism : this.organisms)
        {
            if (organism.alive) return false;
        }
        return true;
    }
    
    public void naturalSelection()
    {
        this.generation++;
        
        speciate();//seperate the population into species
        calculateFitness();//calculate the fitness of each player
        sortSpecies();//sort the species to be ranked in fitness order, best first
        if (massExtinctionEvent)
        {
            massExtinction();
            massExtinctionEvent = false;
        }
        cullSpecies();//kill off the bottom half of each species
        setBestPlayer();//save the best player of this gen
        killStaleSpecies();//remove species which haven't improved in the last 15(ish) generations
        killBadSpecies();//kill species which are so bad that they cant reproduce
        
        println("Generation: %s Mutation Count: %s", this.generation + 1, this.nodeInno.get() + this.connInno.get());
        
        this.organisms.sort(Comparator.comparingDouble(o -> -o.fitness));
        
        Organism champion = this.organisms.get(0);
        if (this.champion == null || champion.fitness > this.champion.fitness) this.champion = champion;
        
        ArrayList<Organism> nextGen = new ArrayList<>();
        nextGen.add(champion);
        
        Organism temp;
        while (nextGen.size() < this.populationSize)
        {
            Organism child;
            if (this.random.nextDouble() < PEX_Neat.ASEXUAL_REPRODUCTION_RATE)
            {
                Organism parent = this.random.nextIndex(this.organisms);
                child = parent.copy(organismFactory.get());
            }
            else
            {
                Organism parent1 = this.random.nextIndex(this.organisms);
                Organism parent2 = this.random.nextIndex(this.organisms);
                
                if (parent2.fitness > parent1.fitness)
                {
                    temp    = parent1;
                    parent1 = parent2;
                    parent2 = temp;
                }
                child       = organismFactory.get();
                child.brain = Genome.crossover(this.random, parent1.brain, parent2.brain, PEX_Neat.DISABLED_GENE_INHERITING_RATE);
            }
            if (this.random.nextDouble() < PEX_Neat.WEIGHT_MUTATION_RATE)
            {
                child.brain.weightMutation(this.random, PEX_Neat.WEIGHT_PERTURBING_RATE);
            }
            if (this.random.nextDouble() < PEX_Neat.NODE_MUTATION_RATE)
            {
                child.brain.nodeMutation(this.random, this.nodeInno, this.connInno);
            }
            if (this.random.nextDouble() < PEX_Neat.CONNECTION_MUTATION_RATE)
            {
                child.brain.connectionMutation(this.random, this.connInno, 100);
            }
            nextGen.add(child);
        }
        
        this.organisms.clear();
        this.organisms.addAll(nextGen);
        
        // double              averageSum = getAvgFitnessSum();
        // ArrayList<Organism> children   = new ArrayList<>();//the next generation
        // println("Species:");
        // // for (int j = 0; j < species.size(); j++)
        // // {//for each species
        // //
        // //     println("best unadjusted fitness:", species.get(j).bestFitness);
        // //     for (int i = 0; i < species.get(j).players.size(); i++)
        // //     {
        // //         print("player " + i, "fitness: " + species.get(j).players.get(i).fitness, "score " + species.get(j).players.get(i).score, ' ');
        // //     }
        // //     println();
        // //     children.add(species.get(j).champ.cloneForReplay());//add champion without any mutation
        // //
        // //     int NoOfChildren = floor(species.get(j).averageFitness / averageSum * pop.size()) -
        // //                        1;//the number of children this species is allowed, note -1 is because the champ is already added
        // //     for (int i = 0; i < NoOfChildren; i++)
        // //     {//get the calculated amount of children from this species
        // //         children.add(species.get(j).giveMeBaby(innovationHistory));
        // //     }
        // // }
        
        // while (children.size() < this.organisms.size())
        // {//if not enough babies (due to flooring the number of children to get a whole int)
        //     // children.add(species.get(0).giveMeBaby(innovationHistory));//get babies from the best species
        //     Organism organism = organismFactory.get();
        //     organism.brain.mutate(this.random, this.nodeInno, this.connInno, PEX_Neat.WEIGHT_PERTURBING_RATE,
        //                           PEX_Neat.WEIGHT_MUTATION_RATE, PEX_Neat.NODE_MUTATION_RATE, PEX_Neat.CONNECTION_MUTATION_RATE);
        //     children.add(organism);
        // }
        // this.organisms.clear();
        // this.organisms.addAll(children); //set the children as the current population
    }
    
    void setBestPlayer()
    {
        // Player tempBest = species.get(0).players.get(0);
        // tempBest.gen = gen;
        //
        //
        // //if best this gen is better than the global best score then set the global best as the best this gen
        //
        // if (tempBest.score > bestScore)
        // {
        //     genPlayers.add(tempBest.cloneForReplay());
        //     println("old best:", bestScore);
        //     println("new best:", tempBest.score);
        //     bestScore  = tempBest.score;
        //     bestPlayer = tempBest.cloneForReplay();
        // }
    }
    
    //------------------------------------------------------------------------------------------------------------------------------------------
    //seperate population into species based on how similar they are to the leaders of each species in the previous gen
    void speciate()
    {
        // for (Species s : species)
        // {//empty species
        //     s.players.clear();
        // }
        // for (int i = 0; i < pop.size(); i++)
        // {//for each player
        //     boolean speciesFound = false;
        //     for (Species s : species)
        //     {//for each species
        //         if (s.sameSpecies(pop.get(i).brain))
        //         {//if the player is similar enough to be considered in the same species
        //             s.addToSpecies(pop.get(i));//add it to the species
        //             speciesFound = true;
        //             break;
        //         }
        //     }
        //     if (!speciesFound)
        //     {//if no species was similar enough then add a new species with this as its champion
        //         species.add(new Species(pop.get(i)));
        //     }
        // }
    }
    
    //------------------------------------------------------------------------------------------------------------------------------------------
    //calculates the fitness of all of the players
    void calculateFitness()
    {
        for (int i = 1; i < this.organisms.size(); i++) this.organisms.get(i).calculateFitness();
    }
    
    //------------------------------------------------------------------------------------------------------------------------------------------
    //sorts the players within a species and the species by their fitnesses
    void sortSpecies()
    {
        // //sort the players within a species
        // for (Species s : species)
        // {
        //     s.sortSpecies();
        // }
        //
        // //sort the species by the fitness of its best player
        // //using selection sort like a loser
        // ArrayList<Species> temp = new ArrayList<Species>();
        // for (int i = 0; i < species.size(); i++)
        // {
        //     float max      = 0;
        //     int   maxIndex = 0;
        //     for (int j = 0; j < species.size(); j++)
        //     {
        //         if (species.get(j).bestFitness > max)
        //         {
        //             max      = species.get(j).bestFitness;
        //             maxIndex = j;
        //         }
        //     }
        //     temp.add(species.get(maxIndex));
        //     species.remove(maxIndex);
        //     i--;
        // }
        // species = (ArrayList) temp.clone();
    }
    
    //------------------------------------------------------------------------------------------------------------------------------------------
    //kills all species which haven't improved in 15 generations
    void killStaleSpecies()
    {
        // for (int i = 2; i < species.size(); i++)
        // {
        //     if (species.get(i).staleness >= 15)
        //     {
        //         species.remove(i);
        //         i--;
        //     }
        // }
    }
    
    //------------------------------------------------------------------------------------------------------------------------------------------
    //if a species sucks so much that it wont even be allocated 1 child for the next generation then kill it now
    void killBadSpecies()
    {
        // double averageSum = getAvgFitnessSum();
        //
        // for (int i = 1; i < species.size(); i++)
        // {
        //     if (species.get(i).averageFitness / averageSum * pop.size() < 1)
        //     {//if wont be given a single child
        //         species.remove(i);//sad
        //         i--;
        //     }
        // }
    }
    
    //------------------------------------------------------------------------------------------------------------------------------------------
    //returns the sum of each species average fitness
    double getAvgFitnessSum()
    {
        // double averageSum = 0;
        // for (Species s : species)
        // {
        //     averageSum += s.averageFitness;
        // }
        // return averageSum;
        return 0.0;
    }
    
    //------------------------------------------------------------------------------------------------------------------------------------------
    //kill the bottom half of each species
    void cullSpecies()
    {
        // for (Species s : species)
        // {
        //     s.cull(); //kill bottom half
        //     s.fitnessSharing();//also while we're at it lets do fitness sharing
        //     s.setAverage();//reset averages because they will have changed
        // }
    }
    
    
    void massExtinction()
    {
        // for (int i = 5; i < species.size(); i++)
        // {
        //     species.remove(i);//sad
        //     i--;
        // }
    }
}
