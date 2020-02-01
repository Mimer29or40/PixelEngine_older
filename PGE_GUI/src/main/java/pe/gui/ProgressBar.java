package pe.gui;

import pe.Color;
import pe.gui.event.IProgressCompleted;

import static pe.PixelEngine.*;

public class ProgressBar extends Label
{
    public ProgressBar(Window parent)
    {
        super(parent);
    }
    
    /*
     * Draw Value Property
     */
    protected boolean drawValue = true;
    
    public boolean canDrawValue()
    {
        return this.drawValue;
    }
    
    public void setDrawValue(boolean drawValue)
    {
        if (this.drawValue != drawValue)
        {
            boolean prev = this.drawValue;
            this.drawValue = drawValue;
            updatedDrawValue(prev, this.drawValue);
        }
    }
    
    public void updatedDrawValue(boolean prev, boolean drawValue)
    {
        redraw();
    }
    
    /*
     * Value Property
     */
    protected double value = 0.0;
    
    public double getValue()
    {
        return this.value;
    }
    
    public void setValue(double value)
    {
        value = Math.max(0.0, Math.min(value, 1.0));
        if (this.value != value)
        {
            double prev = this.value;
            this.value = value;
            updatedValue(prev, this.value);
        }
    }
    
    public void updatedValue(double prev, double value)
    {
        redraw();
        if (value == 1.0) onProgressCompleted();
    }
    
    /*
     * Bar Color Property
     */
    protected final Color barColor     = Color.BLUE.copy();
    protected final Color prevBarColor = Color.BLUE.copy();
    
    public Color getBarColor()
    {
        return this.barColor;
    }
    
    public void setBarColor(Color barColor)
    {
        if (!this.barColor.equals(barColor))
        {
            this.prevBarColor.set(this.barColor);
            this.barColor.set(barColor);
            updatedBarColor(this.prevBarColor, this.barColor);
        }
    }
    
    protected void updatedBarColor(Color prev, Color barColor)
    {
        redraw();
    }
    
    /*
     * Disable Bar Property
     */
    protected final Color disabledBarColor     = Color.DARK_BLUE.copy();
    protected final Color prevDisabledBarColor = Color.DARK_BLUE.copy();
    
    public Color getDisabledBarColor()
    {
        return this.disabledBarColor;
    }
    
    public void setDisabledBarColor(Color disabledBarColor)
    {
        if (!this.disabledBarColor.equals(disabledBarColor))
        {
            this.prevDisabledBarColor.set(this.disabledBarColor);
            this.disabledBarColor.set(disabledBarColor);
            updatedDisabledBarColor(this.prevDisabledBarColor, this.disabledBarColor);
        }
    }
    
    protected void updatedDisabledBarColor(Color prev, Color focusedColor)
    {
        redraw();
    }
    
    /*
     * Events
     */
    
    private IProgressCompleted progressCompleted;
    
    public void onProgressCompleted(IProgressCompleted progressCompleted)
    {
        this.progressCompleted = progressCompleted;
    }
    
    protected void onProgressCompleted()
    {
        if (this.progressCompleted != null) this.progressCompleted.fire();
    }
    
    /*
     * Drawing Stuff
     */
    
    @Override
    protected void drawWindow(double elapsedTime)
    {
        super.drawWindow(elapsedTime);
        
        fillRect(getForegroundOriginX(),
                 getForegroundOriginY(),
                 (int) (this.value * getForegroundWidth()),
                 getForegroundHeight(),
                 this.enabled ? this.barColor : this.disabledBarColor);
        
        if (canDrawValue())
        {
            String text = (int) Math.floor(this.value * 100) + "%";
            
            int x = (getForegroundWidth() - getTextWidth(text)) / 2 + getForegroundOriginX();
            int y = (getForegroundHeight() - getTextHeight(text)) / 2 + getForegroundOriginY();
            
            drawString(x, y, text, Color.BLACK);
        }
    }
}
