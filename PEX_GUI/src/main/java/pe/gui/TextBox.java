package pe.gui;

import pe.Keyboard;
import pe.Mouse;
import pe.color.Color;
import pe.color.Colorc;
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
    
    // TODO - Get Text scroll working by storing full text to another variable and setting text based on cursorPos
    
    @Override
    protected void updatedText(String prev, String text)
    {
        if (getTextLimit() > -1 && text.length() > getTextLimit())
        {
            this.text = prev;
            return;
        }
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
     * Text Limit Property
     */
    protected int textLimit = -1;
    
    public int getTextLimit()
    {
        return this.textLimit;
    }
    
    public void setTextLimit(int textLimit)
    {
        if (this.textLimit != textLimit)
        {
            int prev = this.textLimit;
            this.textLimit = textLimit;
            updatedTextLimit(prev, this.textLimit);
        }
    }
    
    protected void updatedTextLimit(int prev, int textLimit)
    {
    
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
    protected final Color focusedColor     = new Color(Color.GREY);
    protected final Color prevFocusedColor = new Color(Color.GREY);
    
    public Colorc getFocusedColor()
    {
        return this.focusedColor;
    }
    
    public void setFocusedColor(Colorc focusedColor)
    {
        if (!this.focusedColor.equals(focusedColor))
        {
            this.prevFocusedColor.set(this.focusedColor);
            this.focusedColor.set(focusedColor);
            updatedFocusedColor(this.prevFocusedColor, this.focusedColor);
        }
    }
    
    protected void updatedFocusedColor(Colorc prev, Color focusedColor)
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
    protected boolean onMouseButtonDown(Mouse.Button button, int widgetX, int widgetY)
    {
        if (super.onMouseButtonDown(button, widgetX, widgetY))
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
    protected boolean onKeyboardKeyDown(Keyboard.Key key)
    {
        if (super.onKeyboardKeyDown(key))
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
            return true;
        }
        return false;
    }
    
    @Override
    protected boolean onKeyboardKeyRepeated(Keyboard.Key key)
    {
        if (super.onKeyboardKeyRepeated(key))
        {
            if (this.repeatingKey == key) onKeyboardKeyDown(key);
            return true;
        }
        return false;
    }
    
    @Override
    protected boolean onKeyboardKeyTyped(char character)
    {
        if (super.onKeyboardKeyTyped(character))
        {
            if (this.cursorPos == 0)
            {
                setText(character + this.text);
            }
            else if (this.cursorPos == this.text.length())
            {
                setText(this.text + character);
            }
            else
            {
                setText(this.text.substring(0, this.cursorPos) + character + this.text.substring(this.cursorPos));
            }
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
