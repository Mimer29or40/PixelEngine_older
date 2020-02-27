package pe.neat.functionality;

import pe.Random;
import pe.neat.*;

import static pe.PixelEngine.println;

public class WeightMutationTest
{
    static GenomeDrawer drawer = new GenomeDrawer();
    static String       name   = "out/mutation/weight";
    
    public static void main(String[] args)
    {
        NeatTest.setup(WeightMutationTest::setup);
        NeatTest.test(WeightMutationTest::test);
        
        NeatTest.run("WeightMutationTest", drawer);
    }
    
    private static void setup(Random random, Genome genome, Counter nodeInnovation, Counter connInnovation)
    {
        random.setSeed(1337);
    
        genome.addNode(new Node(nodeInnovation.inc(), Node.Type.INPUT, 0));
        genome.addNode(new Node(nodeInnovation.inc(), Node.Type.INPUT, 0));
        genome.addNode(new Node(nodeInnovation.inc(), Node.Type.OUTPUT, 2));
        genome.addNode(new Node(nodeInnovation.inc(), Node.Type.OUTPUT, 2));
        genome.addNode(new Node(nodeInnovation.inc(), Node.Type.BIAS, 0));
        genome.addNode(new Node(nodeInnovation.inc(), Node.Type.HIDDEN, 1));
        genome.addNode(new Node(nodeInnovation.inc(), Node.Type.HIDDEN, 1));
        genome.addNode(new Node(nodeInnovation.inc(), Node.Type.HIDDEN, 1));
        genome.addNode(new Node(nodeInnovation.inc(), Node.Type.HIDDEN, 1));
    
        genome.addConnection(new Connection(connInnovation.inc(), 0, 2, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), 0, 3, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), 0, 5, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), 0, 6, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), 0, 7, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), 0, 8, 1.0, true));
    
        genome.addConnection(new Connection(connInnovation.inc(), 1, 2, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), 1, 3, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), 1, 5, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), 1, 6, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), 1, 7, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), 1, 8, 1.0, true));
    
        genome.addConnection(new Connection(connInnovation.inc(), 4, 2, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), 4, 3, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), 4, 5, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), 4, 6, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), 4, 7, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), 4, 8, 1.0, true));
    
        genome.addConnection(new Connection(connInnovation.inc(), 5, 2, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), 5, 2, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), 5, 3, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), 5, 3, 1.0, true));
    
        genome.addConnection(new Connection(connInnovation.inc(), 6, 2, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), 6, 2, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), 6, 3, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), 6, 3, 1.0, true));
    
        genome.addConnection(new Connection(connInnovation.inc(), 7, 2, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), 7, 2, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), 7, 3, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), 7, 3, 1.0, true));
    
        genome.addConnection(new Connection(connInnovation.inc(), 8, 2, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), 8, 2, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), 8, 3, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), 8, 3, 1.0, true));
    
        String fileName = name + "/generation_0";
        drawer.generateGraph(genome).saveSprite(fileName);
        // genome.save(fileName);
    }
    
    private static void test(Random random, Genome genome, Counter nodeInnovation, Counter connInnovation)
    {
        for (int i = 0; i < 100; i++)
        {
            println(i);
            genome.weightMutation(random, 0.7);
            String fileName = String.format("%s/generation_%s", name, i + 1);
            drawer.generateGraph(genome).saveSprite(fileName);
            // genome.save(fileName);
        }
    }
}
