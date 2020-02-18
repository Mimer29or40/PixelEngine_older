package pe.gui;

import pe.color.Color;
import pe.color.Colorc;

import java.util.List;

import static pe.PixelEngine.*;

public class Label extends Window
{
    public Label(Window parent, String text, double scale)
    {
        super(parent);
        
        setMarginSize(1);
        
        setScale(scale);
        
        int width = textWidth(text, getScale());
        if (width > getForegroundWidth()) setForegroundWidth(width);
    
        int height = textHeight(text, getScale());
        if (height > getForegroundHeight()) setForegroundHeight(height);
        
        setText(text);
    }
    
    public Label(Window parent, String text)
    {
        this(parent, text, 1);
    }
    
    public Label(Window parent)
    {
        this(parent, "", 1);
    }
    
    public void fitWidth(boolean shrink)
    {
        int width    = getWidth();
        int newWidth = textWidth(getText(), getScale()) + (getBorderSize() + getMarginSize()) * 2;
        if ((shrink && newWidth < width) || newWidth > width) setWidth(newWidth);
    }
    
    public void fitHeight(boolean shrink)
    {
        int height    = getHeight();
        int newHeight = textHeight(getText(), getScale()) + (getBorderSize() + getMarginSize()) * 2;
        if ((shrink && newHeight < height) || newHeight > height) setHeight(newHeight);
    }
    
    public void fit(boolean shrink)
    {
        fitWidth(shrink);
        fitHeight(shrink);
    }
    
    /*
     * Text Property
     */
    protected String text = "";
    
    public String getText()
    {
        return this.text;
    }
    
    public void setText(String text)
    {
        if (!text.equals(this.text))
        {
            String prev = this.text;
            this.text = text;
            updatedText(prev, this.text);
        }
    }
    
    protected void updatedText(String prev, String text)
    {
        redraw();
    }
    
    /*
     * Scale Property
     */
    protected double scale = 1;
    
    public double getScale()
    {
        return this.scale;
    }
    
    public void setScale(double scale)
    {
        if (scale != this.scale)
        {
            double prev = this.scale;
            this.scale = scale;
            updatedScale(prev, this.scale);
        }
    }
    
    protected void updatedScale(double prev, double scale)
    {
        redraw();
    }
    
    /*
     * Text Color Property
     */
    protected final Color textColor     = new Color(Color.BLACK);
    private final   Color prevTextColor = new Color(Color.BLACK);
    
    public Colorc getTextColor()
    {
        return this.textColor;
    }
    
    public void setTextColor(Colorc textColor)
    {
        if (!this.textColor.equals(textColor))
        {
            this.prevTextColor.set(this.textColor);
            this.textColor.set(textColor);
            updatedTextColor(this.prevTextColor, this.textColor);
        }
    }
    
    protected void updatedTextColor(Colorc prev, Color textColor)
    {
        redraw();
    }
    
    /*
     * Text Position Property
     */
    protected TextPosition textPosition = TextPosition.CENTER;
    
    public TextPosition getTextPosition()
    {
        return this.textPosition;
    }
    
    public void setTextPosition(TextPosition textPosition)
    {
        if (this.textPosition != textPosition)
        {
            TextPosition prev = this.textPosition;
            this.textPosition = textPosition;
            updatedTextPosition(prev, this.textPosition);
        }
    }
    
    protected void updatedTextPosition(TextPosition prev, TextPosition textPosition)
    {
        redraw();
    }
    
    /*
     * Drawing Stuff
     */
    @Override
    protected void drawWindow(double elapsedTime)
    {
        super.drawWindow(elapsedTime);
    
        if (textWidth(getText(), getScale()) > getForegroundWidth())
        {
            List<String> lines = clipTextWidth(getText(), getScale(), getForegroundWidth());
        
            int textY = getForegroundHeight() - (lines.size() * scaleToPixels(getScale()));
            int vPos  = getTextPosition().getVertical();
            int y     = vPos > 0 ? textY : vPos == 0 ? textY / 2 : 0;
            for (String line : lines)
            {
                int textX = getForegroundWidth() - textWidth(line, getScale());
                
                int hPos = getTextPosition().getHorizontal();
                
                int x = hPos > 0 ? textX : hPos == 0 ? textX / 2 : 0;
                
                drawString(getForegroundOriginX() + x, getForegroundOriginY() + y, line, getTextColor(), getScale());
                
                y += 8 * getScale();
            }
            
        }
        else
        {
            int textX = getForegroundWidth() - textWidth(getText(), getScale());
            int textY = getForegroundHeight() - textHeight(getText(), getScale());
        
            int hPos = getTextPosition().getHorizontal();
            int vPos = getTextPosition().getVertical();
        
            int x = hPos > 0 ? textX : hPos == 0 ? textX / 2 : 0;
            int y = vPos > 0 ? textY : vPos == 0 ? textY / 2 : 0;
        
            drawString(getForegroundOriginX() + x, getForegroundOriginY() + y, getText(), getTextColor(), getScale());
        }
    }
}
