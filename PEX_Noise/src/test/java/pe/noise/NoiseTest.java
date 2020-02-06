package pe.noise;

import pe.PixelEngine;

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
        start(new NoiseTest(), 200, 200, 1, 1);
    }
}
