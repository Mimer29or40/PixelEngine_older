package pe.neat;

public class DrawGenomeTest
{
    public static void main(String[] args)
    {
        NeatTest.setup(DrawGenomeTest::setup);
        
        NeatTest.run("DrawGenomeTest");
    }
    
    private static void setup(Genome genome, Counter nodeInnovation, Counter connInnovation)
    {
        genome.random.setSeed(100);
        
        for (int i = 0; i < 3; i++)
        {
            genome.addNode(new Node(nodeInnovation.inc(), Node.Type.INPUT, 0));
        }
        genome.addNode(new Node(nodeInnovation.inc(), Node.Type.OUTPUT, 2));
        genome.addNode(new Node(nodeInnovation.inc(), Node.Type.HIDDEN, 1));
        
        genome.addConnection(new Connection(connInnovation.inc(), genome.getNode(0), genome.getNode(3), 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), genome.getNode(1), genome.getNode(3), 1.0, false));
        genome.addConnection(new Connection(connInnovation.inc(), genome.getNode(2), genome.getNode(3), 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), genome.getNode(1), genome.getNode(4), 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), genome.getNode(4), genome.getNode(3), 1.0, true));
        genome.addConnection(new Connection(connInnovation.inc(), genome.getNode(0), genome.getNode(4), 1.0, true));
        // genome.addConnection(new Connection(connInnovation.inc(), genome.getNode(2), genome.getNode(4), 1.0, true));
    }
}
