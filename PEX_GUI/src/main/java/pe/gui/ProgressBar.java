package pe.gui;

import pe.color.Color;
import pe.color.Colorc;
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
    protected final Color barColor     = new Color(Color.BLUE);
    protected final Color prevBarColor = new Color(Color.BLUE);
    
    public Colorc getBarColor()
    {
        return this.barColor;
    }
    
    public void setBarColor(Colorc barColor)
    {
        if (!this.barColor.equals(barColor))
        {
            this.prevBarColor.set(this.barColor);
            this.barColor.set(barColor);
            updatedBarColor(this.prevBarColor, this.barColor);
        }
    }
    
    protected void updatedBarColor(Colorc prev, Color barColor)
    {
        redraw();
    }
    
    /*
     * Disable Bar Property
     */
    protected final Color disabledBarColor     = new Color(Color.DARK_BLUE);
    protected final Color prevDisabledBarColor = new Color(Color.DARK_BLUE);
    
    public Colorc getDisabledBarColor()
    {
        return this.disabledBarColor;
    }
    
    public void setDisabledBarColor(Colorc disabledBarColor)
    {
        if (!this.disabledBarColor.equals(disabledBarColor))
        {
            this.prevDisabledBarColor.set(this.disabledBarColor);
            this.disabledBarColor.set(disabledBarColor);
            updatedDisabledBarColor(this.prevDisabledBarColor, this.disabledBarColor);
        }
    }
    
    protected void updatedDisabledBarColor(Colorc prev, Color focusedColor)
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
    
        renderer().noStroke();
        renderer().fill(this.enabled ? this.barColor : this.disabledBarColor);
        renderer().rect(getForegroundOriginX(), getForegroundOriginY(), (int) (this.value * getForegroundWidth()), getForegroundHeight());
    
        if (canDrawValue())
        {
            String text = (int) Math.floor(this.value * 100) + "%";
        
            int x = (getForegroundWidth() - textWidth(text)) / 2 + getForegroundOriginX();
            int y = (getForegroundHeight() - textHeight(text)) / 2 + getForegroundOriginY();
    
            renderer().stroke(Color.BLACK);
            renderer().string(x, y, text);
        }
    }
}
