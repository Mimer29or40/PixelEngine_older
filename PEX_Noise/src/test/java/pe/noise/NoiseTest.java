package pe.noise;

import pe.Color;
import pe.PixelEngine;
import pe.Sprite;

public class NoiseTest extends PixelEngine
{
    @Override
    protected boolean onUserCreate()
    {
        return true;
    }
    
    @Override
    protected boolean onUserUpdate(double elapsedTime)
    {
        return true;
    }
    
    public static void main(String[] args)
    {
        int width  = 200;
        int height = 200;
    
        Noise.Perlin noise = new Noise.Perlin(0);
    
        Sprite sprite = new Sprite(width, height);
        for (int j = 0; j < height; j++)
        {
            double y = (double) j / height * 2.0;
        
            for (int i = 0; i < width; i++)
            {
                double x = (double) i / width * 2.0;
            
                sprite.setPixel(i, j, new Color(noise.generate(x, y)));
            }
        }
        sprite.saveSprite("NOISE");
        
        // start(new NoiseTest(), 200, 200, 1, 1);
    }
}
