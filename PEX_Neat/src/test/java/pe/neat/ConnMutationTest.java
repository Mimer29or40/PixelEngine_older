package pe.neat;

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
    
    private static void setup(Genome genome, Counter nodeInnovation, Counter connInnovation)
    {
        genome.random.setSeed(1337);
        
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
        drawer.generateSprite(genome).saveSprite(fileName);
        // genome.save(fileName);
    }
    
    private static void test(Genome genome, Counter nodeInnovation, Counter connInnovation)
    {
        for (int i = 0; i < 100; i++)
        {
            print(i);
            genome.connectionMutation(connInnovation, 10);
            String fileName = String.format("%s/generation_%s", name, i + 1);
            drawer.generateSprite(genome).saveSprite(fileName);
            // genome.save(fileName);
        }
    }
}
