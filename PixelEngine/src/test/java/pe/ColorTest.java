package pe;

import pe.color.Color;
import pe.draw.DrawMode;

public class ColorTest extends PixelEngine
{
    @Override
    protected boolean setup()
    {
        return true;
    }
    
    Color color = new Color();
    
    @Override
    protected boolean draw(double elapsedTime)
    {
        // for (int j = 0; j < screenHeight(); j++)
        // {
        //     for (int i = 0; i < screenHeight(); i++)
        //     {
        //         draw(i, j, color.set(i, j, 255));
        //     }
        // }
    
        clear(Color.BLANK);
    
        // drawMode((x, y, backdrop, source) -> Blend.ALPHA.blend(backdrop, source, new Color()));
        drawMode(DrawMode.BLEND);
    
        fillCircle(20, 20, 20, new Color(254, 0, 0, 255));
    
        fillCircle(Mouse.x(), Mouse.y(), 20, new Color(0, 0, 254, 127));
    
        return true;
    }
    
    @Override
    protected void destroy()
    {
    }
    
    public static void main(String[] args)
    {
        // int r = 255;
        // int g = 255;
        // int b = 255;
        // int a = 255;
        // println(r | (g << 8) | (b << 16) | (a << 24));
        start(new ColorTest(), 200, 200, 4, 4);
    }
}