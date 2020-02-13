package pe.neat;

import pe.Random;

import static pe.PixelEngine.print;

public class NodeMutationTest
{
    static GenomeDrawer drawer = new GenomeDrawer();
    static String name = "out/mutation/node";
    
    public static void main(String[] args)
    {
        NeatTest.setup(NodeMutationTest::setup);
        NeatTest.test(NodeMutationTest::test);
        
        NeatTest.run("NodeMutationTest", drawer);
    }
    
    private static void setup(Random random, Genome genome, Counter nodeInnovation, Counter connInnovation)
    {
        random.setSeed(1337);
    
        genome.addNode(new Node(nodeInnovation.inc(), Node.Type.INPUT, 0));
        genome.addNode(new Node(nodeInnovation.inc(), Node.Type.INPUT, 0));
        genome.addNode(new Node(nodeInnovation.inc(), Node.Type.OUTPUT, 1));
    
        genome.addConnection(new Connection(connInnovation.inc(), 0, 2, 0.5, true));
        genome.addConnection(new Connection(connInnovation.inc(), 1, 2, 0.75, true));
    
        String fileName = name + "/generation_0";
        drawer.generateGraph(genome).saveSprite(fileName);
        // genome.save(fileName);
    }
    
    private static void test(Random random, Genome genome, Counter nodeInnovation, Counter connInnovation)
    {
        for (int i = 0; i < 100; i++)
        {
            print(i);
            genome.nodeMutation(nodeInnovation, connInnovation);
            for (int j = 0; j < 2; j++)
            {
                genome.connectionMutation(connInnovation, 100);
            }
            String fileName = String.format("%s/generation_%s", name, i + 1);
            drawer.generateGraph(genome).saveSprite(fileName);
            // genome.save(fileName);
        }
    }
}
