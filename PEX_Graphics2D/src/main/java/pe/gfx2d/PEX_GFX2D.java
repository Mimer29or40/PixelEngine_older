package pe.gfx2d;

import pe.*;
import pe.vector.Vector2;

import static pe.PixelEngine.getScreenHeight;
import static pe.PixelEngine.getScreenWidth;

public class PEX_GFX2D extends PEX
{
    private static final Vector2 mousePos = new Vector2();
    private static final Vector2 viewport = new Vector2();
    
    public PEX_GFX2D(Profiler profiler)
    {
        super(profiler);
    }
    
    public static Vector2 getMousePos()
    {
        return PEX_GFX2D.mousePos.set(Mouse.getX(), Mouse.getY());
    }
    
    public static Vector2 getViewport()
    {
        return PEX_GFX2D.viewport.set(getScreenWidth(), getScreenHeight());
    }
    
    public static void drawSprite(Sprite sprite, Transform2D transform)
    {
        if (sprite == null) return;
        
        float ex = 0.0F, ey = 0.0F;
        float sx, sy;
        float px, py;
        
        px = sx = transform.forward_x(0.0F, 0.0F, 0.0F);
        py = sy = transform.forward_y(0.0F, 0.0F, 0.0F);
        ex = Math.max(ex, px);
        ey = Math.max(ey, py);
        
        px = transform.forward_x(sprite.getWidth(), sprite.getHeight(), px);
        py = transform.forward_y(sprite.getWidth(), sprite.getHeight(), py);
        sx = Math.min(sx, px);
        sy = Math.min(sy, py);
        ex = Math.max(ex, px);
        ey = Math.max(ey, py);
        
        px = transform.forward_x(0.0F, sprite.getHeight(), px);
        py = transform.forward_y(0.0F, sprite.getHeight(), py);
        sx = Math.min(sx, px);
        sy = Math.min(sy, py);
        ex = Math.max(ex, px);
        ey = Math.max(ey, py);
        
        px = transform.forward_x(sprite.getHeight(), 0.0F, px);
        py = transform.forward_y(sprite.getHeight(), 0.0F, py);
        sx = Math.min(sx, px);
        sy = Math.min(sy, py);
        ex = Math.max(ex, px);
        ey = Math.max(ey, py);
        
        // Perform inversion of transform if required
        transform.invert();
        
        float temp;
        if (ex < sx)
        {
            temp = ex;
            ex = sx;
            sx = temp;
        }
        if (ey < sy)
        {
            temp = ey;
            ey = sy;
            sy = temp;
        }
        
        // Iterate through render space, and sample Sprite from suitable texel location
        for (float i = sx; i < ex; i++)
        {
            for (float j = sy; j < ey; j++)
            {
                float ox = transform.backward_x(i, j, 0.0F);
                float oy = transform.backward_y(i, j, 0.0F);
                PixelEngine.draw((int) i, (int) j, sprite.getPixel((int) (ox + 0.5f), (int) (oy + 0.5f)));
            }
        }
    }
    
    public static void draw(Vector2 pos, Color p)
    {
        PixelEngine.draw(pos.xi(), pos.yi(), p);
    }
    
    public static void draw(Vector2 pos)
    {
        PixelEngine.draw(pos.xi(), pos.yi());
    }
    
    public static void drawLine(Vector2 pos1, Vector2 pos2, Color p, int pattern)
    {
        PixelEngine.drawLine(pos1.xi(), pos1.yi(), pos2.xi(), pos2.yi(), p, pattern);
    }
    
    public static void drawLine(Vector2 pos1, Vector2 pos2, Color p)
    {
        PixelEngine.drawLine(pos1.xi(), pos1.yi(), pos2.xi(), pos2.yi(), p, 0xFFFFFFFF);
    }
    
    public static void drawLine(Vector2 pos1, Vector2 pos2, int pattern)
    {
        PixelEngine.drawLine(pos1.xi(), pos1.yi(), pos2.xi(), pos2.yi(), Color.WHITE, pattern);
    }
    
    public static void drawLine(Vector2 pos1, Vector2 pos2)
    {
        PixelEngine.drawLine(pos1.xi(), pos1.yi(), pos2.xi(), pos2.yi(), Color.WHITE, 0xFFFFFFFF);
    }
    
    public static void drawCircle(Vector2 pos, int radius, Color p, int mask)
    {
        PixelEngine.drawCircle(pos.xi(), pos.yi(), radius, p, mask);
    }
    
    public static void drawCircle(Vector2 pos, int radius, Color p)
    {
        PixelEngine.drawCircle(pos.xi(), pos.yi(), radius, p, 0xFF);
    }
    
    public static void drawCircle(Vector2 pos, int radius, int mask)
    {
        PixelEngine.drawCircle(pos.xi(), pos.yi(), radius, Color.WHITE, mask);
    }
    
    public static void drawCircle(Vector2 pos, int radius)
    {
        PixelEngine.drawCircle(pos.xi(), pos.yi(), radius, Color.WHITE, 0xFF);
    }
    
    public static void fillCircle(Vector2 pos, int radius, Color p)
    {
        PixelEngine.fillCircle(pos.xi(), pos.yi(), radius, p);
    }
    
    public static void fillCircle(Vector2 pos, int radius)
    {
        PixelEngine.fillCircle(pos.xi(), pos.yi(), radius, Color.WHITE);
    }
    
    public static void drawRect(Vector2 pos, int w, int h, Color p)
    {
        PixelEngine.drawRect(pos.xi(), pos.yi(), w, h, p);
    }
    
    public static void drawRect(Vector2 pos, int w, int h)
    {
        PixelEngine.drawRect(pos.xi(), pos.yi(), w, h, Color.WHITE);
    }
    
    public static void fillRect(Vector2 pos, int w, int h, Color p)
    {
        PixelEngine.fillRect(pos.xi(), pos.yi(), w, h, p);
    }
    
    public static void fillRect(Vector2 pos, int w, int h)
    {
        PixelEngine.fillRect(pos.xi(), pos.yi(), w, h, Color.WHITE);
    }
    
    public static void drawTriangle(Vector2 pos1, Vector2 pos2, Vector2 pos3, Color p)
    {
        PixelEngine.drawTriangle(pos1.xi(), pos1.yi(), pos2.xi(), pos2.yi(), pos3.xi(), pos3.yi(), p);
    }
    
    public static void drawTriangle(Vector2 pos1, Vector2 pos2, Vector2 pos3)
    {
        PixelEngine.drawTriangle(pos1.xi(), pos1.yi(), pos2.xi(), pos2.yi(), pos3.xi(), pos3.yi(), Color.WHITE);
    }
    
    public static void fillTriangle(Vector2 pos1, Vector2 pos2, Vector2 pos3, Color p)
    {
        PixelEngine.fillTriangle(pos1.xi(), pos1.yi(), pos2.xi(), pos2.yi(), pos3.xi(), pos3.yi(), p);
    }
    
    public static void fillTriangle(Vector2 pos1, Vector2 pos2, Vector2 pos3)
    {
        PixelEngine.fillTriangle(pos1.xi(), pos1.yi(), pos2.xi(), pos2.yi(), pos3.xi(), pos3.yi(), Color.WHITE);
    }
    
    public static void drawSprite(Vector2 pos, Sprite sprite, int scale)
    {
        PixelEngine.drawSprite(pos.xi(), pos.yi(), sprite, scale);
    }
    
    public static void drawSprite(Vector2 pos, Sprite sprite)
    {
        PixelEngine.drawSprite(pos.xi(), pos.yi(), sprite, 1);
    }
    
    public static void drawPartialSprite(Vector2 pos, Sprite sprite, int ox, int oy, int w, int h, int scale)
    {
        PixelEngine.drawPartialSprite(pos.xi(), pos.yi(), sprite, ox, oy, w, h, scale);
    }
    
    public static void drawPartialSprite(Vector2 pos, Sprite sprite, int ox, int oy, int w, int h)
    {
        PixelEngine.drawPartialSprite(pos.xi(), pos.yi(), sprite, ox, oy, w, h, 1);
    }
    
    public static void drawString(Vector2 pos, String text, Color color, int scale)
    {
        PixelEngine.drawString(pos.xi(), pos.yi(), text, color, scale);
    }
    
    public static void drawString(Vector2 pos, String text, Color color)
    {
        PixelEngine.drawString(pos.xi(), pos.yi(), text, color, 1);
    }
    
    public static void drawString(Vector2 pos, String text, int scale)
    {
        PixelEngine.drawString(pos.xi(), pos.yi(), text, Color.WHITE, scale);
    }
    
    public static void drawString(Vector2 pos, String text)
    {
        PixelEngine.drawString(pos.xi(), pos.yi(), text, Color.WHITE, 1);
    }
    
    @Override
    public void initialize()
    {
    
    }
    
    @Override
    public void beforeUserUpdate(double elapsedTime)
    {
    
    }
    
    @Override
    public void afterUserUpdate(double elapsedTime)
    {
    
    }
    
    @Override
    public void destroy()
    {
    
    }
}
