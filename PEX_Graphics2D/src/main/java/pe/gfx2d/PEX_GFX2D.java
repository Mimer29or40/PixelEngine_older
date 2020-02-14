package pe.gfx2d;

import org.joml.Vector2dc;
import org.joml.Vector2fc;
import org.joml.Vector2i;
import org.joml.Vector2ic;
import pe.*;
import pe.draw.DrawPattern;

import static pe.PixelEngine.screenHeight;
import static pe.PixelEngine.screenWidth;

@SuppressWarnings("unused")
public class PEX_GFX2D extends PEX
{
    private static final Vector2i mousePos = new Vector2i();
    private static final Vector2i viewport = new Vector2i();
    
    public PEX_GFX2D(Profiler profiler)
    {
        super(profiler);
    }
    
    public static Vector2ic getMousePos()
    {
        return PEX_GFX2D.mousePos.set(Mouse.x(), Mouse.y());
    }
    
    public static Vector2ic getViewport()
    {
        return PEX_GFX2D.viewport.set(screenWidth(), screenHeight());
    }
    
    // TODO - This needs an equivalent
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
            ex   = sx;
            sx   = temp;
        }
        if (ey < sy)
        {
            temp = ey;
            ey   = sy;
            sy   = temp;
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
    
    public static void draw(Vector2ic p, Color c)
    {
        PixelEngine.draw(p.x(), p.y(), c);
    }
    
    public static void draw(Vector2ic p)
    {
        PixelEngine.draw(p.x(), p.y());
    }
    
    public static void draw(Vector2fc p, Color c)
    {
        PixelEngine.draw((int) p.x(), (int) p.y(), c);
    }
    
    public static void draw(Vector2fc p)
    {
        PixelEngine.draw((int) p.x(), (int) p.y());
    }
    
    public static void draw(Vector2dc p, Color c)
    {
        PixelEngine.draw((int) p.x(), (int) p.y(), c);
    }
    
    public static void draw(Vector2dc p)
    {
        PixelEngine.draw((int) p.x(), (int) p.y());
    }
    
    public static void drawLine(Vector2ic p1, Vector2ic p2, int w, Color c, DrawPattern pattern)
    {
        PixelEngine.drawLine(p1.x(), p1.y(), p2.x(), p2.y(), w, c, pattern);
    }
    
    public static void drawLine(Vector2ic p1, Vector2ic p2, int w, Color c)
    {
        PixelEngine.drawLine(p1.x(), p1.y(), p2.x(), p2.y(), w, c, DrawPattern.SOLID);
    }
    
    public static void drawLine(Vector2ic p1, Vector2ic p2, int w, DrawPattern pattern)
    {
        PixelEngine.drawLine(p1.x(), p1.y(), p2.x(), p2.y(), w, Color.WHITE, pattern);
    }
    
    public static void drawLine(Vector2ic p1, Vector2ic p2, int w)
    {
        PixelEngine.drawLine(p1.x(), p1.y(), p2.x(), p2.y(), w, Color.WHITE, DrawPattern.SOLID);
    }
    
    public static void drawLine(Vector2ic p1, Vector2ic p2, Color c, DrawPattern pattern)
    {
        PixelEngine.drawLine(p1.x(), p1.y(), p2.x(), p2.y(), 1, c, pattern);
    }
    
    public static void drawLine(Vector2ic p1, Vector2ic p2, Color c)
    {
        PixelEngine.drawLine(p1.x(), p1.y(), p2.x(), p2.y(), 1, c, DrawPattern.SOLID);
    }
    
    public static void drawLine(Vector2ic p1, Vector2ic p2, DrawPattern pattern)
    {
        PixelEngine.drawLine(p1.x(), p1.y(), p2.x(), p2.y(), 1, Color.WHITE, pattern);
    }
    
    public static void drawLine(Vector2ic p1, Vector2ic p2)
    {
        PixelEngine.drawLine(p1.x(), p1.y(), p2.x(), p2.y(), 1, Color.WHITE, DrawPattern.SOLID);
    }
    
    public static void drawLine(Vector2fc p1, Vector2fc p2, int w, Color c, DrawPattern pattern)
    {
        PixelEngine.drawLine((int) p1.x(), (int) p1.y(), (int) p2.x(), (int) p2.y(), w, c, pattern);
    }
    
    public static void drawLine(Vector2fc p1, Vector2fc p2, int w, Color c)
    {
        PixelEngine.drawLine((int) p1.x(), (int) p1.y(), (int) p2.x(), (int) p2.y(), w, c, DrawPattern.SOLID);
    }
    
    public static void drawLine(Vector2fc p1, Vector2fc p2, int w, DrawPattern pattern)
    {
        PixelEngine.drawLine((int) p1.x(), (int) p1.y(), (int) p2.x(), (int) p2.y(), w, Color.WHITE, pattern);
    }
    
    public static void drawLine(Vector2fc p1, Vector2fc p2, int w)
    {
        PixelEngine.drawLine((int) p1.x(), (int) p1.y(), (int) p2.x(), (int) p2.y(), w, Color.WHITE, DrawPattern.SOLID);
    }
    
    public static void drawLine(Vector2fc p1, Vector2fc p2, Color c, DrawPattern pattern)
    {
        PixelEngine.drawLine((int) p1.x(), (int) p1.y(), (int) p2.x(), (int) p2.y(), 1, c, pattern);
    }
    
    public static void drawLine(Vector2fc p1, Vector2fc p2, Color c)
    {
        PixelEngine.drawLine((int) p1.x(), (int) p1.y(), (int) p2.x(), (int) p2.y(), 1, c, DrawPattern.SOLID);
    }
    
    public static void drawLine(Vector2fc p1, Vector2fc p2, DrawPattern pattern)
    {
        PixelEngine.drawLine((int) p1.x(), (int) p1.y(), (int) p2.x(), (int) p2.y(), 1, Color.WHITE, pattern);
    }
    
    public static void drawLine(Vector2fc p1, Vector2fc p2)
    {
        PixelEngine.drawLine((int) p1.x(), (int) p1.y(), (int) p2.x(), (int) p2.y(), 1, Color.WHITE, DrawPattern.SOLID);
    }
    
    public static void drawLine(Vector2dc p1, Vector2dc p2, int w, Color c, DrawPattern pattern)
    {
        PixelEngine.drawLine((int) p1.x(), (int) p1.y(), (int) p2.x(), (int) p2.y(), w, c, pattern);
    }
    
    public static void drawLine(Vector2dc p1, Vector2dc p2, int w, Color c)
    {
        PixelEngine.drawLine((int) p1.x(), (int) p1.y(), (int) p2.x(), (int) p2.y(), w, c, DrawPattern.SOLID);
    }
    
    public static void drawLine(Vector2dc p1, Vector2dc p2, int w, DrawPattern pattern)
    {
        PixelEngine.drawLine((int) p1.x(), (int) p1.y(), (int) p2.x(), (int) p2.y(), w, Color.WHITE, pattern);
    }
    
    public static void drawLine(Vector2dc p1, Vector2dc p2, int w)
    {
        PixelEngine.drawLine((int) p1.x(), (int) p1.y(), (int) p2.x(), (int) p2.y(), w, Color.WHITE, DrawPattern.SOLID);
    }
    
    public static void drawLine(Vector2dc p1, Vector2dc p2, Color c, DrawPattern pattern)
    {
        PixelEngine.drawLine((int) p1.x(), (int) p1.y(), (int) p2.x(), (int) p2.y(), 1, c, pattern);
    }
    
    public static void drawLine(Vector2dc p1, Vector2dc p2, Color c)
    {
        PixelEngine.drawLine((int) p1.x(), (int) p1.y(), (int) p2.x(), (int) p2.y(), 1, c, DrawPattern.SOLID);
    }
    
    public static void drawLine(Vector2dc p1, Vector2dc p2, DrawPattern pattern)
    {
        PixelEngine.drawLine((int) p1.x(), (int) p1.y(), (int) p2.x(), (int) p2.y(), 1, Color.WHITE, pattern);
    }
    
    public static void drawLine(Vector2dc p1, Vector2dc p2)
    {
        PixelEngine.drawLine((int) p1.x(), (int) p1.y(), (int) p2.x(), (int) p2.y(), 1, Color.WHITE, DrawPattern.SOLID);
    }
    
    public static void drawBezier(Vector2ic p1, Vector2ic p2, Vector2ic p3, Color c)
    {
        PixelEngine.drawBezier(p1.x(), p1.y(), p2.x(), p2.y(), p3.x(), p3.y(), c);
    }
    
    public static void drawBezier(Vector2ic p1, Vector2ic p2, Vector2ic p3)
    {
        PixelEngine.drawBezier(p1.x(), p1.y(), p2.x(), p2.y(), p3.x(), p3.y(), Color.WHITE);
    }
    
    public static void drawBezier(Vector2fc p1, Vector2fc p2, Vector2fc p3, Color c)
    {
        PixelEngine.drawBezier((int) p1.x(), (int) p1.y(), (int) p2.x(), (int) p2.y(), (int) p3.x(), (int) p3.y(), c);
    }
    
    public static void drawBezier(Vector2fc p1, Vector2fc p2, Vector2fc p3)
    {
        PixelEngine.drawBezier((int) p1.x(), (int) p1.y(), (int) p2.x(), (int) p2.y(), (int) p3.x(), (int) p3.y(), Color.WHITE);
    }
    
    public static void drawBezier(Vector2dc p1, Vector2dc p2, Vector2dc p3, Color c)
    {
        PixelEngine.drawBezier((int) p1.x(), (int) p1.y(), (int) p2.x(), (int) p2.y(), (int) p3.x(), (int) p3.y(), c);
    }
    
    public static void drawBezier(Vector2dc p1, Vector2dc p2, Vector2dc p3)
    {
        PixelEngine.drawBezier((int) p1.x(), (int) p1.y(), (int) p2.x(), (int) p2.y(), (int) p3.x(), (int) p3.y(), Color.WHITE);
    }
    
    public static void drawCircle(Vector2ic p, int radius, Color c)
    {
        PixelEngine.drawCircle(p.x(), p.y(), radius, c);
    }
    
    public static void drawCircle(Vector2ic p, int radius)
    {
        PixelEngine.drawCircle(p.x(), p.y(), radius, Color.WHITE);
    }
    
    public static void drawCircle(Vector2fc p, int radius, Color c)
    {
        PixelEngine.drawCircle((int) p.x(), (int) p.y(), radius, c);
    }
    
    public static void drawCircle(Vector2fc p, int radius)
    {
        PixelEngine.drawCircle((int) p.x(), (int) p.y(), radius, Color.WHITE);
    }
    
    public static void drawCircle(Vector2dc p, int radius, Color c)
    {
        PixelEngine.drawCircle((int) p.x(), (int) p.y(), radius, c);
    }
    
    public static void drawCircle(Vector2dc p, int radius)
    {
        PixelEngine.drawCircle((int) p.x(), (int) p.y(), radius, Color.WHITE);
    }
    
    public static void fillCircle(Vector2ic p, int radius, Color c)
    {
        PixelEngine.fillCircle(p.x(), p.y(), radius, c);
    }
    
    public static void fillCircle(Vector2ic p, int radius)
    {
        PixelEngine.fillCircle(p.x(), p.y(), radius, Color.WHITE);
    }
    
    public static void fillCircle(Vector2fc p, int radius, Color c)
    {
        PixelEngine.fillCircle((int) p.x(), (int) p.y(), radius, c);
    }
    
    public static void fillCircle(Vector2fc p, int radius)
    {
        PixelEngine.fillCircle((int) p.x(), (int) p.y(), radius, Color.WHITE);
    }
    
    public static void fillCircle(Vector2dc p, int radius, Color c)
    {
        PixelEngine.fillCircle((int) p.x(), (int) p.y(), radius, c);
    }
    
    public static void fillCircle(Vector2dc p, int radius)
    {
        PixelEngine.fillCircle((int) p.x(), (int) p.y(), radius, Color.WHITE);
    }
    
    public static void drawEllipse(Vector2ic p, Vector2ic s, Color c)
    {
        PixelEngine.drawEllipse(p.x(), p.y(), s.x(), s.y(), c);
    }
    
    public static void drawEllipse(Vector2ic p, Vector2ic s)
    {
        PixelEngine.drawEllipse(p.x(), p.y(), s.x(), s.y(), Color.WHITE);
    }
    
    public static void drawEllipse(Vector2fc p, Vector2fc s, Color c)
    {
        PixelEngine.drawEllipse((int) p.x(), (int) p.y(), (int) s.x(), (int) s.y(), c);
    }
    
    public static void drawEllipse(Vector2fc p, Vector2fc s)
    {
        PixelEngine.drawEllipse((int) p.x(), (int) p.y(), (int) s.x(), (int) s.y(), Color.WHITE);
    }
    
    public static void drawEllipse(Vector2dc p, Vector2dc s, Color c)
    {
        PixelEngine.drawEllipse((int) p.x(), (int) p.y(), (int) s.x(), (int) s.y(), c);
    }
    
    public static void drawEllipse(Vector2dc p, Vector2dc s)
    {
        PixelEngine.drawEllipse((int) p.x(), (int) p.y(), (int) s.x(), (int) s.y(), Color.WHITE);
    }
    
    public static void fillEllipse(Vector2ic p, Vector2ic s, Color c)
    {
        PixelEngine.fillEllipse(p.x(), p.y(), s.x(), s.y(), c);
    }
    
    public static void fillEllipse(Vector2ic p, Vector2ic s)
    {
        PixelEngine.fillEllipse(p.x(), p.y(), s.x(), s.y(), Color.WHITE);
    }
    
    public static void fillEllipse(Vector2fc p, Vector2fc s, Color c)
    {
        PixelEngine.fillEllipse((int) p.x(), (int) p.y(), (int) s.x(), (int) s.y(), c);
    }
    
    public static void fillEllipse(Vector2fc p, Vector2fc s)
    {
        PixelEngine.fillEllipse((int) p.x(), (int) p.y(), (int) s.x(), (int) s.y(), Color.WHITE);
    }
    
    public static void fillEllipse(Vector2dc p, Vector2dc s, Color c)
    {
        PixelEngine.fillEllipse((int) p.x(), (int) p.y(), (int) s.x(), (int) s.y(), c);
    }
    
    public static void fillEllipse(Vector2dc p, Vector2dc s)
    {
        PixelEngine.fillEllipse((int) p.x(), (int) p.y(), (int) s.x(), (int) s.y(), Color.WHITE);
    }
    
    public static void drawRect(Vector2ic p, int w, int h, Color c)
    {
        PixelEngine.drawRect(p.x(), p.y(), w, h, c);
    }
    
    public static void drawRect(Vector2ic p, int w, int h)
    {
        PixelEngine.drawRect(p.x(), p.y(), w, h, Color.WHITE);
    }
    
    public static void drawRect(Vector2fc p, int w, int h, Color c)
    {
        PixelEngine.drawRect((int) p.x(), (int) p.y(), w, h, c);
    }
    
    public static void drawRect(Vector2fc p, int w, int h)
    {
        PixelEngine.drawRect((int) p.x(), (int) p.y(), w, h, Color.WHITE);
    }
    
    public static void drawRect(Vector2dc p, int w, int h, Color c)
    {
        PixelEngine.drawRect((int) p.x(), (int) p.y(), w, h, c);
    }
    
    public static void drawRect(Vector2dc p, int w, int h)
    {
        PixelEngine.drawRect((int) p.x(), (int) p.y(), w, h, Color.WHITE);
    }
    
    public static void fillRect(Vector2ic p, int w, int h, Color c)
    {
        PixelEngine.fillRect(p.x(), p.y(), w, h, c);
    }
    
    public static void fillRect(Vector2ic p, int w, int h)
    {
        PixelEngine.fillRect(p.x(), p.y(), w, h, Color.WHITE);
    }
    
    public static void fillRect(Vector2fc p, int w, int h, Color c)
    {
        PixelEngine.fillRect((int) p.x(), (int) p.y(), w, h, c);
    }
    
    public static void fillRect(Vector2fc p, int w, int h)
    {
        PixelEngine.fillRect((int) p.x(), (int) p.y(), w, h, Color.WHITE);
    }
    
    public static void fillRect(Vector2dc p, int w, int h, Color c)
    {
        PixelEngine.fillRect((int) p.x(), (int) p.y(), w, h, c);
    }
    
    public static void fillRect(Vector2dc p, int w, int h)
    {
        PixelEngine.fillRect((int) p.x(), (int) p.y(), w, h, Color.WHITE);
    }
    
    public static void drawTriangle(Vector2ic p1, Vector2ic p2, Vector2ic p3, Color c)
    {
        PixelEngine.drawTriangle(p1.x(), p1.y(), p2.x(), p2.y(), p3.x(), p3.y(), c);
    }
    
    public static void drawTriangle(Vector2ic p1, Vector2ic p2, Vector2ic p3)
    {
        PixelEngine.drawTriangle(p1.x(), p1.y(), p2.x(), p2.y(), p3.x(), p3.y(), Color.WHITE);
    }
    
    public static void drawTriangle(Vector2fc p1, Vector2fc p2, Vector2fc p3, Color c)
    {
        PixelEngine.drawTriangle((int) p1.x(), (int) p1.y(), (int) p2.x(), (int) p2.y(), (int) p3.x(), (int) p3.y(), c);
    }
    
    public static void drawTriangle(Vector2fc p1, Vector2fc p2, Vector2fc p3)
    {
        PixelEngine.drawTriangle((int) p1.x(), (int) p1.y(), (int) p2.x(), (int) p2.y(), (int) p3.x(), (int) p3.y(), Color.WHITE);
    }
    
    public static void drawTriangle(Vector2dc p1, Vector2dc p2, Vector2dc p3, Color c)
    {
        PixelEngine.drawTriangle((int) p1.x(), (int) p1.y(), (int) p2.x(), (int) p2.y(), (int) p3.x(), (int) p3.y(), c);
    }
    
    public static void drawTriangle(Vector2dc p1, Vector2dc p2, Vector2dc p3)
    {
        PixelEngine.drawTriangle((int) p1.x(), (int) p1.y(), (int) p2.x(), (int) p2.y(), (int) p3.x(), (int) p3.y(), Color.WHITE);
    }
    
    public static void fillTriangle(Vector2ic p1, Vector2ic p2, Vector2ic p3, Color c)
    {
        PixelEngine.fillTriangle(p1.x(), p1.y(), p2.x(), p2.y(), p3.x(), p3.y(), c);
    }
    
    public static void fillTriangle(Vector2ic p1, Vector2ic p2, Vector2ic p3)
    {
        PixelEngine.fillTriangle(p1.x(), p1.y(), p2.x(), p2.y(), p3.x(), p3.y(), Color.WHITE);
    }
    
    public static void fillTriangle(Vector2fc p1, Vector2fc p2, Vector2fc p3, Color c)
    {
        PixelEngine.fillTriangle((int) p1.x(), (int) p1.y(), (int) p2.x(), (int) p2.y(), (int) p3.x(), (int) p3.y(), c);
    }
    
    public static void fillTriangle(Vector2fc p1, Vector2fc p2, Vector2fc p3)
    {
        PixelEngine.fillTriangle((int) p1.x(), (int) p1.y(), (int) p2.x(), (int) p2.y(), (int) p3.x(), (int) p3.y(), Color.WHITE);
    }
    
    public static void fillTriangle(Vector2dc p1, Vector2dc p2, Vector2dc p3, Color c)
    {
        PixelEngine.fillTriangle((int) p1.x(), (int) p1.y(), (int) p2.x(), (int) p2.y(), (int) p3.x(), (int) p3.y(), c);
    }
    
    public static void fillTriangle(Vector2dc p1, Vector2dc p2, Vector2dc p3)
    {
        PixelEngine.fillTriangle((int) p1.x(), (int) p1.y(), (int) p2.x(), (int) p2.y(), (int) p3.x(), (int) p3.y(), Color.WHITE);
    }
    
    public static void drawSprite(Vector2ic p, Sprite sprite, int scale)
    {
        PixelEngine.drawSprite(p.x(), p.y(), sprite, scale);
    }
    
    public static void drawSprite(Vector2ic p, Sprite sprite)
    {
        PixelEngine.drawSprite(p.x(), p.y(), sprite, 1);
    }
    
    public static void drawSprite(Vector2fc p, Sprite sprite, int scale)
    {
        PixelEngine.drawSprite((int) p.x(), (int) p.y(), sprite, scale);
    }
    
    public static void drawSprite(Vector2fc p, Sprite sprite)
    {
        PixelEngine.drawSprite((int) p.x(), (int) p.y(), sprite, 1);
    }
    
    public static void drawSprite(Vector2dc p, Sprite sprite, int scale)
    {
        PixelEngine.drawSprite((int) p.x(), (int) p.y(), sprite, scale);
    }
    
    public static void drawSprite(Vector2dc p, Sprite sprite)
    {
        PixelEngine.drawSprite((int) p.x(), (int) p.y(), sprite, 1);
    }
    
    public static void drawPartialSprite(Vector2ic p, Sprite sprite, int ox, int oy, int w, int h, int scale)
    {
        PixelEngine.drawPartialSprite(p.x(), p.y(), sprite, ox, oy, w, h, scale);
    }
    
    public static void drawPartialSprite(Vector2ic p, Sprite sprite, int ox, int oy, int w, int h)
    {
        PixelEngine.drawPartialSprite(p.x(), p.y(), sprite, ox, oy, w, h, 1);
    }
    
    public static void drawPartialSprite(Vector2fc p, Sprite sprite, int ox, int oy, int w, int h, int scale)
    {
        PixelEngine.drawPartialSprite((int) p.x(), (int) p.y(), sprite, ox, oy, w, h, scale);
    }
    
    public static void drawPartialSprite(Vector2fc p, Sprite sprite, int ox, int oy, int w, int h)
    {
        PixelEngine.drawPartialSprite((int) p.x(), (int) p.y(), sprite, ox, oy, w, h, 1);
    }
    
    public static void drawPartialSprite(Vector2dc p, Sprite sprite, int ox, int oy, int w, int h, int scale)
    {
        PixelEngine.drawPartialSprite((int) p.x(), (int) p.y(), sprite, ox, oy, w, h, scale);
    }
    
    public static void drawPartialSprite(Vector2dc p, Sprite sprite, int ox, int oy, int w, int h)
    {
        PixelEngine.drawPartialSprite((int) p.x(), (int) p.y(), sprite, ox, oy, w, h, 1);
    }
    
    public static void drawString(Vector2ic p, String text, Color color, int scale)
    {
        PixelEngine.drawString(p.x(), p.y(), text, color, scale);
    }
    
    public static void drawString(Vector2ic p, String text, Color color)
    {
        PixelEngine.drawString(p.x(), p.y(), text, color, 1);
    }
    
    public static void drawString(Vector2ic p, String text, int scale)
    {
        PixelEngine.drawString(p.x(), p.y(), text, Color.WHITE, scale);
    }
    
    public static void drawString(Vector2ic p, String text)
    {
        PixelEngine.drawString(p.x(), p.y(), text, Color.WHITE, 1);
    }
    
    public static void drawString(Vector2fc p, String text, Color color, int scale)
    {
        PixelEngine.drawString((int) p.x(), (int) p.y(), text, color, scale);
    }
    
    public static void drawString(Vector2fc p, String text, Color color)
    {
        PixelEngine.drawString((int) p.x(), (int) p.y(), text, color, 1);
    }
    
    public static void drawString(Vector2fc p, String text, int scale)
    {
        PixelEngine.drawString((int) p.x(), (int) p.y(), text, Color.WHITE, scale);
    }
    
    public static void drawString(Vector2fc p, String text)
    {
        PixelEngine.drawString((int) p.x(), (int) p.y(), text, Color.WHITE, 1);
    }
    
    public static void drawString(Vector2dc p, String text, Color color, int scale)
    {
        PixelEngine.drawString((int) p.x(), (int) p.y(), text, color, scale);
    }
    
    public static void drawString(Vector2dc p, String text, Color color)
    {
        PixelEngine.drawString((int) p.x(), (int) p.y(), text, color, 1);
    }
    
    public static void drawString(Vector2dc p, String text, int scale)
    {
        PixelEngine.drawString((int) p.x(), (int) p.y(), text, Color.WHITE, scale);
    }
    
    public static void drawString(Vector2dc p, String text)
    {
        PixelEngine.drawString((int) p.x(), (int) p.y(), text, Color.WHITE, 1);
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
