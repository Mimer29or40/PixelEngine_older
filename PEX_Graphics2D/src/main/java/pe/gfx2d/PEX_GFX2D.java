package pe.gfx2d;

import org.joml.Vector2dc;
import org.joml.Vector2fc;
import org.joml.Vector2i;
import org.joml.Vector2ic;
import pe.Mouse;
import pe.PEX;
import pe.Profiler;
import pe.Sprite;
import pe.render.DrawPattern;

import static pe.PixelEngine.*;

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
                renderer().stroke(sprite.getPixel((int) (ox + 0.5f), (int) (oy + 0.5f)));
                renderer().draw((int) i, (int) j);
            }
        }
    }

    public static void draw(Vector2ic p)
    {
        renderer().draw(p.x(), p.y());
    }

    public static void draw(Vector2fc p)
    {
        renderer().draw((int) p.x(), (int) p.y());
    }

    public static void draw(Vector2dc p)
    {
        renderer().draw((int) p.x(), (int) p.y());
    }

    public static void drawLine(Vector2ic p1, Vector2ic p2, DrawPattern pattern)
    {
        renderer().drawLine(p1.x(), p1.y(), p2.x(), p2.y());
    }

    public static void drawLine(Vector2ic p1, Vector2ic p2)
    {
        renderer().drawLine(p1.x(), p1.y(), p2.x(), p2.y());
    }

    public static void drawLine(Vector2fc p1, Vector2fc p2, DrawPattern pattern)
    {
        renderer().drawLine((int) p1.x(), (int) p1.y(), (int) p2.x(), (int) p2.y());
    }

    public static void drawLine(Vector2fc p1, Vector2fc p2)
    {
        renderer().drawLine((int) p1.x(), (int) p1.y(), (int) p2.x(), (int) p2.y());
    }

    public static void drawLine(Vector2dc p1, Vector2dc p2, DrawPattern pattern)
    {
        renderer().drawLine((int) p1.x(), (int) p1.y(), (int) p2.x(), (int) p2.y());
    }

    public static void drawLine(Vector2dc p1, Vector2dc p2)
    {
        renderer().drawLine((int) p1.x(), (int) p1.y(), (int) p2.x(), (int) p2.y());
    }

    public static void drawBezier(Vector2ic p1, Vector2ic p2, Vector2ic p3)
    {
        renderer().drawBezier(p1.x(), p1.y(), p2.x(), p2.y(), p3.x(), p3.y());
    }

    public static void drawBezier(Vector2fc p1, Vector2fc p2, Vector2fc p3)
    {
        renderer().drawBezier((int) p1.x(), (int) p1.y(), (int) p2.x(), (int) p2.y(), (int) p3.x(), (int) p3.y());
    }

    public static void drawBezier(Vector2dc p1, Vector2dc p2, Vector2dc p3)
    {
        renderer().drawBezier((int) p1.x(), (int) p1.y(), (int) p2.x(), (int) p2.y(), (int) p3.x(), (int) p3.y());
    }

    public static void drawCircle(Vector2ic p, int radius)
    {
        renderer().drawCircle(p.x(), p.y(), radius);
    }

    public static void drawCircle(Vector2fc p, int radius)
    {
        renderer().drawCircle((int) p.x(), (int) p.y(), radius);
    }

    public static void drawCircle(Vector2dc p, int radius)
    {
        renderer().drawCircle((int) p.x(), (int) p.y(), radius);
    }

    public static void drawEllipse(Vector2ic p, Vector2ic s)
    {
        renderer().drawEllipse(p.x(), p.y(), s.x(), s.y());
    }

    public static void drawEllipse(Vector2fc p, Vector2fc s)
    {
        renderer().drawEllipse((int) p.x(), (int) p.y(), (int) s.x(), (int) s.y());
    }

    public static void drawEllipse(Vector2dc p, Vector2dc s)
    {
        renderer().drawEllipse((int) p.x(), (int) p.y(), (int) s.x(), (int) s.y());
    }

    public static void drawRect(Vector2ic p, int w, int h)
    {
        renderer().drawRect(p.x(), p.y(), w, h);
    }

    public static void drawRect(Vector2fc p, int w, int h)
    {
        renderer().drawRect((int) p.x(), (int) p.y(), w, h);
    }

    public static void drawRect(Vector2dc p, int w, int h)
    {
        renderer().drawRect((int) p.x(), (int) p.y(), w, h);
    }
    
    public static void drawTriangle(Vector2ic p1, Vector2ic p2, Vector2ic p3)
    {
        renderer().drawTriangle(p1.x(), p1.y(), p2.x(), p2.y(), p3.x(), p3.y());
    }
    
    public static void drawTriangle(Vector2fc p1, Vector2fc p2, Vector2fc p3)
    {
        renderer().drawTriangle((int) p1.x(), (int) p1.y(), (int) p2.x(), (int) p2.y(), (int) p3.x(), (int) p3.y());
    }
    
    public static void drawTriangle(Vector2dc p1, Vector2dc p2, Vector2dc p3)
    {
        renderer().drawTriangle((int) p1.x(), (int) p1.y(), (int) p2.x(), (int) p2.y(), (int) p3.x(), (int) p3.y());
    }
    
    public static void drawSprite(Vector2ic p, Sprite sprite, int scale)
    {
        renderer().drawSprite(p.x(), p.y(), sprite, scale);
    }
    
    public static void drawSprite(Vector2ic p, Sprite sprite)
    {
        renderer().drawSprite(p.x(), p.y(), sprite, 1);
    }
    
    public static void drawSprite(Vector2fc p, Sprite sprite, int scale)
    {
        renderer().drawSprite((int) p.x(), (int) p.y(), sprite, scale);
    }
    
    public static void drawSprite(Vector2fc p, Sprite sprite)
    {
        renderer().drawSprite((int) p.x(), (int) p.y(), sprite, 1);
    }
    
    public static void drawSprite(Vector2dc p, Sprite sprite, int scale)
    {
        renderer().drawSprite((int) p.x(), (int) p.y(), sprite, scale);
    }
    
    public static void drawSprite(Vector2dc p, Sprite sprite)
    {
        renderer().drawSprite((int) p.x(), (int) p.y(), sprite, 1);
    }
    
    public static void drawPartialSprite(Vector2ic p, Sprite sprite, int ox, int oy, int w, int h, int scale)
    {
        renderer().drawPartialSprite(p.x(), p.y(), sprite, ox, oy, w, h, scale);
    }
    
    public static void drawPartialSprite(Vector2ic p, Sprite sprite, int ox, int oy, int w, int h)
    {
        renderer().drawPartialSprite(p.x(), p.y(), sprite, ox, oy, w, h, 1);
    }
    
    public static void drawPartialSprite(Vector2fc p, Sprite sprite, int ox, int oy, int w, int h, int scale)
    {
        renderer().drawPartialSprite((int) p.x(), (int) p.y(), sprite, ox, oy, w, h, scale);
    }
    
    public static void drawPartialSprite(Vector2fc p, Sprite sprite, int ox, int oy, int w, int h)
    {
        renderer().drawPartialSprite((int) p.x(), (int) p.y(), sprite, ox, oy, w, h, 1);
    }
    
    public static void drawPartialSprite(Vector2dc p, Sprite sprite, int ox, int oy, int w, int h, int scale)
    {
        renderer().drawPartialSprite((int) p.x(), (int) p.y(), sprite, ox, oy, w, h, scale);
    }
    
    public static void drawPartialSprite(Vector2dc p, Sprite sprite, int ox, int oy, int w, int h)
    {
        renderer().drawPartialSprite((int) p.x(), (int) p.y(), sprite, ox, oy, w, h, 1);
    }
    
    public static void drawString(Vector2ic p, String text, int scale)
    {
        renderer().drawString(p.x(), p.y(), text, scale);
    }
    
    public static void drawString(Vector2ic p, String text)
    {
        renderer().drawString(p.x(), p.y(), text, 1);
    }
    
    public static void drawString(Vector2fc p, String text, int scale)
    {
        renderer().drawString((int) p.x(), (int) p.y(), text, scale);
    }
    
    public static void drawString(Vector2fc p, String text)
    {
        renderer().drawString((int) p.x(), (int) p.y(), text, 1);
    }
    
    public static void drawString(Vector2dc p, String text, int scale)
    {
        renderer().drawString((int) p.x(), (int) p.y(), text, scale);
    }
    
    public static void drawString(Vector2dc p, String text)
    {
        renderer().drawString((int) p.x(), (int) p.y(), text, 1);
    }
    
    @Override
    public void beforeSetup()
    {
    
    }
    
    @Override
    public void afterSetup()
    {
    
    }
    
    @Override
    public void beforeDraw(double elapsedTime)
    {
        
    }
    
    @Override
    public void afterDraw(double elapsedTime)
    {
        
    }
    
    @Override
    public void beforeDestroy()
    {
    
    }
    
    @Override
    public void afterDestroy()
    {
    
    }
}
