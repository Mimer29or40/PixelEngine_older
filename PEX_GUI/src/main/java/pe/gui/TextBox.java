package pe.gui;

import pe.Color;
import pe.Keyboard;
import pe.Mouse;
import pe.gui.event.ITextChanged;
import pe.gui.event.ITextEntered;

import java.util.HashSet;
import java.util.Set;

import static pe.PixelEngine.drawLine;
import static pe.PixelEngine.scaleToPixels;

public class TextBox extends Label
{
    private   double  cursorTime = 0;
    protected boolean cursorOn   = false;
    
    private Keyboard.Key repeatingKey = null;
    
    private   String focusedText = "";
    protected int    cursorPos   = 0;
    
    public TextBox(Window parent, String text)
    {
        super(parent, text);
        
        setTextPosition(TextPosition.LEFT);
    }
    
    public TextBox(Window parent)
    {
        this(parent, "");
    }
    
    @Override
    protected void updatedText(String prev, String text)
    {
        if (this.validChars != null && this.validChars.size() > 0)
        {
            for (int i = 0; i < text.length(); i++)
            {
                if (!this.validChars.contains(text.charAt(i)))
                {
                    this.text = prev;
                    return;
                }
            }
        }
        super.updatedText(prev, text);
        this.cursorPos = Math.max(0, this.cursorPos + (text.length() - prev.length()));
        onTextChanged(this.text);
    }
    
    /*
     * Valid Chars Property
     */
    protected final Set<Character> validChars = new HashSet<>();
    
    public String getValidChars()
    {
        StringBuilder b = new StringBuilder();
        for (char c : this.validChars) b.append(b);
        return b.toString();
    }
    
    public void setValidChars(String validChars)
    {
        this.validChars.clear();
        for (int i = 0; i < validChars.length(); i++) this.validChars.add(validChars.charAt(i));
    }
    
    /*
     * Cursor Blink Frequency Property
     */
    protected double cursorFreq = 0.5;
    
    public double getCursorFreq()
    {
        return this.cursorFreq;
    }
    
    public void setCursorFreq(double cursorFreq)
    {
        if (this.cursorFreq != cursorFreq)
        {
            double prev = this.cursorFreq;
            this.cursorFreq = cursorFreq;
            updatedCursorFreq(prev, this.cursorFreq);
        }
    }
    
    protected void updatedCursorFreq(double prev, double text)
    {
    
    }
    
    /*
     * Focused Color Property
     */
    protected final Color focusedColor     = Color.GREY.copy();
    protected final Color prevFocusedColor = Color.GREY.copy();
    
    public Color getFocusedColor()
    {
        return this.focusedColor;
    }
    
    public void setFocusedColor(Color focusedColor)
    {
        if (!this.focusedColor.equals(focusedColor))
        {
            this.prevFocusedColor.set(this.focusedColor);
            this.focusedColor.set(focusedColor);
            updatedFocusedColor(this.prevFocusedColor, this.focusedColor);
        }
    }
    
    protected void updatedFocusedColor(Color prev, Color focusedColor)
    {
        redraw();
    }
    
    /*
     * Events
     */
    
    @Override
    protected void onFocused()
    {
        super.onFocused();
        this.cursorPos = this.text.length();
        this.focusedText = this.text;
        redraw();
    }
    
    @Override
    protected void onUnfocused()
    {
        super.onUnfocused();
        if (!this.focusedText.equals(this.text)) onTextEntered(this.text);
        redraw();
    }
    
    @Override
    protected boolean onMousePressed(Mouse.Button button, int widgetX, int widgetY)
    {
        if (super.onMousePressed(button, widgetX, widgetY))
        {
            if (button == Mouse.LEFT)
            {
                if (getMarginSize() <= widgetX && widgetX < getWidth() - getMarginSize() && getMarginSize() <= widgetY && widgetY < getHeight() - getMarginSize())
                {
                    this.cursorPos = Math.max(0, Math.min((int) Math.round((widgetX - getMarginSize()) / (8 * this.scale)), this.text.length()));
                    redraw();
                }
            }
            else if (button == Mouse.RIGHT)
            {
                setText("");
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
            this.repeatingKey = key;
            if (key == Keyboard.BACK)
            {
                if (this.text.length() > 0 && this.cursorPos != 0)
                {
                    if (this.cursorPos == this.text.length())
                    {
                        setText(this.text.substring(0, this.text.length() - 1));
                    }
                    else
                    {
                        setText(this.text.substring(0, this.cursorPos - 1) + this.text.substring(this.cursorPos));
                    }
                }
            }
            else if (key == Keyboard.DEL)
            {
                if (this.text.length() > 0 && this.cursorPos != this.text.length())
                {
                    if (this.cursorPos == 0)
                    {
                        setText(this.text.substring(1));
                    }
                    else
                    {
                        setText(this.text.substring(0, this.cursorPos) + this.text.substring(this.cursorPos + 1));
                        this.cursorPos++;
                    }
                }
                
            }
            else if (key == Keyboard.ENTER)
            {
                PEX_GUI.setFocused(null);
            }
            else if (key == Keyboard.HOME)
            {
                this.cursorPos = 0;
                redraw();
                
            }
            else if (key == Keyboard.END)
            {
                this.cursorPos = this.text.length();
                redraw();
            }
            else if (key == Keyboard.LEFT)
            {
                this.cursorPos = Math.max(0, this.cursorPos - 1);
                redraw();
            }
            else if (key == Keyboard.RIGHT)
            {
                this.cursorPos = Math.min(this.text.length(), this.cursorPos + 1);
                redraw();
            }
            else
            {
                if (key.baseChar > 0)
                {
                    if (this.cursorPos == 0)
                    {
                        setText((Keyboard.isShiftDown() ? key.shiftChar : key.baseChar) + this.text);
                    }
                    else if (this.cursorPos == this.text.length())
                    {
                        setText(this.text + (Keyboard.isShiftDown() ? key.shiftChar : key.baseChar));
                    }
                    else
                    {
                        setText(this.text.substring(0, this.cursorPos) + (Keyboard.isShiftDown() ? key.shiftChar : key.baseChar) + this.text.substring(this.cursorPos));
                    }
                }
            }
            return true;
        }
        return false;
    }
    
    @Override
    protected boolean onKeyRepeated(Keyboard.Key key)
    {
        if (super.onKeyRepeated(key))
        {
            if (this.repeatingKey == key) onKeyPressed(key);
            return true;
        }
        return false;
    }
    
    private ITextChanged textChanged;
    
    public void onTextChanged(ITextChanged textChanged)
    {
        this.textChanged = textChanged;
    }
    
    protected void onTextChanged(String text)
    {
        if (this.textChanged != null) this.textChanged.fire(text);
    }
    
    private ITextEntered textEntered;
    
    public void onTextEntered(ITextEntered textEntered)
    {
        this.textEntered = textEntered;
    }
    
    protected void onTextEntered(String text)
    {
        if (this.textEntered != null) this.textEntered.fire(text);
    }
    
    /*
     * Updating Stuff
     */
    
    @Override
    protected void updateWindow(double elapsedTime)
    {
        super.updateWindow(elapsedTime);
        
        this.cursorTime += elapsedTime;
        if (isFocused() && this.cursorOn && this.cursorTime >= this.cursorFreq * 2)
        {
            this.cursorTime = 0.0;
            this.cursorOn = false;
            redraw();
        }
        else if (isFocused() && !this.cursorOn && this.cursorTime >= this.cursorFreq)
        {
            this.cursorOn = true;
            redraw();
        }
    }
    
    /*
     * Drawing Stuff
     */
    
    @Override
    protected void drawWindow(double elapsedTime)
    {
        super.drawWindow(elapsedTime);
        
        if (isFocused() && this.cursorOn)
        {
            int cursorX = getForegroundOriginX() + scaleToPixels(getScale()) * this.cursorPos;
            int cursorY = getForegroundOriginY();
            drawLine(cursorX, cursorY, cursorX, cursorY + getForegroundHeight() - 1, this.textColor);
        }
    }
}