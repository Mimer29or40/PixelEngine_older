package pe.neat.evaluator;

import pe.Random;
import pe.neat.*;

import java.util.function.Consumer;
import java.util.function.Function;

import static pe.PixelEngine.println;

public class ManyConnections
{
    public static void main(String[] args)
    {
        EvaluatorTest.setup(ManyConnections::setup);
        
        GenomeDrawer drawer = new GenomeDrawer();
        drawer.layerSpacing = 30;
        
        double[][] inputs = new double[][] {
                new double[] {0.0, 0.0, 1.0},
                new double[] {0.0, 1.0, 1.0},
                new double[] {1.0, 0.0, 1.0},
                new double[] {1.0, 1.0, 1.0},
                };
        double[] correct_results = new double[] {0.0, 1.0, 1.0, 0.0};
        
        Settings settings = new Settings(100);
        
        Function<Genome, Double> evaluator = (genome) -> (double) genome.connections.size();
        
        Consumer<Genome> printFunc = (genome) -> println(genome.connections.size());
        
        EvaluatorTest.run("ManyConnections", settings, evaluator, printFunc, 200, drawer);
    }
    
    private static void setup(Random random, Genome genome, Counter nodeInnovation, Counter connInnovation)
    {
        random.setSeed(1337);
        
        genome.addNode(new Node(nodeInnovation.inc(), Node.Type.INPUT, 0));
        genome.addNode(new Node(nodeInnovation.inc(), Node.Type.INPUT, 0));
        genome.addNode(new Node(nodeInnovation.inc(), Node.Type.OUTPUT, 1));
        
        genome.addConnection(new Connection(connInnovation.inc(), 0, 2, 0.5, true));
        genome.addConnection(new Connection(connInnovation.inc(), 1, 2, 0.5, true));
    }
}
