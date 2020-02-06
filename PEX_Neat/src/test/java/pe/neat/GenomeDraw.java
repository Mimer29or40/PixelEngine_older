package pe.neat;

import pe.PixelEngine;

public class GenomeDraw extends PixelEngine
{
    // public static Sprite drawGenome(Genome genome)
    // {
    //
    // }
    
    @Override
    protected boolean onUserCreate()
    {
        Genome genome = new Genome(2, 3);
        
        return false;
    }
    
    @Override
    protected boolean onUserUpdate(double elapsedTime)
    {
        return false;
    }
    
    public static void main(String[] args)
    {
        start(new GenomeDraw(), 400, 400, 1, 1);
    }
}
