package pe.neat;

import pe.PixelEngine;

public class DrawGenome extends PixelEngine
{
    @Override
    protected boolean onUserCreate()
    {
        return false;
    }
    
    public static void main(String[] args)
    {
        start(new DrawGenome(), 400, 400, 1, 1);
    }
}
