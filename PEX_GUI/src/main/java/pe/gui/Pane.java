package pe.gui;

import pe.Color;
import pe.DrawMode;
import pe.Sprite;

import static pe.PixelEngine.*;

public class Pane extends Window
{
    protected final Label title;
    
    public Pane(Window parent, int width, int height, String title)
    {
        super(parent);
        
        this.title = new Label(this)
        {
            public int getAbsX()
            {
                return getX() + (getParent() != null ? getParent().getAbsX() - getParent().getForegroundOriginX() : 0);
            }
            
            public int getAbsY()
            {
                return getY() + (getParent() != null ? getParent().getAbsY() - getParent().getForegroundOriginY() : 0);
            }
            
            //@Override
            //protected boolean onMouseClicked(Mouse mouse, int widgetX, int widgetY, boolean doubleClicked)
            //{
            //    print("Title Clicked");
            //    return super.onMouseClicked(mouse, widgetX, widgetY, doubleClicked);
            //}
        };
        
        setTitle(title);
        setWidth(width);
        setHeight(height);
    }
    
    public Pane(Window parent, int width, int height)
    {
        this(parent, width, height, "");
    }
    
    public Pane(Window parent, String title)
    {
        this(parent, 30, 30, title);
    }
    
    public Pane(Window parent)
    {
        this(parent, 30, 30, "");
    }
    
    public int getAbsX()
    {
        return getX() + getForegroundOriginX() + (getParent() != null ? getParent().getAbsX() : 0);
    }
    
    public int getAbsY()
    {
        return getY() + getForegroundOriginY() + (getParent() != null ? getParent().getAbsY() : 0);
    }
    
    @Override
    protected void updatedWidth(int prev, int width)
    {
        super.updatedWidth(prev, width);
        this.title.setWidth(width);
    }
    
    @Override
    protected void updatedBorderSize(int prev, int borderSize)
    {
        super.updatedBorderSize(prev, borderSize);
        this.title.setBorderSize(width);
    }
    
    @Override
    public int getForegroundOriginY()
    {
        return super.getForegroundOriginY() + (this.title.isVisible() ? this.title.getHeight() - getBorderSize() : 0);
    }
    
    @Override
    public int getForegroundHeight()
    {
        return super.getForegroundHeight() - (this.title.isVisible() ? this.title.getHeight() - getBorderSize() : 0);
    }
    
    @Override
    public void setForegroundHeight(int height)
    {
        super.setForegroundHeight(height + (this.title.isVisible() ? this.title.getHeight() - getBorderSize() : 0));
    }
    
    /*
     * Title Property
     */
    public String getTitle()
    {
        return this.title.getText();
    }
    
    public void setTitle(String text)
    {
        this.title.setText(text);
        this.title.fitHeight(true);
        this.title.setVisible(!text.equals(""));
    }
    
    /*
     * Drawing Stuff
     */
    
    private Sprite childSprite = null;
    
    public Sprite getChildSprite()
    {
        return this.childSprite;
    }
    
    @Override
    protected void generateSprites(double elapsedTime)
    {
        super.generateSprites(elapsedTime);
        
        if (getChildSprite() != null) getChildSprite().clear();
        
        this.childSprite = new Sprite(getForegroundWidth(), getForegroundHeight());
    }
    
    @Override
    protected void drawChildren(double elapsedTime)
    {
        setDrawMode(DrawMode.NORMAL);
        setTarget(getChildSprite());
        clear(Color.BLANK);
        
        for (Window child : getChildren())
        {
            child.draw(elapsedTime);
            if (child.isVisible())
            {
                setDrawMode(DrawMode.NORMAL);
                setTarget(child == this.title ? getSprite() : getChildSprite());
                drawSprite(child.getX(), child.getY(), child.getSprite(), 1);
            }
        }
        
        setDrawMode(DrawMode.ALPHA);
        setTarget(getSprite());
        drawSprite(getForegroundOriginX(), getForegroundOriginY(), getChildSprite(), 1);
    }
}
