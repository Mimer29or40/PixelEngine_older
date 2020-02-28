package pe.neat.cb;

import pe.PixelEngine;

public class Asteroids extends PixelEngine
{
    @Override
    protected boolean setup()
    {
        return true;
    }
    
    @Override
    protected boolean draw(double elapsedTime)
    {
        return true;
    }
    
    public static void main(String[] args)
    {
        start(new Asteroids(), 600, 350, 1, 1);
    }
}
