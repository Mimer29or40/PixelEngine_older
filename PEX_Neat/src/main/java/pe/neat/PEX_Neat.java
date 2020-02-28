package pe.neat;

import pe.Keyboard;
import pe.PEX;
import pe.Profiler;
import pe.Random;
import pe.color.Color;
import pe.draw.DrawMode;

import java.util.function.Supplier;

import static pe.PixelEngine.*;

public class PEX_Neat extends PEX
{
    // private static final Logger LOGGER = Logger.getLogger();
    
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
    
    public static final Random random = new Random();
    
    private static Supplier<Organism> defaultOrganism;
    
    private static Population population;
    private static int        populationSize;
    
    private static Organism userOrganism;
    
    private static GenomeDrawer genomeDrawer;
    
    private static boolean runFlag;
    private static boolean playFlag;
    
    private static boolean showHelp       = false; // F1
    private static boolean userPlaying    = false; // F2
    private static boolean cycleOrganisms = false; // F3
    private static boolean showChamp      = false; // F4
    private static boolean showBest       = true;  // F5
    private static boolean drawBrain      = false; // F12
    
    private static int currentOrganism = 0;
    
    private static int timeScale = 1;
    
    public static void setDefaultOrganism(Supplier<Organism> defaultOrganism)
    {
        PEX_Neat.defaultOrganism = defaultOrganism;
    }
    
    public static void setPopulationSize(int populationSize)
    {
        PEX_Neat.populationSize = populationSize;
    }
    
    public PEX_Neat(Profiler profiler)
    {
        super(profiler);
    }
    
    @Override
    public void beforeSetup() { }
    
    @Override
    public void afterSetup()
    {
        PEX_Neat.playFlag = PEX_Neat.defaultOrganism != null;
        PEX_Neat.runFlag  = PEX_Neat.playFlag && PEX_Neat.populationSize > 0;
        
        if (PEX_Neat.playFlag) PEX_Neat.userOrganism = defaultOrganism.get();
        
        if (PEX_Neat.runFlag)
        {
            PEX_Neat.population = new Population(PEX_Neat.random, PEX_Neat.populationSize, PEX_Neat.defaultOrganism);
            
            PEX_Neat.genomeDrawer = new GenomeDrawer();
            PEX_Neat.genomeDrawer.backgroundColor.set(Color.BLANK);
            PEX_Neat.genomeDrawer.imageScale   = 1;
            PEX_Neat.genomeDrawer.nodeSpacing  = 1;
            PEX_Neat.genomeDrawer.layerSpacing = 1;
        }
    }
    
    @Override
    public void beforeDraw(double elapsedTime)
    {
        if (PEX_Neat.runFlag)
        {
            if (Keyboard.F1.down()) PEX_Neat.showHelp = !PEX_Neat.showHelp;
            if (Keyboard.F2.down())
            {
                PEX_Neat.userPlaying    = true;
                PEX_Neat.cycleOrganisms = false;
                PEX_Neat.showChamp      = false;
                PEX_Neat.showBest       = false;
            }
            if (Keyboard.F3.down())
            {
                PEX_Neat.userPlaying    = false;
                PEX_Neat.cycleOrganisms = true;
                PEX_Neat.showChamp      = false;
                PEX_Neat.showBest       = false;
            }
            if (Keyboard.F4.down())
            {
                PEX_Neat.userPlaying    = false;
                PEX_Neat.cycleOrganisms = false;
                PEX_Neat.showChamp      = true;
                PEX_Neat.showBest       = false;
            }
            if (Keyboard.F5.down())
            {
                PEX_Neat.userPlaying    = false;
                PEX_Neat.cycleOrganisms = false;
                PEX_Neat.showChamp      = false;
                PEX_Neat.showBest       = true;
            }
            if (Keyboard.F12.down()) PEX_Neat.drawBrain = !PEX_Neat.drawBrain;
    
            if (PEX_Neat.cycleOrganisms)
            {
                if (Keyboard.LEFT.down()) PEX_Neat.currentOrganism--;
                if (Keyboard.RIGHT.down()) PEX_Neat.currentOrganism++;
        
                if (PEX_Neat.currentOrganism < 0) PEX_Neat.currentOrganism = PEX_Neat.populationSize - 1;
                if (PEX_Neat.currentOrganism > PEX_Neat.populationSize - 1) PEX_Neat.currentOrganism = 0;
            }
    
            if (Keyboard.EQUALS.down()) PEX_Neat.timeScale++;
            if (Keyboard.MINUS.down()) PEX_Neat.timeScale = Math.max(PEX_Neat.timeScale - 1, 1);
    
            if (PEX_Neat.userPlaying)
            {
                PEX_Neat.userOrganism.update(elapsedTime, true);
        
                if (!PEX_Neat.userOrganism.alive && Keyboard.ENTER.down())
                {
                    PEX_Neat.userOrganism = PEX_Neat.defaultOrganism.get();
                }
            }
            else
            {
                elapsedTime *= PEX_Neat.timeScale;
        
                if (PEX_Neat.population.update(elapsedTime))
                {
                    PEX_Neat.population.naturalSelection();
                }
            }
        }
        else if (PEX_Neat.playFlag)
        {
            PEX_Neat.userOrganism.update(elapsedTime, true);
    
            if (!PEX_Neat.userOrganism.alive && Keyboard.ENTER.down())
            {
                PEX_Neat.userOrganism = PEX_Neat.defaultOrganism.get();
            }
        }
    }
    
    @Override
    public void afterDraw(double elapsedTime)
    {
        if (PEX_Neat.runFlag)
        {
            String text;
            int    x, y;
    
            if (PEX_Neat.userPlaying)
            {
                PEX_Neat.userOrganism.draw(elapsedTime);
                drawString(1, screenHeight() - 8 - 1, "User Playing");
        
                if (!PEX_Neat.userOrganism.alive) drawScreenText("DEAD");
            }
            else
            {
                elapsedTime *= PEX_Neat.timeScale;
        
                Organism current = null;
        
                if (PEX_Neat.cycleOrganisms) current = PEX_Neat.population.organisms.get(PEX_Neat.currentOrganism);
                if (PEX_Neat.showChamp) current = PEX_Neat.population.champion;
                if (PEX_Neat.showBest)
                {
                    double maxFitness = 0;
                    current = PEX_Neat.population.champion;
                    for (Organism organism : PEX_Neat.population.organisms)
                    {
                        if (organism.alive && organism.fitness >= maxFitness)
                        {
                            maxFitness = organism.fitness;
                            current    = organism;
                        }
                    }
                }
        
                double fitness = 0.0;
        
                if (current != null)
                {
                    current.draw(elapsedTime);
                    fitness = current.fitness;
            
                    if (!current.alive) drawScreenText("DEAD");
            
                    if (PEX_Neat.drawBrain)
                    {
                        drawMode(DrawMode.BLEND);
                        drawSprite(0, 0, PEX_Neat.genomeDrawer.generateGraph(current.brain));
                        drawMode(DrawMode.NORMAL);
                    }
                }
        
                if (PEX_Neat.cycleOrganisms)
                {
                    text = "Organism: " + (PEX_Neat.currentOrganism + 1);
                    x    = 1;
                    y    = screenHeight() - textHeight(text) - 1;
                    drawString(x, y, text);
                }
                if (PEX_Neat.showChamp)
                {
                    text = "Champion Organism";
                    x    = 1;
                    y    = screenHeight() - textHeight(text) - 1;
                    drawString(x, y, text);
                }
                if (PEX_Neat.showBest)
                {
                    text = "Best Organism";
                    x    = 1;
                    y    = screenHeight() - textHeight(text) - 1;
                    drawString(x, y, text);
                }
        
                text = "Generation: " + (PEX_Neat.population.generation + 1);
                x    = screenWidth() - textWidth(text) - 1;
                y    = 1;
                drawString(x, y, text);
        
                y += textHeight(text) + 1;
                text = "Fitness: " + round(fitness, 3);
                x    = screenWidth() - textWidth(text) - 1;
                drawString(x, y, text);
            }
    
            if (PEX_Neat.showHelp)
            {
                x    = 1;
                y    = 1;
                text = " F1: Show this Menu";
                drawString(x, y, text);
    
                y += textHeight(text) + 1;
                text = " F2: Play Game";
                drawString(x, y, text);
    
                y += textHeight(text) + 1;
                text = " F3: Cycle Organisms";
                drawString(x, y, text);
    
                y += textHeight(text) + 1;
                text = " F4: Show Champion";
                drawString(x, y, text);
    
                y += textHeight(text) + 1;
                text = " F5: Show Best";
                drawString(x, y, text);
    
                y += textHeight(text) + 1;
                text = " F6: ";
                drawString(x, y, text);
    
                y += textHeight(text) + 1;
                text = " F7: ";
                drawString(x, y, text);
    
                y += textHeight(text) + 1;
                text = " F8: ";
                drawString(x, y, text);
    
                y += textHeight(text) + 1;
                text = " F9: ";
                drawString(x, y, text);
    
                y += textHeight(text) + 1;
                text = "F10: ";
                drawString(x, y, text);
    
                y += textHeight(text) + 1;
                text = "F11: ";
                drawString(x, y, text);
    
                y += textHeight(text) + 1;
                text = "F12: Show Brain";
                drawString(x, y, text);
    
                y += textHeight(text) + 1;
                text = "LEFT/RIGHT: Cycle between organisms";
                drawString(x, y, text);
    
                y += textHeight(text) + 1;
                text = "MINUS/EQUALS: Change time scale";
                drawString(x, y, text);
            }
        }
        else if (PEX_Neat.playFlag)
        {
            PEX_Neat.userOrganism.draw(elapsedTime);
    
            if (!PEX_Neat.userOrganism.alive) drawScreenText("DEAD");
        }
    }
    
    @Override
    public void beforeDestroy() { }
    
    @Override
    public void afterDestroy() { }
    
    private static void drawScreenText(String text)
    {
        int width  = textWidth(text);
        int height = textHeight(text);
        
        fillRect((screenWidth() - width) / 2 - 2, (screenHeight() - height) / 2 - 2, width + 4, height + 4, Color.GREY);
        drawString((screenWidth() - width) / 2, (screenHeight() - height) / 2, text);
    }
}
