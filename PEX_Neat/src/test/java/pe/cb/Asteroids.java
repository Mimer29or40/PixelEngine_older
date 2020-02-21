package pe.cb;

import pe.PixelEngine;

public class Asteroids extends PixelEngine
{
    @Override
    protected boolean setup()
    {
        return super.setup();
    }
    
    @Override
    protected boolean draw(double elapsedTime)
    {
        return super.draw(elapsedTime);
    }
    
    public static void main(String[] args)
    {
        start(new Asteroids(), 1200, 675, 1, 1);
    }
}
