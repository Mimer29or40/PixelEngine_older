package pe.neat;

public class ConnMutationTest
{
    public static void main(String[] args)
    {
        NeatTest.setup(ConnMutationTest::setup);
        NeatTest.test(ConnMutationTest::test);
        
        NeatTest.run("ConnMutationTest");
    }
    
    private static void setup(Genome genome, Counter nodeInnovation, Counter connInnovation)
    {
        genome.random.setSeed(1337);

        genome.addNode(new Node(nodeInnovation.inc(), Node.Type.INPUT, 0));
        genome.addNode(new Node(nodeInnovation.inc(), Node.Type.INPUT, 0));
        genome.addNode(new Node(nodeInnovation.inc(), Node.Type.OUTPUT, 1));
    }
    
    private static void test(Genome genome, Counter nodeInnovation, Counter connInnovation)
    {
        genome.connectionMutation(connInnovation, 10);
    }
}
