package pe.gui;

import pe.Color;
import pe.DrawMode;
import pe.PixelEngine;

import static pe.PixelEngine.*;

public class ScrollPane extends Pane // TODO
{
    
    public ScrollPane(Window parent, int width, int height, String title)
    {
        super(parent, width, height, title);
    }
    
    public ScrollPane(Window parent, int width, int height)
    {
        this(parent, width, height, "");
    }
    
    public ScrollPane(Window parent, String title)
    {
        this(parent, 30, 30, title);
    }
    
    public ScrollPane(Window parent)
    {
        this(parent, 30, 30, "");
    }
    
    @Override
    public int getForegroundWidth()
    {
        return super.getForegroundWidth();
    }
    
    @Override
    public int getForegroundHeight()
    {
        return super.getForegroundHeight();
    }
    
    @Override
    public void setForegroundWidth(int width)
    {
        super.setForegroundWidth(width);
    }
    
    @Override
    public void setForegroundHeight(int height)
    {
        super.setForegroundHeight(height);
    }
    
    private int offsetX = 0;
    
    protected int getOffsetX()
    {
        return this.offsetX;
    }
    
    protected void setOffsetX(int offsetX)
    {
        offsetX = Math.max(0, offsetX);
        if (this.offsetX != offsetX)
        {
            int prev = this.offsetX;
            this.offsetX = offsetX;
            updatedOffsetX(prev, this.offsetX);
        }
    }
    
    protected void updatedOffsetX(int prev, int offsetX)
    {
        redraw();
    }
    
    private int offsetY = 0;
    
    protected int getOffsetY()
    {
        return this.offsetY;
    }
    
    protected void setOffsetY(int offsetY)
    {
        offsetY = Math.max(0, offsetY);
        if (this.offsetY != offsetY)
        {
            int prev = this.offsetY;
            this.offsetY = offsetY;
            updatedOffsetY(prev, this.offsetY);
        }
    }
    
    protected void updatedOffsetY(int prev, int offsetY)
    {
        redraw();
    }
    
    private int internalWidth = 0;
    
    public int getInternalWidth()
    {
        return this.internalWidth;
    }
    
    private void setInternalWidth()
    {
        this.internalWidth = 0;
        for (Window child : getChildren()) this.internalWidth = Math.max(this.internalWidth, child.getMaxX());
    }
    
    private int internalHeight = 0;
    
    public int getInternalHeight()
    {
        return this.internalHeight;
    }
    
    private void setInternalHeight()
    {
        this.internalHeight = 0;
        for (Window child : getChildren()) this.internalHeight = Math.max(this.internalHeight, child.getMaxY());
    }
    
    /*
     * Drawing Stuff
     */
    
    @Override
    protected void drawChildren(double elapsedTime)
    {
        setInternalWidth();
        setInternalHeight();
    
        PixelEngine.drawMode(DrawMode.NORMAL);
        PixelEngine.renderTarget(getChildSprite());
        clear(Color.BLANK);
    
        for (Window child : getChildren())
        {
            child.draw(elapsedTime);
            if (child.isVisible())
            {
                PixelEngine.drawMode(DrawMode.NORMAL);
                PixelEngine.renderTarget(child == this.title ? getSprite() : getChildSprite());
                drawSprite(child.getX(), child.getY(), child.getSprite(), 1);
            }
        }
    
        PixelEngine.drawMode(DrawMode.ALPHA);
        PixelEngine.renderTarget(getSprite());
        drawPartialSprite(getForegroundOriginX(), getForegroundOriginY(), getChildSprite(), getOffsetX(), getOffsetY(), getForegroundWidth(), getForegroundHeight(), 1);
    }
}
