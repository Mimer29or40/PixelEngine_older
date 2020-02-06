package pe.neat;

import pe.Color;
import pe.PixelEngine;
import pe.Sprite;

import java.util.Random;

public class DrawGenome extends PixelEngine
{
    public static Sprite drawGenome(Genome genome)
    {
        int nodeRadius   = 10;
        int nodeSpacing  = 10 + nodeRadius;
        int layerSpacing = 10 + nodeRadius;
        int border       = 10 + nodeRadius;
        
        int   width          = (genome.layerCount - 1) * layerSpacing + border * 2;
        int[] layerNodeCount = new int[genome.layerCount];
        int   height         = 0;
        for (Node node : genome.nodes) layerNodeCount[node.layer]++;
        for (int nodeCount : layerNodeCount) height = Math.max(height, nodeCount);
        height = (height - 1) * nodeSpacing + border * 2;
        
        Sprite sprite = new Sprite(width, height, Color.BLANK);
        
        return sprite;
    }
    
    @Override
    protected boolean onUserCreate()
    {
        Genome genome = new Genome(2, 3, new Random(100));
        
        Sprite genomeSprite = drawGenome(genome);
        
        return false;
    }
    
    public static void main(String[] args)
    {
        start(new DrawGenome(), 400, 400, 1, 1);
    }
}
