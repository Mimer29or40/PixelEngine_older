package pe.neat;

import pe.Events;
import pe.Logger;
import pe.PEX;
import pe.Profiler;
import pe.event.EventKeyboardKeyDown;

import static pe.PixelEngine.drawString;

public class PEX_Neat extends PEX
{
    private static final Logger LOGGER = Logger.getLogger();
    
    /**
     * Rate at which mutation happens without crossover
     */
    public static final double ASEXUAL_REPRODUCTION_RATE = 0.25;
    
    /**
     * Rate at which a genome will have its weights mutated
     */
    public static final double WEIGHT_MUTATION_RATE = 0.80;
    
    /**
     * Rate at which a node mutation will occur
     */
    public static final double NODE_MUTATION_RATE = 0.05;
    
    /**
     * Rate at which a connection mutation will occur
     */
    public static final double CONNECTION_MUTATION_RATE = 0.05;
    
    /**
     * Rate at which a genome's weights will be nudged and not completely random
     */
    public static final double WEIGHT_PERTURBING_RATE = 0.90;
    
    /**
     * Rate at which a child genome's connection will be disabled
     */
    public static final double DISABLED_GENE_INHERITING_RATE = 0.75;
    
    /**
     * Genomic distance before two genomes are different species
     */
    public static final double SPECIES_DISTANCE = 3.0;
    
    /**
     * Weight for excess genes
     */
    public static final double EXCESS_GENE_WEIGHT = 1.0;
    
    /**
     * Weight for disjoint genes
     */
    public static final double DISJOINT_GENE_WEIGHT = 1.0;
    
    /**
     * Weight for average weight distance
     */
    public static final double AVERAGE_WEIGHT_DIFFERENCE_WEIGHT = 0.4;
    
    private static int populationSize;
    // private static Population population;
    
    /**
     * Flag to show the best of the previous generation
     */
    public static boolean showBest = true;
    
    /**
     * Flag if the all time best organism is being replayed
     */
    public static boolean runBest = false;
    
    /**
     * Flag if the user is playing
     */
    public static boolean humanPlaying = false;
    
    public static Organism humanPlayer;
    
    public static boolean  runThroughSpecies = false;
    public static int      upToSpecies       = 0;
    public static Organism speciesChamp;
    
    public static boolean showBrain = false;
    
    public static boolean showBestEachGen = false;
    public static int     upToGen         = 0;
    // public static Player  genPlayerTemp;
    
    public static boolean showNothing = false;
    
    public static void setPopulationSize(int size)
    {
        // if (PEX_Neat.population != null)
        // {
        //     PEX_Neat.LOGGER.warning("Population size can only be used in setup");
        //     return;
        // }
        PEX_Neat.populationSize = size;
    }
    
    protected PEX_Neat(Profiler profiler)
    {
        super(profiler);
    }
    
    @Override
    public void beforeSetup()
    {
        Events.subscribe(EventKeyboardKeyDown.class, e -> {
            EventKeyboardKeyDown event = (EventKeyboardKeyDown) e;
            // switch (event.key())
            // event.key()
        });
    }
    
    @Override
    public void afterSetup()
    {
        // PEX_Neat.population = new Population(PEX_Neat.populationSize);
    }
    
    @Override
    public void beforeDraw(double elapsedTime)
    {
    
    }
    
    @Override
    public void afterDraw(double elapsedTime)
    {
        if (PEX_Neat.showBestEachGen)
        {
            drawString(1, 1, "Fitness: " + PEX_Neat.speciesChamp.fitness);
            // drawString(1, 10, "Generation: " + PEX_Neat.population.fitness);
        }
        else if (PEX_Neat.runThroughSpecies)
        {
            drawString(1, 1, "Fitness: " + PEX_Neat.speciesChamp.fitness);//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<replace
            drawString(1, 10, "Species: " + (PEX_Neat.upToSpecies + 1));
            // drawString(50, screenHeight() / 2 + 200, "Players in this Species: " + PEX_Neat.population.species.get(upToSpecies).players.size());
        }
        else if (PEX_Neat.humanPlaying)
        {
            drawString(1, 1, "Fitness: " + PEX_Neat.humanPlayer.fitness);
        }
        else if (PEX_Neat.runBest)
        {
            // drawString(650, 50, "Score: " + PEX_Neat.population.bestPlayer.score);//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<replace
            // drawString(1150, 50, "Gen: " + PEX_Neat.population.gen);
        }
        else if (PEX_Neat.showBest)
        {
            // drawString(650, 50, "Score: " + PEX_Neat.population.pop.get(0).score);//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<replace
            // drawString(1150, 50, "Gen: " + PEX_Neat.population.gen);
            // drawString(50, screenHeight() / 2 + 300, "Species: " + PEX_Neat.population.species.size());
            // drawString(50, screenHeight() / 2 + 200, "Global Best Score: " + PEX_Neat.population.bestScore);
        }
    }
    
    @Override
    public void beforeDestroy()
    {
    
    }
    
    @Override
    public void afterDestroy()
    {
    
    }
}
