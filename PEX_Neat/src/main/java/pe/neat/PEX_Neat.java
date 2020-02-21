package pe.neat;

import pe.Logger;
import pe.PEX;
import pe.Profiler;
import pe.Sprite;
import pe.draw.DrawMode;

import java.util.function.Supplier;

import static pe.PixelEngine.*;

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
    
    private static int        populationSize;
    private static Population population;
    
    private static Supplier<Organism> organismFactory;
    
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
    
    public static boolean  showBestEachGen = false;
    public static int      upToGen         = 0;
    public static Organism genPlayerTemp;
    
    public static boolean showNothing = false;
    
    public static final GenomeDrawer drawGenome = new GenomeDrawer();
    
    public static void setOrganismFactory(Supplier<Organism> factory)
    {
        PEX_Neat.organismFactory = factory;
    }
    
    public static void setPopulationSize(int size)
    {
        if (PEX_Neat.population != null)
        {
            PEX_Neat.LOGGER.warn("Population size can only be used in setup");
            return;
        }
        PEX_Neat.populationSize = size;
    }
    
    public PEX_Neat(Profiler profiler)
    {
        super(profiler);
    }
    
    @Override
    public void beforeSetup()
    {
    
    }
    
    @Override
    public void afterSetup()
    {
        PEX_Neat.population = new Population(PEX_Neat.populationSize, PEX_Neat.organismFactory);
    
        PEX_Neat.genPlayerTemp = organismFactory.get();
    }
    
    @Override
    public void beforeDraw(double elapsedTime)
    {
        // if (Keyboard.SPACE.down()) PEX_Neat.showBest = !PEX_Neat.showBest;
        // if (Keyboard.B.down()) PEX_Neat.runBest = !PEX_Neat.runBest;
        // if (Keyboard.S.down())
        // {
        //     PEX_Neat.runThroughSpecies = !PEX_Neat.runThroughSpecies;
        //     PEX_Neat.upToSpecies       = 0;
        //     // PEX_Neat.genPlayerTemp     = PEX_Neat.population.species.get(PEX_Neat.upToSpecies).champion.clone();
        // }
        // if (Keyboard.G.down())
        // {
        //     PEX_Neat.showBestEachGen = !PEX_Neat.showBestEachGen;
        //     PEX_Neat.upToGen         = 0;
        //     // PEX_Neat.genPlayerTemp   = PEX_Neat.population.genPlayers.get(PEX_Neat.upToGen).clone();
        // }
        // if (Keyboard.N.down()) PEX_Neat.showNothing = !PEX_Neat.showNothing;
        // if (Keyboard.P.down())
        // {
        //     PEX_Neat.humanPlaying = !PEX_Neat.humanPlaying;
        //     PEX_Neat.humanPlayer  = PEX_Neat.organismFactory.get();
        // }
        // if (Keyboard.RIGHT.down())
        // {
        //     if (PEX_Neat.runThroughSpecies)
        //     {//if showing the species in the current generation then move on to the next species
        //         PEX_Neat.upToSpecies++;
        //         // if (PEX_Neat.upToSpecies >= PEX_Neat.population.species.size())
        //         // {
        //         //     PEX_Neat.runThroughSpecies = false;
        //         // }
        //         // else
        //         // {
        //         //     PEX_Neat.speciesChamp = PEX_Neat.population.species.get(PEX_Neat.upToSpecies).champ.cloneForReplay();
        //         // }
        //     }
        //     else if (PEX_Neat.showBestEachGen)
        //     {//if showing the best player each generation then move on to the next generation
        //         PEX_Neat.upToGen++;
        //         // if (upToGen >= PEX_Neat.population.genPlayers.size())
        //         // {//if reached the current generation then exit out of the showing generations mode
        //         //     PEX_Neat.showBestEachGen = false;
        //         // }
        //         // else
        //         // {
        //         //     PEX_Neat.genPlayerTemp = PEX_Neat.population.genPlayers.get(PEX_Neat.upToGen).cloneForReplay();
        //         // }
        //     }
        // }
        population.updateGeneration(elapsedTime);
        population.calculateFitness();
    
        if (population.done())
        {
            population.naturalSelection();
        }
    
        // if (PEX_Neat.showBestEachGen)
        // {//show the best of each gen
        //     if (PEX_Neat.genPlayerTemp.alive)
        //     {//if current gen player is not dead then update it
        //
        //         PEX_Neat.genPlayerTemp.gatherInputs(elapsedTime);
        //         PEX_Neat.genPlayerTemp.processInputs(elapsedTime);
        //     }
        //     else
        //     {//if dead move on to the next generation
        //         PEX_Neat.upToGen++;
        //         // if (PEX_Neat.upToGen >= PEX_Neat.population.genPlayers.size())
        //         // {//if at the end then return to the start and stop doing it
        //         //     PEX_Neat.upToGen         = 0;
        //         //     PEX_Neat.showBestEachGen = false;
        //         // }
        //         // else
        //         // {//if not at the end then get the next generation
        //         //     PEX_Neat.genPlayerTemp = PEX_Neat.population.genPlayers.get(PEX_Neat.upToGen).cloneForReplay();
        //         // }
        //     }
        // }
        // else if (PEX_Neat.runThroughSpecies) //show all the species
        // {
        //     if (PEX_Neat.speciesChamp.alive)
        //     {//if best player is not dead
        //         PEX_Neat.speciesChamp.gatherInputs(elapsedTime);
        //         PEX_Neat.speciesChamp.processInputs(elapsedTime);
        //     }
        //     else
        //     {//once dead
        //         PEX_Neat.upToSpecies++;
        //         // if (PEX_Neat.upToSpecies >= PEX_Neat.population.species.size())
        //         // {
        //         //     PEX_Neat.runThroughSpecies = false;
        //         // }
        //         // else
        //         // {
        //         //     PEX_Neat.speciesChamp = PEX_Neat.population.species.get(PEX_Neat.upToSpecies).champ.cloneForReplay();
        //         // }
        //     }
        // }
        // else if (PEX_Neat.humanPlaying)
        // {//if the user is controling the ship[
        //     if (PEX_Neat.humanPlayer.alive)
        //     {//if the player isnt dead then move and show the player based on input
        //         PEX_Neat.humanPlayer.gatherInputs(elapsedTime);
        //     }
        //     else
        //     {//once done return to ai
        //         PEX_Neat.humanPlaying = false;
        //     }
        // }
        // else if (PEX_Neat.runBest) //if just evolving normally
        // {// if replaying the best ever game
        //     // if (!PEX_Neat.population.bestPlayer.dead)
        //     // {//if best player is not dead
        //     //     PEX_Neat.population.bestPlayer.look();
        //     //     PEX_Neat.population.bestPlayer.think();
        //     //     PEX_Neat.population.bestPlayer.update();
        //     //     PEX_Neat.population.bestPlayer.show();
        //     // }
        //     // else
        //     {//once dead
        //         PEX_Neat.runBest = false;//stop replaying it
        //         // PEX_Neat.population.bestPlayer = PEX_Neat.population.bestPlayer.cloneForReplay();//reset the best player so it can play again
        //     }
        // }
        // else if (!PEX_Neat.population.done())
        // {//if any players are alive then update them
        //     PEX_Neat.population.updateAlive();
        // }
        // else
        // {//all dead
        //     // genetic algorithm
        //     PEX_Neat.population.naturalSelection();
        // }
    }
    
    @Override
    public void afterDraw(double elapsedTime)
    {
        if (!PEX_Neat.showNothing)
        {
            for (Organism organism : population.organisms)
            {
                if (organism.alive)
                {
                    organism.draw(elapsedTime);
                    drawString(1, 1, "Fitness: " + Math.round(organism.fitness * 100) / 100);
                    drawString(1, 10, "Generation: " + (population.generation + 1));
                    break;
                }
            }
        
            // if (PEX_Neat.showBestEachGen && PEX_Neat.genPlayerTemp.alive)
            // {
            //     PEX_Neat.genPlayerTemp.draw(elapsedTime);
            // }
            // else if (PEX_Neat.runThroughSpecies && PEX_Neat.speciesChamp.alive) //show all the species
            // {
            //     PEX_Neat.genPlayerTemp.draw(elapsedTime);
            // }
            // else if (PEX_Neat.humanPlaying && PEX_Neat.humanPlayer.alive)
            // {
            //     PEX_Neat.genPlayerTemp.draw(elapsedTime);
            // }
            // else if (runBest) //if just evolving normally
            // {// if replaying the best ever game
            //     // if (!PEX_Neat.population.bestPlayer.dead)
            //     // {//if best player is not dead
            //     //     PEX_Neat.population.bestPlayer.look();
            //     //     PEX_Neat.population.bestPlayer.think();
            //     //     PEX_Neat.population.bestPlayer.update();
            //     //     PEX_Neat.population.bestPlayer.show();
            //     // }
            //     // else
            //     {//once dead
            //         PEX_Neat.runBest = false;//stop replaying it
            //         // PEX_Neat.population.bestPlayer = PEX_Neat.population.bestPlayer.cloneForReplay();//reset the best player so it can play again
            //     }
            // }
        
            int x = 0;
            int y = 0;
            drawMode(DrawMode.MASK);
        
            if (PEX_Neat.runThroughSpecies)
            {
                Sprite sprite = PEX_Neat.drawGenome.generateGraph(PEX_Neat.speciesChamp.brain);
                drawSprite(x, y, sprite);
            }
            else if (PEX_Neat.runBest)
            {
                // Sprite sprite = PEX_Neat.drawGenome.generateGraph(PEX_Neat.population.bestPlayer.brain);
                // drawSprite(x, y, sprite);
            }
            else if (PEX_Neat.humanPlaying)
            {
                PEX_Neat.showBrain = false;
            }
            else if (PEX_Neat.showBestEachGen)
            {
                // Sprite sprite = PEX_Neat.drawGenome.generateGraph(PEX_Neat.genPlayerTemp.brain);
                // drawSprite(x, y, sprite);
            }
            else
            {
                // Sprite sprite = PEX_Neat.drawGenome.generateGraph(PEX_Neat.population.pop.get(0).brain);
                // drawSprite(x, y, sprite);
            }
        
            // if (PEX_Neat.showBestEachGen)
            // {
            //     drawString(1, 1, "Fitness: " + PEX_Neat.genPlayerTemp.fitness);
            //     // drawString(1, 10, "Generation: " + PEX_Neat.population.fitness);
            // }
            // else if (PEX_Neat.runThroughSpecies)
            // {
            //     drawString(1, 1, "Fitness: " + PEX_Neat.speciesChamp.fitness);//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<replace
            //     drawString(1, 10, "Species: " + (PEX_Neat.upToSpecies + 1));
            //     // drawString(50, screenHeight() / 2 + 200, "Players in this Species: " + PEX_Neat.population.species.get(upToSpecies).players.size());
            // }
            // else if (PEX_Neat.humanPlaying)
            // {
            //     drawString(1, 1, "Fitness: " + PEX_Neat.humanPlayer.fitness);
            // }
            // else if (PEX_Neat.runBest)
            // {
            //     // drawString(650, 50, "Score: " + PEX_Neat.population.bestPlayer.score);//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<replace
            //     // drawString(1150, 50, "Gen: " + PEX_Neat.population.gen);
            // }
            // else if (PEX_Neat.showBest)
            // {
            //     // drawString(650, 50, "Score: " + PEX_Neat.population.pop.get(0).score);//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<replace
            //     // drawString(1150, 50, "Gen: " + PEX_Neat.population.gen);
            //     // drawString(50, screenHeight() / 2 + 300, "Species: " + PEX_Neat.population.species.size());
            //     // drawString(50, screenHeight() / 2 + 200, "Global Best Score: " + PEX_Neat.population.bestScore);
            // }
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
