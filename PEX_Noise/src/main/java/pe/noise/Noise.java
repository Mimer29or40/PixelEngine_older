package pe.noise;

import pe.Sprite;

import java.util.Random;

public class Noise
{
    protected final Random random = new Random();
    
    public long seed;
    
    public Noise(long seed)
    {
        this.seed = seed;
    }
    
    public double get(int x)
    {
        return 0.0;
    }
    
    public static class DiamondSquare extends Noise
    {
        public DiamondSquare(long seed)
        {
            super(seed);
        }
    }
    
    public static void main(String[] args)
    {
        int width  = 200;
        int height = 200;
        
        Sprite sprite = new Sprite(width, height);
        for (int j = 0; j < height; j++)
        {
            for (int i = 0; i < width; i++)
            {
            
            }
        }
    }
}
