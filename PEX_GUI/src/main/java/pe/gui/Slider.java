package pe.gui;

import pe.Color;
import pe.Keyboard;
import pe.Mouse;
import pe.gui.event.ISliderChanged;

import java.util.ArrayList;
import java.util.List;

import static pe.PixelEngine.*;

public class Slider extends Window
{
    private       int          barWidth;
    private final List<Double> barIncrements = new ArrayList<>();
    private final List<Double> increments    = new ArrayList<>();
    
    public Slider(Window parent, double min, double max, double increment)
    {
        super(parent);
        
        setMin(min);
        setMax(max);
        setIncrement(increment);
        
        setMarginSize(0);
        
        genIncrements();
        
        setValue((min + max) * 0.5);
    }
    
    public Slider(Window parent, double min, double max)
    {
        this(parent, min, max, 1);
    }
    
    private void genIncrements()
    {
        this.increments.clear();
        this.barIncrements.clear();
        
        for (double t = this.min; t < this.max; t += this.increment)
        {
            this.increments.add(t);
        }
        this.increments.add(this.max);
        
        double spacing = (getForegroundWidth()) / (double) this.increments.size();
        if (spacing >= 1)
        {
            int index = 1;
            for (int i = 0; i < getForegroundWidth(); i++)
            {
                this.barIncrements.add(this.increments.get(index - 1));
                if (i >= (int) (spacing * index)) index++;
            }
        }
        else if (spacing > 0)
        {
            double spacingInv = 1D / spacing * this.increment;
            for (double i = this.min; this.barIncrements.size() < getForegroundWidth(); i += spacingInv)
            {
                double closestDist = Double.MAX_VALUE;
                double closestInc  = Double.MAX_VALUE;
                for (double inc : this.increments)
                {
                    double dist = Math.abs(inc - i);
                    if (dist < closestDist)
                    {
                        closestDist = dist;
                        closestInc = inc;
                    }
                    else if (dist > closestDist)
                    {
                        break;
                    }
                }
                this.barIncrements.add(closestInc);
            }
            if (!this.barIncrements.isEmpty()) this.barIncrements.set(this.barIncrements.size() - 1, this.max);
        }
        for (int i = 0; i < this.barIncrements.size(); i++)
        {
            if (this.value < this.barIncrements.get(i))
            {
                this.barWidth = i;
                break;
            }
        }
        redraw();
    }
    
    @Override
    protected void updatedWidth(int prev, int width)
    {
        super.updatedWidth(prev, width);
        genIncrements();
    }
    
    /*
     * Current Value Property
     */
    protected double value;
    
    public double getValue()
    {
        return this.value;
    }
    
    public void setValue(double value)
    {
        if (this.value != value)
        {
            double prev = this.value;
            this.value = value;
            updatedValue(prev, this.value);
        }
    }
    
    protected void updatedValue(double prev, double value)
    {
        if (value <= this.min)
        {
            this.value = this.min;
            this.barWidth = 1;
        }
        else if (value >= this.max)
        {
            this.value = this.max;
            this.barWidth = getForegroundWidth();
        }
        else
        {
            int overIndex = this.increments.size();
            for (int i = 0; i < this.increments.size(); i++)
            {
                if (value < this.increments.get(i))
                {
                    overIndex = i;
                    break;
                }
            }
            double diffOver  = Math.abs(value - this.increments.get(overIndex));
            double diffUnder = Math.abs(value - this.increments.get(overIndex - 1));
            this.value = diffOver <= diffUnder ? this.increments.get(overIndex) : this.increments.get(overIndex - 1);
            
            for (int i = 0; i < this.barIncrements.size(); i++)
            {
                if (this.value < this.barIncrements.get(i))
                {
                    this.barWidth = i;
                    break;
                }
            }
        }
        onSliderChanged(this.value);
        redraw();
    }
    
    /*
     * Minimum Value Property
     */
    protected double min = 1;
    
    public double getMin()
    {
        return this.min;
    }
    
    public void setMin(double min)
    {
        if (this.min != min)
        {
            double prev = this.min;
            this.min = min;
            updatedMin(prev, this.min);
        }
    }
    
    protected void updatedMin(double prev, double min)
    {
        genIncrements();
    }
    
    /*
     * Maximum Value Property
     */
    protected double max = 10;
    
    public double getMax()
    {
        return this.max;
    }
    
    public void setMax(double max)
    {
        if (this.max != max)
        {
            double prev = this.max;
            this.max = max;
            updatedMax(prev, this.max);
        }
    }
    
    protected void updatedMax(double prev, double max)
    {
        genIncrements();
    }
    
    /*
     * Increment Property
     */
    protected double increment = 1;
    
    public double getIncrement()
    {
        return this.increment;
    }
    
    public void setIncrement(double increment)
    {
        if (this.increment != increment)
        {
            double prev = this.increment;
            this.increment = increment;
            updatedIncrement(prev, this.increment);
        }
    }
    
    protected void updatedIncrement(double prev, double increment)
    {
        genIncrements();
    }
    
    protected final Color sliderColor     = Color.BLUE.copy();
    private final   Color prevSliderColor = Color.BLUE.copy();
    
    public Color getSliderColor()
    {
        return this.sliderColor;
    }
    
    public void setSliderColor(Color sliderColor)
    {
        if (!this.sliderColor.equals(sliderColor))
        {
            this.prevSliderColor.set(this.sliderColor);
            this.sliderColor.set(sliderColor);
            updatedSliderColor(this.prevSliderColor, this.sliderColor);
        }
    }
    
    protected void updatedSliderColor(Color prev, Color focusedColor)
    {
        redraw();
    }
    
    /*
     * Events
     */
    
    @Override
    protected boolean onMouseHeld(Mouse.Button button, int widgetX, int widgetY)
    {
        if (super.onMouseHeld(button, widgetX, widgetY) && button == Mouse.LEFT)
        {
            int barWidth = Math.max(1, Math.min(widgetX - getForegroundOriginX() + 1, getForegroundWidth()));
            
            if (this.barWidth != barWidth)
            {
                this.barWidth = barWidth;
                
                setValue(this.barIncrements.get(this.barWidth - 1));
                
                redraw();
            }
            return true;
        }
        return false;
    }
    
    @Override
    protected boolean onMouseWheel(int scrollX, int scrollY)
    {
        if (super.onMouseWheel(scrollX, scrollY))
        {
            if (scrollY < 0 && getValue() > getMin())
            {
                setValue(getValue() - getIncrement());
            }
            else if (scrollY > 0 && getValue() < getMax())
            {
                setValue(getValue() + getIncrement());
            }
            return true;
        }
        return false;
    }
    
    @Override
    protected boolean onKeyPressed(Keyboard.Key key)
    {
        if (super.onKeyPressed(key))
        {
            if (key == Keyboard.LEFT && getValue() > getMin())
            {
                setValue(getValue() - getIncrement());
            }
            else if (key == Keyboard.RIGHT && getValue() < getMax())
            {
                setValue(getValue() + getIncrement());
            }
            return true;
        }
        return false;
    }
    
    private ISliderChanged sliderChanged;
    
    public void onSliderChanged(ISliderChanged sliderChanged)
    {
        this.sliderChanged = sliderChanged;
    }
    
    protected void onSliderChanged(double value)
    {
        if (this.sliderChanged != null) this.sliderChanged.fire(value);
    }
    
    /*
     * Drawing Stuff
     */
    
    @Override
    protected void drawWindow(double elapsedTime)
    {
        super.drawWindow(elapsedTime);
    
        fillRect(getForegroundOriginX(), getForegroundOriginY(), this.barWidth, getForegroundHeight(), getSliderColor());
    
        String text = getValue() == (int) getValue() ? String.valueOf((int) getValue()) : String.valueOf(round(getValue(), 3));
    
        int x = (getForegroundWidth() - textWidth(text)) / 2;
        int y = (getForegroundHeight() - textHeight(text)) / 2;
    
        drawString(getForegroundOriginX() + x, getForegroundOriginY() + y, text, Color.BLACK);
    }
}
