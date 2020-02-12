package pe.neat;

import static pe.PixelEngine.print;

public class CrossoverTest
{
    static GenomeDrawer drawer = new GenomeDrawer();
    
    public static void main(String[] args)
    {
        NeatTest.setup(CrossoverTest::setup);
        NeatTest.test(CrossoverTest::test);
        
        drawer.nodeSpacing = 50;
        NeatTest.run("CrossoverTest", 3, drawer);
    }
    
    private static void setup(Genome[] genome, Counter[] nodeInnovation, Counter[] connInnovation)
    {
        genome[0].random.setSeed(1337);
        genome[1].random.setSeed(genome[0].random.nextLong());
        
        for (int i = 0; i < 3; i++)
        {
            genome[0].addNode(new Node(nodeInnovation[0].inc(), Node.Type.INPUT, 0));
        }
        genome[0].addNode(new Node(nodeInnovation[0].inc(), Node.Type.OUTPUT, 2));
        genome[0].addNode(new Node(nodeInnovation[0].inc(), Node.Type.HIDDEN, 1));
        
        genome[0].addConnection(new Connection(connInnovation[0].inc(), genome[0].getNode(0), genome[0].getNode(3), 1.0, true));
        genome[0].addConnection(new Connection(connInnovation[0].inc(), genome[0].getNode(1), genome[0].getNode(3), 1.0, false));
        genome[0].addConnection(new Connection(connInnovation[0].inc(), genome[0].getNode(2), genome[0].getNode(3), 1.0, true));
        genome[0].addConnection(new Connection(connInnovation[0].inc(), genome[0].getNode(1), genome[0].getNode(4), 1.0, true));
        genome[0].addConnection(new Connection(connInnovation[0].inc(), genome[0].getNode(4), genome[0].getNode(3), 1.0, true));
        genome[0].addConnection(new Connection(connInnovation[0].inc(), genome[0].getNode(0), genome[0].getNode(4), 1.0, true));
        
        for (int i = 0; i < 3; i++)
        {
            genome[1].addNode(new Node(nodeInnovation[1].inc(), Node.Type.INPUT, 0));
        }
        genome[1].addNode(new Node(nodeInnovation[1].inc(), Node.Type.OUTPUT, 3));
        genome[1].addNode(new Node(nodeInnovation[1].inc(), Node.Type.HIDDEN, 1));
        genome[1].addNode(new Node(nodeInnovation[1].inc(), Node.Type.HIDDEN, 2));
        
        genome[1].addConnection(new Connection(connInnovation[1].inc(), genome[1].getNode(0), genome[1].getNode(3), 0.5, true));
        genome[1].addConnection(new Connection(connInnovation[1].inc(), genome[1].getNode(1), genome[1].getNode(3), 0.5, false));
        genome[1].addConnection(new Connection(connInnovation[1].inc(), genome[1].getNode(2), genome[1].getNode(3), 0.5, true));
        genome[1].addConnection(new Connection(connInnovation[1].inc(), genome[1].getNode(1), genome[1].getNode(4), 0.5, true));
        genome[1].addConnection(new Connection(connInnovation[1].inc(), genome[1].getNode(4), genome[1].getNode(3), 0.5, false));
        genome[1].addConnection(new Connection(connInnovation[1].inc(), genome[1].getNode(4), genome[1].getNode(5), 0.5, true));
        genome[1].addConnection(new Connection(connInnovation[1].inc(), genome[1].getNode(5), genome[1].getNode(3), 0.5, true));
        genome[1].addConnection(new Connection(connInnovation[1].inc(), genome[1].getNode(2), genome[1].getNode(4), 0.5, true));
        genome[1].addConnection(new Connection(connInnovation[1].inc(), genome[1].getNode(0), genome[1].getNode(5), 0.5, true));
    }
    
    private static void test(Genome[] genome, Counter[] nodeInnovation, Counter[] connInnovation)
    {
        genome[2] = Genome.crossover(genome[0], genome[1], 0.10);
    }
}
