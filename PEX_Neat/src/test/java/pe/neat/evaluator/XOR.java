package pe.neat.evaluator;

import pe.Random;
import pe.neat.*;

import java.util.function.Consumer;
import java.util.function.Function;

import static pe.PixelEngine.print;
import static pe.PixelEngine.println;

public class XOR
{
    public static void main(String[] args)
    {
        EvaluatorTest.setup(XOR::setup);
    
        GenomeDrawer drawer = new GenomeDrawer();
        drawer.layerSpacing = 30;
    
        double[][] inputs = new double[][] {
                new double[] {0.0, 0.0, 1.0},
                new double[] {0.0, 1.0, 1.0},
                new double[] {1.0, 0.0, 1.0},
                new double[] {1.0, 1.0, 1.0},
                };
        double[] correct_results = new double[] {0.0, 1.0, 1.0, 0.0};
    
        Settings settings = new Settings(1000);
        
        Function<Genome, Double> evaluator = (genome) -> {
            Network net = new Network(genome);
    
            double total = 0;
            for (int i = 0; i < 4; i++)
            {
                double[] outputs  = net.calculate(inputs[i]);
                double   distance = Math.abs(correct_results[i] - outputs[0]);
                total += distance * distance;
            }
    
            // if (genome.connections.size() > 20) total += genome.connections.size() - 20;
            //
            // return 100.0 - total * 5.0;
            return 1.0 / total;
        };
        
        Consumer<Genome> printFunc = (genome) -> {
            Network net = new Network(genome);
            for (int i = 0; i < 4; i++)
            {
                double[] outputs = net.calculate(inputs[i]);
                print("%s, ", outputs[0]);
            }
            println();
        };
    
        EvaluatorTest.run("XOR", settings, evaluator, printFunc, 1000, drawer);
    }
    
    private static void setup(Random random, Genome genome, Counter nodeInnovation, Counter connInnovation)
    {
        random.setSeed(1337);
        
        genome.addNode(new Node(nodeInnovation.inc(), Node.Type.INPUT, 0));
        genome.addNode(new Node(nodeInnovation.inc(), Node.Type.INPUT, 0));
        genome.addNode(new Node(nodeInnovation.inc(), Node.Type.INPUT, 0));
        genome.addNode(new Node(nodeInnovation.inc(), Node.Type.OUTPUT, 1));
        
        genome.addConnection(new Connection(connInnovation.inc(), 0, 3, 0.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), 1, 3, 0.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), 2, 3, 0.0, true));
    }
}
