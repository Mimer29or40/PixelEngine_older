package pe.render;

import pe.Logger;
import pe.Profiler;
import pe.Sprite;
import pe.color.Blend;
import pe.color.Color;
import pe.color.Colorc;
import pe.color.IBlendPos;

public abstract class Renderer
{
    protected static final Logger LOGGER = Logger.getLogger();
    
    protected Texture window, prev;
    protected Sprite font, target;
    
    protected       DrawMode  drawMode = DrawMode.NORMAL;
    protected final Blend     blend    = new Blend();
    protected       IBlendPos blendFunc;
    
    protected final Color stroke = new Color(Color.BLACK);
    protected final Color fill   = new Color(Color.WHITE);
    protected final Color clear  = new Color(Color.BACKGROUND_GREY);
    
    protected int strokeWeight = 1;
    
    public Sprite drawTarget()
    {
        return this.target;
    }
    
    public void drawTarget(Sprite target)
    {
        this.target = target != null ? target : this.window;
    }
    
    public DrawMode drawMode()
    {
        return this.drawMode;
    }
    
    public void drawMode(DrawMode mode)
    {
        this.drawMode = mode;
    }
    
    public void drawMode(IBlendPos pixelFunc)
    {
        this.drawMode  = DrawMode.CUSTOM;
        this.blendFunc = pixelFunc;
    }
    
    public void stroke(Number r, Number g, Number b, Number a)
    {
        this.stroke.set(r, g, b, a);
    }
    
    public void stroke(Number r, Number g, Number b)
    {
        this.stroke.set(r, g, b);
    }
    
    public void stroke(Number grey, Number a)
    {
        this.stroke.set(grey, a);
    }
    
    public void stroke(Number grey)
    {
        this.stroke.set(grey);
    }
    
    public void stroke(Colorc color)
    {
        this.stroke.set(color);
    }
    
    public void noStroke()
    {
        this.stroke.a(0);
    }
    
    public void fill(Number r, Number g, Number b, Number a)
    {
        this.fill.set(r, g, b, a);
    }
    
    public void fill(Number r, Number g, Number b)
    {
        this.fill.set(r, g, b);
    }
    
    public void fill(Number grey, Number a)
    {
        this.fill.set(grey, a);
    }
    
    public void fill(Number grey)
    {
        this.fill.set(grey);
    }
    
    public void fill(Colorc color)
    {
        this.fill.set(color);
    }
    
    public void noFill()
    {
        this.fill.a(0);
    }
    
    public void strokeWeight(int strokeWeight)
    {
        if (strokeWeight < 1) throw new RuntimeException("strokeWeight must be >= 1");
        this.strokeWeight = strokeWeight;
    }
    
    public abstract void init();
    
    public abstract void render(boolean update, Profiler profiler);
    
    public void clear(Number r, Number g, Number b, Number a)
    {
        this.clear.set(r, g, b, a);
        clearImpl();
    }
    
    public void clear(Number r, Number g, Number b)
    {
        this.clear.set(r, g, b);
        clearImpl();
    }
    
    public void clear(Number grey, Number a)
    {
        this.clear.set(grey, a);
        clearImpl();
    }
    
    public void clear(Number grey)
    {
        this.clear.set(grey);
        clearImpl();
    }
    
    public void clear()
    {
        this.clear.set(Color.BACKGROUND_GREY);
        clearImpl();
    }
    
    public void clear(Colorc color)
    {
        this.clear.set(color);
        clearImpl();
    }
    
    protected abstract void clearImpl();
    
    protected abstract void point(int x, int y, Colorc color);
    
    public void point(int x, int y)
    {
        point(x, y, this.stroke);
    }
    
    // TODO - Draw Pattern
    public abstract void line(int x1, int y1, int x2, int y2);
    
    public abstract void bezier(int x1, int y1, int x2, int y2, int x3, int y3);
    
    public abstract void circle(int x, int y, int radius);
    
    public abstract void ellipse(int x, int y, int w, int h);
    
    public abstract void rect(int x, int y, int w, int h);
    
    public abstract void triangle(int x1, int y1, int x2, int y2, int x3, int y3);
    
    public abstract void partialSprite(int x, int y, Sprite sprite, int ox, int oy, int w, int h, double scale);
    
    public void partialSprite(int x, int y, Sprite sprite, int ox, int oy, int w, int h)
    {
        partialSprite(x, y, sprite, ox, oy, w, h, 1);
    }
    
    public void sprite(int x, int y, Sprite sprite, double scale)
    {
        partialSprite(x, y, sprite, 0, 0, sprite.getWidth(), sprite.getHeight(), scale);
    }
    
    public void sprite(int x, int y, Sprite sprite)
    {
        partialSprite(x, y, sprite, 0, 0, sprite.getWidth(), sprite.getHeight(), 1);
    }
    
    public abstract void text(int x, int y, String text, double scale);
    
    public void text(int x, int y, String text)
    {
        text(x, y, text, 1);
    }
}
