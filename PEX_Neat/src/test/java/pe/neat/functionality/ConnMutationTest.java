package pe.neat.functionality;

import pe.Random;
import pe.neat.Counter;
import pe.neat.Genome;
import pe.neat.GenomeDrawer;
import pe.neat.Node;

import static pe.PixelEngine.print;

public class ConnMutationTest
{
    static GenomeDrawer drawer = new GenomeDrawer();
    static String       name   = "out/mutation/connection";
    
    public static void main(String[] args)
    {
        NeatTest.setup(ConnMutationTest::setup);
        NeatTest.test(ConnMutationTest::test);
        
        NeatTest.run("ConnMutationTest", drawer);
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
        
        String fileName = name + "/generation_0";
        drawer.generateGraph(genome).saveImage(fileName);
        // genome.save(fileName);
    }
    
    private static void test(Random random, Genome genome, Counter nodeInnovation, Counter connInnovation)
    {
        for (int i = 0; i < 100; i++)
        {
            print(i);
            genome.connectionMutation(random, connInnovation, 10);
            String fileName = String.format("%s/generation_%s", name, i + 1);
            drawer.generateGraph(genome).saveImage(fileName);
            // genome.save(fileName);
        }
    }
}
