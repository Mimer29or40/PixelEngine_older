package pe.neat;

import pe.Color;
import pe.Sprite;
import pe.draw.DrawMode;

import java.util.List;

import static pe.PixelEngine.*;

public class FitnessPlotter
{
    public int imageWidth  = 400;
    public int imageHeight = 300;
    
    public int borderSize = 20;
    
    public final Color backgroundColor = Color.WHITE.copy();
    public final Color borderColor     = Color.BLACK.copy();
    public final Color textColor       = Color.BLACK.copy();
    
    // public final Color nodeBorderColor = Color.BLACK.copy();
    // public final Color inputNodeColor  = new Color(50, 200, 100);
    // public final Color outputNodeColor = new Color(50, 100, 200);
    // public final Color hiddenNodeColor = new Color(200, 50, 100);
    // public final Color biasNodeColor   = new Color(200, 100, 50);
    //
    // public final Color posConnColor = new Color(0, 0, 255, 100);
    // public final Color negConnColor = new Color(255, 0, 0, 100);
    // public final Color disConnColor = new Color(0, 0, 0, 100);
    //
    // public Orientation orientation = Orientation.RIGHT;
    
    public FitnessPlotter(int imageWidth, int imageHeight)
    {
        this.imageWidth  = imageWidth;
        this.imageHeight = imageHeight;
    }
    
    public FitnessPlotter() { }
    
    public Sprite generatePlot(List<Double> fitnessArray)
    {
        double minValue = Double.MAX_VALUE;
        double maxValue = Double.MIN_VALUE;
        int    size     = fitnessArray.size();
        
        for (double value : fitnessArray)
        {
            minValue = Math.min(minValue, value);
            maxValue = Math.max(maxValue, value);
        }
        
        int minX = this.borderSize + textWidth("" + (float) minValue) + 5;
        int minY = this.borderSize;
        int maxX = this.imageWidth - this.borderSize;
        int maxY = this.imageHeight - this.borderSize;
    
        Sprite   sprite = new Sprite(this.imageWidth, this.imageHeight, this.backgroundColor);
        Sprite   prev   = drawTarget();
        DrawMode mode   = drawMode();
        drawTarget(sprite);
        drawMode(DrawMode.ALPHA);
        
        drawRect(minX, minY, maxX - minX + 1, maxY - minY + 1, this.borderColor);
        
        int prevX = minX, prevY = maxY;
        for (int i = 0; i < size; i++)
        {
            int x = (int) map(i, 0, size - 1, minX, maxX);
            int y = (int) map(fitnessArray.get(i), minValue, maxValue, maxY, minY);
            
            drawLine(x, y, prevX, prevY, Color.RED);
            prevX = x;
            prevY = y;
        }
        
        for (int i = 0; i < 5; i++)
        {
            int x = (int) map(i, 0, 4, minX, maxX);
            
            drawLine(x, maxY, x, maxY + 5, this.borderColor);
            
            String text  = "" + (int) map(i, 0, 4, 0, size);
            int    width = textWidth(text);
            
            drawString(x - width / 2, maxY + 10, text, this.textColor);
        }
        
        for (int i = 0; i < 5; i++)
        {
            int y = (int) map(i, 0, 4, maxY, minY);
            
            drawLine(minX, y, minX - 5, y, this.borderColor);
            
            String text   = "" + (float) map(i, 0, 4, minValue, maxValue);
            int    width  = textWidth(text);
            int    height = textHeight(text);
            
            drawString(minX - 10 - width, y - height / 2, text, this.textColor);
        }
    
        drawTarget(prev);
        drawMode(mode);
        return sprite;
    }
}
