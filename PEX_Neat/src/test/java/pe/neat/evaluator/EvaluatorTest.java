package pe.neat.evaluator;

import pe.PixelEngine;
import pe.Random;
import pe.neat.*;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class EvaluatorTest extends PixelEngine
{
    private static String                   name;
    private static Settings                 settings;
    private static Function<Genome, Double> fitnessCalculator;
    private static Consumer<Genome>         printFunc;
    private static GenomeDrawer             drawer;
    private static int                      generations;
    
    private static EvaluatorTest.ISetupFunc setupFunc;
    
    @Override
    protected void setup()
    {
        Random random = new Random();
    
        final Genome  genome         = new Genome();
        final Counter nodeInnovation = new Counter();
        final Counter connInnovation = new Counter();
    
        if (setupFunc != null) setupFunc.setup(random, genome, nodeInnovation, connInnovation);
    
        Supplier<Genome> generator = () -> {
            Genome g = genome.copy();
            for (Connection connection : g.getConnections())
            {
                connection.weight += random.nextGaussian() * 0.01;
                connection.weight = Math.max(-1.0, Math.min(connection.weight, 1.0));
            }
            return g;
        };
    
        Evaluator eva = new Evaluator(random, settings, generator, fitnessCalculator, nodeInnovation, connInnovation);
        
        String name = String.format("out/%s", EvaluatorTest.name);
        
        ArrayList<Double> fitnessOverTime = new ArrayList<>();
        for (int i = 0; i < generations; i++)
        {
            eva.evaluateGeneration();
            
            fitnessOverTime.add(eva.fittest.fitness);
            
            println("Generation %s", i);
            println("\tHighest Fitness: %s", eva.fittest.fitness);
            println("\tPopulation Size: %s", eva.genomes.size());
            println("\tResults from best network:");
            
            print("\t\t");
            printFunc.accept(eva.fittest);
            
            if (i % (generations / 2) == 0) drawer.generateGraph(eva.fittest).saveSprite(String.format("%s_%s.png", name, i));
        }
        
        FitnessPlotter plotter = new FitnessPlotter();
        plotter.generatePlot(fitnessOverTime).saveSprite(String.format("%s_fitness.png", name));
        
        drawer.generateGraph(eva.fittest).saveSprite(String.format("%s_%s.png", name, generations));
        eva.fittest.save(String.format("%s_fittest", name));
    }
    
    public static void setup(ISetupFunc setupFunc)
    {
        EvaluatorTest.setupFunc = setupFunc;
    }
    
    public static void run(String name, Settings settings, Function<Genome, Double> fitnessCalculator, Consumer<Genome> printFunc, int generations, GenomeDrawer drawer)
    {
        EvaluatorTest.name              = name;
        EvaluatorTest.settings          = settings;
        EvaluatorTest.fitnessCalculator = fitnessCalculator;
        EvaluatorTest.printFunc         = printFunc;
        EvaluatorTest.generations       = generations;
        EvaluatorTest.drawer            = drawer;
        
        start(new EvaluatorTest());
    }
    
    public static void run(String name, Settings settings, Function<Genome, Double> evaluator, Consumer<Genome> printFunc, GenomeDrawer drawer)
    {
        run(name, settings, evaluator, printFunc, 1000, drawer);
    }
    
    public static void run(String name, Settings settings, Function<Genome, Double> evaluator, Consumer<Genome> printFunc, int generations)
    {
        run(name, settings, evaluator, printFunc, generations, new GenomeDrawer());
    }
    
    public static void run(String name, Settings settings, Function<Genome, Double> evaluator, Consumer<Genome> printFunc)
    {
        run(name, settings, evaluator, printFunc, 1000, new GenomeDrawer());
    }
    
    public interface ISetupFunc
    {
        void setup(Random random, Genome genome, Counter nodeInnovation, Counter connInnovation);
    }
}
