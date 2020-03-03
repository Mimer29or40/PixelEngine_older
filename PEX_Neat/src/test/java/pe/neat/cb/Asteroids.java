package pe.neat.cb;

import pe.PixelEngine;

public class Asteroids extends PixelEngine
{
    @Override
    protected void setup()
    {
        size(600, 350, 1, 1);
    }
    
    @Override
    protected void draw(double elapsedTime)
    {
    
    }
    
    public static void main(String[] args)
    {
        start(new Asteroids());
    }
}
