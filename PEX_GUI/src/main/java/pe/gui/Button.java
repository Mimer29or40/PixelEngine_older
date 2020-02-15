package pe.gui;

import pe.Keyboard;
import pe.Mouse;
import pe.color.Color;
import pe.color.Colorc;
import pe.gui.event.*;

public class Button extends Label
{
    public Button(Window parent, String text, int scale)
    {
        super(parent, text, scale);
    }
    
    public Button(Window parent, String text)
    {
        super(parent, text, 1);
    }
    
    public Button(Window parent)
    {
        super(parent, "", 1);
    }
    
    public void press()
    {
        onMouseButtonDown(Mouse.NONE, 0, 0);
    }
    
    public void release()
    {
        onMouseButtonUp(Mouse.NONE, 0, 0);
    }
    
    public void click()
    {
        onMouseButtonClicked(Mouse.NONE, 0, 0, false);
    }
    
    /*
     * Pressed Property
     */
    protected boolean pressed = false;
    
    public boolean isPressed()
    {
        return this.pressed;
    }
    
    public void setPressed(boolean pressed)
    {
        if (this.pressed != pressed)
        {
            boolean prev = this.pressed;
            this.pressed = pressed;
            updatedPressed(prev, this.pressed);
        }
    }
    
    protected void updatedPressed(boolean prev, boolean pressed)
    {
        redraw();
    }
    
    /*
     * Toggleable Property
     */
    protected boolean toggleable = false;
    
    public boolean isToggleable()
    {
        return this.toggleable;
    }
    
    public void setToggleable(boolean toggleable)
    {
        if (toggleable != this.toggleable)
        {
            boolean prev = this.toggleable;
            this.toggleable = toggleable;
            updatedToggleable(prev, this.toggleable);
        }
    }
    
    protected void updatedToggleable(boolean prev, boolean toggleable)
    {
        setPressed(false);
    }
    
    /*
     * Hover Color Property
     */
    protected final Color hoverColor     = new Color(Color.BLUE);
    private final   Color prevHoverColor = new Color(Color.BLUE);
    
    public Colorc getHoverColor()
    {
        return this.hoverColor;
    }
    
    public void setHoverColor(Colorc hoverColor)
    {
        if (!this.hoverColor.equals(hoverColor))
        {
            this.prevHoverColor.set(this.hoverColor);
            this.hoverColor.set(hoverColor);
            updatedHoverColor(this.prevHoverColor, this.hoverColor);
        }
    }
    
    protected void updatedHoverColor(Colorc prev, Color hoverColor)
    {
        redraw();
    }
    
    /*
     * Pressed Color Property
     */
    protected final Color pressedColor     = new Color(Color.DARK_BLUE);
    private final   Color prevPressedColor = new Color(Color.DARK_BLUE);
    
    public Colorc getPressedColor()
    {
        return this.pressedColor;
    }
    
    public void setPressedColor(Colorc pressedColor)
    {
        if (!this.pressedColor.equals(pressedColor))
        {
            this.prevPressedColor.set(this.pressedColor);
            this.pressedColor.set(pressedColor);
            updatedPressedColor(this.prevPressedColor, this.pressedColor);
        }
    }
    
    protected void updatedPressedColor(Colorc prev, Color pressedColor)
    {
        redraw();
    }
    
    /*
     * Events
     */
    
    @Override
    protected void onMouseEntered()
    {
        super.onMouseEntered();
        redraw();
    }
    
    @Override
    protected void onMouseExited()
    {
        super.onMouseExited();
        if (!isToggleable() && isPressed())
        {
            release();
        }
        redraw();
    }
    
    @Override
    protected boolean onMouseButtonDown(Mouse.Button button, int widgetX, int widgetY)
    {
        if (super.onMouseButtonDown(button, widgetX, widgetY))
        {
            if (!isToggleable())
            {
                if (!isPressed())
                {
                    setPressed(true);
                    onButtonPressed(button, widgetX, widgetY);
                }
            }
            else
            {
                setPressed(!isPressed());
                onButtonPressed(button, widgetX, widgetY);
                onButtonToggled(button, widgetX, widgetY, this.pressed);
            }
            return true;
        }
        return false;
    }
    
    @Override
    protected boolean onMouseButtonUp(Mouse.Button button, int widgetX, int widgetY)
    {
        if (super.onMouseButtonUp(button, widgetX, widgetY))
        {
            if (!isToggleable())
            {
                if (isPressed())
                {
                    setPressed(false);
                    onButtonReleased(button, widgetX, widgetY);
                }
            }
            else
            {
                onButtonReleased(button, widgetX, widgetY);
            }
            return true;
        }
        return false;
    }
    
    @Override
    protected boolean onMouseButtonHeld(Mouse.Button button, int widgetX, int widgetY)
    {
        if (super.onMouseButtonHeld(button, widgetX, widgetY))
        {
            onButtonHeld(button, widgetX, widgetY);
            if (!isToggleable() && !isPressed())
            {
                setPressed(true);
                onButtonPressed(button, widgetX, widgetY);
            }
            return true;
        }
        return false;
    }
    
    @Override
    protected boolean onMouseButtonClicked(Mouse.Button button, int widgetX, int widgetY, boolean doubleClicked)
    {
        if (super.onMouseButtonClicked(button, widgetX, widgetY, doubleClicked) && isEnabled())
        {
            onButtonClicked(button, widgetX, widgetY, doubleClicked);
            return true;
        }
        return false;
    }
    
    @Override
    protected boolean onKeyboardKeyDown(Keyboard.Key key)
    {
        if (super.onKeyboardKeyDown(key) && key == Keyboard.SPACE)
        {
            onButtonClicked(Mouse.NONE, 0, 0, false);
            return true;
        }
        return false;
    }
    
    private IButtonPressed buttonPressed;
    
    public void onButtonPressed(IButtonPressed buttonPressed)
    {
        this.buttonPressed = buttonPressed;
    }
    
    protected void onButtonPressed(Mouse.Button button, int widgetX, int widgetY)
    {
        if (this.buttonPressed != null) this.buttonPressed.fire(button, widgetX, widgetY);
    }
    
    private IButtonReleased buttonReleased;
    
    public void onButtonReleased(IButtonReleased buttonReleased)
    {
        this.buttonReleased = buttonReleased;
    }
    
    protected void onButtonReleased(Mouse.Button button, int widgetX, int widgetY)
    {
        if (this.buttonReleased != null) this.buttonReleased.fire(button, widgetX, widgetY);
    }
    
    private IButtonHeld buttonHeld;
    
    public void onButtonHeld(IButtonHeld buttonHeld)
    {
        this.buttonHeld = buttonHeld;
    }
    
    protected void onButtonHeld(Mouse.Button button, int widgetX, int widgetY)
    {
        if (this.buttonHeld != null) this.buttonHeld.fire(button, widgetX, widgetY);
    }
    
    private IButtonClicked buttonClicked;
    
    public void onButtonClicked(IButtonClicked buttonClicked)
    {
        this.buttonClicked = buttonClicked;
    }
    
    protected void onButtonClicked(Mouse.Button button, int widgetX, int widgetY, boolean doubleClicked)
    {
        if (this.buttonClicked != null) this.buttonClicked.fire(button, widgetX, widgetY, doubleClicked);
    }
    
    private IButtonToggled buttonToggled;
    
    public void onButtonToggled(IButtonToggled buttonToggled)
    {
        this.buttonToggled = buttonToggled;
    }
    
    protected void onButtonToggled(Mouse.Button button, int widgetX, int widgetY, boolean pressed)
    {
        if (this.buttonToggled != null) this.buttonToggled.fire(button, widgetX, widgetY, pressed);
    }
    
    /*
     * Drawing Stuff
     */
    
    protected Colorc getBackgroundColor()
    {
        if (isEnabled() && isPressed())
        {
            return getPressedColor();
        }
        else if (isEnabled() && isTop())
        {
            return getHoverColor();
        }
        else
        {
            return super.getBackgroundColor();
        }
    }
}
