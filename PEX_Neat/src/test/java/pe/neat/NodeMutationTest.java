package pe.neat;

import static pe.PixelEngine.print;

public class NodeMutationTest
{
    static GenomeDrawer drawer = new GenomeDrawer();
    static String name = "out/mutation/node";
    
    public static void main(String[] args)
    {
        NeatTest.setup(NodeMutationTest::setup);
        NeatTest.test(NodeMutationTest::test);
        
        NeatTest.run("NodeMutationTest");
    }
    
    private static void setup(Genome genome, Counter nodeInnovation, Counter connInnovation)
    {
        genome.random.setSeed(1337);

        Node input1 = genome.addNode(new Node(nodeInnovation.inc(), Node.Type.INPUT, 0));
        Node input2 = genome.addNode(new Node(nodeInnovation.inc(), Node.Type.INPUT, 0));
        Node output = genome.addNode(new Node(nodeInnovation.inc(), Node.Type.OUTPUT, 1));
        
        genome.addConnection(new Connection(connInnovation.inc(), input1, output, 0.5, true));
        genome.addConnection(new Connection(connInnovation.inc(), input2, output, 0.75, true));
        
        String fileName = name + "/generation_0";
        drawer.generateSprite(genome).saveSprite(fileName);
        // genome.save(fileName);
    }
    
    private static void test(Genome genome, Counter nodeInnovation, Counter connInnovation)
    {
        for (int i = 0; i < 20; i++)
        {
            print(i);
            genome.nodeMutation(nodeInnovation, connInnovation);
            for (int j = 0; j < 2; j++)
            {
                genome.connectionMutation(connInnovation, 100);
            }
            String fileName = String.format("%s/generation_%s", name, i + 1);
            drawer.generateSprite(genome).saveSprite(fileName);
            // genome.save(fileName);
        }
    }
}
