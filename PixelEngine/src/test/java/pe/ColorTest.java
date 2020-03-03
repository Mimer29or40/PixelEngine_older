package pe;

import pe.color.Color;

public class ColorTest extends PixelEngine
{
    @Override
    protected void setup()
    {
        size(200, 200, 4, 4);
    }
    
    @Override
    protected void draw(double elapsedTime)
    {
        // for (int j = 0; j < screenHeight(); j++)
        // {
        //     for (int i = 0; i < screenHeight(); i++)
        //     {
        //         renderer().stroke(i, j, 255);
        //         renderer().draw(i, j);
        //     }
        // }
        
        renderer().clear(Color.BLACK);
    
        // // renderer().drawMode((x, y, backdrop, source) -> Blend.ALPHA.blend(backdrop, source, new Color()));
        // renderer().drawMode(DrawMode.BLEND);
        renderer().stroke(255, 0, 0);
    
        renderer().line(0, 0, Mouse.x(), Mouse.y());
        // renderer().line(-1, -1, 1, 1);
    
        // renderer().noStroke();
        // renderer().fill(254, 0, 0, 255);
        // renderer().circle(20, 20, 20);
        //
        // renderer().fill(0, 0, 254, 127);
        // renderer().circle(Mouse.x(), Mouse.y(), 20);
    
        if (Keyboard.SPACE.down())
        {
            printFrameData("");
        }
    }
    
    public static void main(String[] args)
    {
        // println(255);
        // println(((byte) 255) & 0xFF);
        // println((int) ((byte) 255) & 0xFF);
        // int r = 255;
        // int g = 255;
        // int b = 255;
        // int a = 255;
        // println(r | (g << 8) | (b << 16) | (a << 24));
        // start(new ColorTest(), 200, 200, 4, 4);
        enableProfiler();
        start(new ColorTest());
    }
}