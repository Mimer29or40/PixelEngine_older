package pe.neat;

public class WeightMutationTest
{
    public static void main(String[] args)
    {
        NeatTest.setup(WeightMutationTest::setup);
        NeatTest.test(WeightMutationTest::test);
        
        NeatTest.run("WeightMutationTest");
    }
    
    private static void setup(Genome genome, Counter nodeInnovation, Counter connInnovation)
    {
        genome.random.setSeed(1337);

        Node node0 = genome.addNode(new Node(nodeInnovation.inc(), Node.Type.INPUT, 0));
        Node node1 = genome.addNode(new Node(nodeInnovation.inc(), Node.Type.INPUT, 0));
        Node node2 = genome.addNode(new Node(nodeInnovation.inc(), Node.Type.OUTPUT, 1));
        
        genome.addConnection(new Connection(connInnovation.inc(), node0, node2, 0.5, true));
        genome.addConnection(new Connection(connInnovation.inc(), node1, node2, 1.0, true));
    }
    
    private static void test(Genome genome, Counter nodeInnovation, Counter connInnovation)
    {
        for (int i = 0; i < 100000; i++)
        {
            genome.weightMutation(0.7);
        }
    }
}
