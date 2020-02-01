package pe.gui;

import pe.Color;
import pe.Mouse;
import pe.gui.event.IScrolled;

import static pe.PixelEngine.*;

public class ScrollBar extends Window
{
    private final Orientation orientation;
    
    private final ScrollButton one;
    private final ScrollButton two;
    
    private int mousePos = 0;
    private int scrollSize;
    
    private IScrolled scrolled;
    
    public ScrollBar(Window parent, Orientation orientation)
    {
        super(parent);
        
        this.orientation = orientation;
        
        if (this.orientation == Orientation.VERTICAL)
        {
            int temp = this.width;
            this.width = this.height;
            this.height = temp;
        }
        
        this.one = new ScrollButton(this, this.orientation == Orientation.HORIZONTAL ? End.LEFT : End.TOP);
        this.one.setWidth(this.orientation == Orientation.HORIZONTAL ? 12 : getWidth());
        this.one.setHeight(this.orientation == Orientation.HORIZONTAL ? getHeight() : 12);
        this.one.setPosition(0, 0);
        
        this.two = new ScrollButton(this, this.orientation == Orientation.HORIZONTAL ? End.RIGHT : End.BOTTOM);
        this.two.setWidth(this.orientation == Orientation.HORIZONTAL ? 12 : getWidth());
        this.two.setHeight(this.orientation == Orientation.HORIZONTAL ? getHeight() : 12);
        this.two.setX(this.orientation == Orientation.HORIZONTAL ? getWidth() - this.two.getWidth() : 0);
        this.two.setY(this.orientation == Orientation.HORIZONTAL ? 0 : getHeight() - this.two.getHeight());
        
        this.scrollSize = (this.orientation == Orientation.HORIZONTAL ? getWidth() : getHeight()) / 5;
    }
    
    public Orientation getOrientation()
    {
        return this.orientation;
    }
    
    @Override
    protected void updatedWidth(int prev, int width)
    {
        super.updatedWidth(prev, width);
        
        this.one.setWidth(this.orientation == Orientation.HORIZONTAL ? 12 : getWidth());
        this.two.setWidth(this.orientation == Orientation.HORIZONTAL ? 12 : getWidth());
        this.two.setX(this.orientation == Orientation.HORIZONTAL ? getWidth() - this.two.getWidth() : 0);
        this.two.setY(this.orientation == Orientation.HORIZONTAL ? 0 : getHeight() - this.two.getHeight());
        
        if (this.orientation == Orientation.HORIZONTAL) this.scrollSize = getWidth() / 5;
    }
    
    @Override
    protected void updatedHeight(int prev, int height)
    {
        super.updatedHeight(prev, height);
        
        this.one.setHeight(this.orientation == Orientation.HORIZONTAL ? getHeight() : 12);
        this.two.setHeight(this.orientation == Orientation.HORIZONTAL ? getHeight() : 12);
        this.two.setX(this.orientation == Orientation.HORIZONTAL ? getWidth() - this.two.getWidth() : 0);
        this.two.setY(this.orientation == Orientation.HORIZONTAL ? 0 : getHeight() - this.two.getHeight());
        
        if (this.orientation == Orientation.VERTICAL) this.scrollSize = getHeight() / 5;
    }
    
    @Override
    protected void updatedBorderSize(int prev, int borderSize)
    {
        super.updatedBorderSize(prev, borderSize);
        this.one.setBorderSize(borderSize);
        this.two.setBorderSize(borderSize);
    }
    
    @Override
    public int getForegroundOriginX()
    {
        return super.getForegroundOriginX() + (this.orientation == Orientation.HORIZONTAL ? this.one.getWidth() - 1 : 0);
    }
    
    @Override
    public int getForegroundOriginY()
    {
        return super.getForegroundOriginY() + (this.orientation == Orientation.VERTICAL ? this.one.getHeight() - 1 : 0);
    }
    
    @Override
    public int getForegroundWidth()
    {
        return super.getForegroundWidth() - (this.orientation == Orientation.HORIZONTAL ? this.one.getWidth() + this.two.getWidth() - 2 : 0);
    }
    
    @Override
    public int getForegroundHeight()
    {
        return super.getForegroundHeight() - (this.orientation == Orientation.VERTICAL ? this.one.getHeight() + this.two.getHeight() - 2 : 0);
    }
    
    public double getScroll()
    {
        return (double) this.mousePos / (double) ((this.orientation == Orientation.HORIZONTAL ? getForegroundWidth() : getForegroundHeight()) - this.scrollSize);
    }
    
    public void setScroll(double scroll)
    {
        int mousePos = (int) (((this.orientation == Orientation.HORIZONTAL ? getForegroundWidth() : getForegroundHeight()) - this.scrollSize) * scroll);
        
        if (this.mousePos != mousePos)
        {
            this.mousePos = mousePos;
            
            onScrolled(getScroll());
            
            redraw();
        }
    }
    
    public void onScrolled(IScrolled scrolled)
    {
        this.scrolled = scrolled;
    }
    
    @Override
    protected void drawWindow(double elapsedTime)
    {
        super.drawWindow(elapsedTime);
        
        switch (this.orientation)
        {
            case HORIZONTAL:
                drawRect(mousePos, 0, this.scrollSize, getForegroundHeight(), Color.BLACK);
                break;
            case VERTICAL:
                drawRect(0, mousePos, getForegroundWidth(), this.scrollSize, Color.BLACK);
                break;
        }
    }
    
    @Override
    protected boolean onMouseHeld(Mouse.Button button, int widgetX, int widgetY)
    {
        if (super.onMouseHeld(button, widgetX, widgetY) && button == Mouse.LEFT)
        {
            int mousePos = 0;
            switch (this.orientation)
            {
                case HORIZONTAL:
                    mousePos = Math.max(0, Math.min(widgetX - getForegroundOriginX() - this.scrollSize / 2, getForegroundWidth() - this.scrollSize));
                    break;
                case VERTICAL:
                    mousePos = Math.max(0, Math.min(widgetY - getForegroundOriginY() - this.scrollSize / 2, getForegroundHeight() - this.scrollSize));
                    break;
            }
            
            if (this.mousePos != mousePos)
            {
                this.mousePos = mousePos;
                
                onScrolled(getScroll());
                
                redraw();
            }
            
            return true;
        }
        return false;
    }
    
    protected void onScrolled(double value)
    {
        if (this.scrolled != null) this.scrolled.fire(value);
    }
    
    private static class ScrollButton extends Button
    {
        public final End end;
        
        public ScrollButton(Window parent, End end)
        {
            super(parent);
            
            this.end = end;
        }
        
        @Override
        protected void drawWindow(double elapsedTime)
        {
            clear(Color.BLANK);
            
            int centerX = getForegroundWidth() / 2, centerY = getForegroundHeight() / 2;
            int leftX   = (getForegroundWidth() - getForegroundHeight()) / 2, rightX = (getForegroundWidth() + getForegroundHeight()) / 2;
            int topY    = (getForegroundHeight() - getForegroundWidth()) / 2, bottomY = (getForegroundHeight() + getForegroundWidth()) / 2;
            
            switch (this.end)
            {
                case TOP:
                    drawLine(centerX, 0, 0, getForegroundHeight(), getTextColor());
                    drawLine(centerX, 0, getForegroundWidth(), getForegroundHeight(), getTextColor());
                    break;
                case BOTTOM:
                    drawLine(centerX, getForegroundHeight(), 0, 0, getTextColor());
                    drawLine(centerX, getForegroundHeight(), getForegroundWidth(), 0, getTextColor());
                    break;
                case LEFT:
                    drawLine(0, centerY, getForegroundWidth(), 0, getTextColor());
                    drawLine(0, centerY, getForegroundWidth(), getForegroundHeight(), getTextColor());
                    break;
                case RIGHT:
                    drawLine(getForegroundWidth(), centerY, 0, 0, getTextColor());
                    drawLine(getForegroundWidth(), centerY, 0, getForegroundHeight(), getTextColor());
                    break;
                default:
            }
        }
    }
    
    private enum End
    {
        TOP, BOTTOM, LEFT, RIGHT
    }
}
