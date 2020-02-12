package pe.neat;

import pe.PixelEngine;
import pe.Sprite;

import java.util.Random;

public class DrawGenomeTest extends PixelEngine
{
    @Override
    protected boolean onUserCreate()
    {
        Counter nodeInnovation = new Counter();
        Counter connInnovation = new Counter();
        Genome  genome         = new Genome(new Random(100));
        
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
        
        GenomeDrawer drawer = new GenomeDrawer();
        
        Sprite genomeSprite = drawer.generateSprite(genome);
        genomeSprite.saveSprite("out/test.png");
        
        return false;
    }
    
    public static void main(String[] args)
    {
        start(new DrawGenomeTest(), 400, 400, 1, 1);
    }
}
