package pe.neat;

import static pe.PixelEngine.print;

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
    
    private static void setup(Genome genome, Counter nodeInnovation, Counter connInnovation)
    {
        genome.random.setSeed(1337);
        
        Node node0 = genome.addNode(new Node(nodeInnovation.inc(), Node.Type.INPUT, 0));
        Node node1 = genome.addNode(new Node(nodeInnovation.inc(), Node.Type.INPUT, 0));
        Node node2 = genome.addNode(new Node(nodeInnovation.inc(), Node.Type.OUTPUT, 2));
        Node node3 = genome.addNode(new Node(nodeInnovation.inc(), Node.Type.OUTPUT, 2));
        Node node4 = genome.addNode(new Node(nodeInnovation.inc(), Node.Type.BIAS, 0));
        Node node5 = genome.addNode(new Node(nodeInnovation.inc(), Node.Type.HIDDEN, 1));
        Node node6 = genome.addNode(new Node(nodeInnovation.inc(), Node.Type.HIDDEN, 1));
        Node node7 = genome.addNode(new Node(nodeInnovation.inc(), Node.Type.HIDDEN, 1));
        Node node8 = genome.addNode(new Node(nodeInnovation.inc(), Node.Type.HIDDEN, 1));
        
        genome.addConnection(new Connection(connInnovation.inc(), node0, node2, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), node0, node3, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), node0, node5, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), node0, node6, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), node0, node7, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), node0, node8, 1.0, true));
        
        genome.addConnection(new Connection(connInnovation.inc(), node1, node2, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), node1, node3, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), node1, node5, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), node1, node6, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), node1, node7, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), node1, node8, 1.0, true));
        
        genome.addConnection(new Connection(connInnovation.inc(), node4, node2, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), node4, node3, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), node4, node5, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), node4, node6, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), node4, node7, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), node4, node8, 1.0, true));
        
        genome.addConnection(new Connection(connInnovation.inc(), node5, node2, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), node5, node2, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), node5, node3, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), node5, node3, 1.0, true));
        
        genome.addConnection(new Connection(connInnovation.inc(), node6, node2, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), node6, node2, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), node6, node3, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), node6, node3, 1.0, true));
        
        genome.addConnection(new Connection(connInnovation.inc(), node7, node2, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), node7, node2, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), node7, node3, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), node7, node3, 1.0, true));
        
        genome.addConnection(new Connection(connInnovation.inc(), node8, node2, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), node8, node2, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), node8, node3, 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), node8, node3, 1.0, true));
        
        String fileName = name + "/generation_0";
        drawer.generateSprite(genome).saveSprite(fileName);
        // genome.save(fileName);
    }
    
    private static void test(Genome genome, Counter nodeInnovation, Counter connInnovation)
    {
        for (int i = 0; i < 100; i++)
        {
            print(i);
            genome.weightMutation(0.7);
            String fileName = String.format("%s/generation_%s", name, i + 1);
            drawer.generateSprite(genome).saveSprite(fileName);
            // genome.save(fileName);
        }
    }
}
