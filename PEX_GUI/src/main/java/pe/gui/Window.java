package pe.gui;

import pe.*;
import pe.gui.event.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static pe.PixelEngine.*;

public class Window
{
    private static final Logger LOGGER = Logger.getLogger();
    
    public Window(Window parent)
    {
        setParent(parent);
    }
    
    private int layer = -1;
    
    public int getLayer()
    {
        return this.layer;
    }
    
    /*
     * Parent Methods
     */
    private Window parent = null;
    
    public Window getParent()
    {
        return this.parent;
    }
    
    public void setParent(Window parent)
    {
        if (getParent() != null) getParent().removeChild(this);
        if (parent == null)
        {
            this.layer = -1;
        }
        else
        {
            parent.addChild(this);
        }
    }
    
    public List<Window> getSiblings()
    {
        if (getParent() == null) return Collections.emptyList();
        return getParent().getChildren();
    }
    
    /*
     * Children Methods
     */
    private final List<Window> children = new ArrayList<>();
    
    public List<Window> getChildren()
    {
        return this.children;
    }
    
    public void addChild(Window child)
    {
        if (this == child) throw new RuntimeException("Window can not be its own parent");
        if (getChildren().contains(child)) throw new RuntimeException("Window can only be added to another Window once");
        if (child.parent != null) throw new RuntimeException("Window can only have one parent.");
        
        child.parent = this;
        getChildren().add(child);
        child.updateLayer();
    }
    
    public void removeChild(Window child)
    {
        if (this == child) throw new RuntimeException("Window is not a child child");
        if (!getChildren().contains(child)) throw new RuntimeException("Window is not a child child");
        if (child.parent == null) throw new RuntimeException("Window is not a child child");
        
        child.parent = null;
        getChildren().remove(child);
        child.layer = -1;
    }
    
    public boolean isDescendant(Window query)
    {
        for (Window child : getChildren()) if (child == query || child.isDescendant(query)) return true;
        return false;
    }
    
    /*
     * Focused Property
     */
    public boolean isFocused()
    {
        return PEX_GUI.isFocused(this);
    }
    
    public void setFocused()
    {
        PEX_GUI.setFocused(this);
    }
    
    /*
     * Is Top Property
     */
    public boolean isTop()
    {
        return PEX_GUI.isTop(this);
    }
    
    /*
     * Visible Property
     */
    protected boolean visible = true;
    
    public boolean isVisible()
    {
        return this.visible;
    }
    
    public void setVisible(boolean visible)
    {
        if (visible != this.visible)
        {
            boolean prev = this.visible;
            this.visible = visible;
            updatedVisible(prev, visible);
        }
    }
    
    protected void updatedVisible(boolean prev, boolean visible)
    {
    
    }
    
    /*
     * Enabled Property
     */
    protected boolean enabled = true;
    
    public boolean isEnabled()
    {
        return this.enabled;
    }
    
    public void setEnabled(boolean enabled)
    {
        if (enabled != this.enabled)
        {
            boolean prev = this.enabled;
            this.enabled = enabled;
            updatedEnabled(prev, enabled);
        }
        for (Window child : getChildren()) child.setEnabled(enabled);
    }
    
    protected void updatedEnabled(boolean prev, boolean enabled)
    {
        redraw();
    }
    
    /*
     * X Position Property
     */
    protected int x = 0;
    
    public int getX()
    {
        return this.x;
    }
    
    public void setX(int x)
    {
        if (x != this.x)
        {
            int prev = this.x;
            this.x = x;
            updatedX(prev, this.x);
        }
    }
    
    protected void updatedX(int prev, int x)
    {
        updateLayer();
    }
    
    /*
     * Y Position Property
     */
    protected int y = 0;
    
    public int getY()
    {
        return this.y;
    }
    
    public void setY(int y)
    {
        if (y != this.y)
        {
            int prev = this.y;
            this.y = y;
            updatedY(prev, this.y);
        }
    }
    
    protected void updatedY(int prev, int y)
    {
        updateLayer();
    }
    
    public void setPosition(int x, int y)
    {
        setX(x);
        setY(y);
    }
    
    /*
     * Width Property
     */
    protected int width = 0;
    
    public int getWidth()
    {
        return this.width;
    }
    
    public void setWidth(int width)
    {
        if (this.width != width)
        {
            int prev = this.width;
            this.width = width;
            updatedWidth(prev, this.width);
        }
    }
    
    protected void updatedWidth(int prev, int width)
    {
        updateLayer();
        updatedForegroundWidth(getForegroundWidth());
        regen();
    }
    
    /*
     * Height Property
     */
    protected int height = 0;
    
    public int getHeight()
    {
        return this.height;
    }
    
    public void setHeight(int height)
    {
        if (this.height != height)
        {
            int prev = this.height;
            this.height = height;
            updatedHeight(prev, this.height);
        }
    }
    
    protected void updatedHeight(int prev, int height)
    {
        updateLayer();
        updatedForegroundHeight(getForegroundHeight());
        regen();
    }
    
    public void setSize(int width, int height)
    {
        setWidth(width);
        setHeight(height);
    }
    
    /*
     * Local Bottom Right Point
     */
    public int getMaxX()
    {
        return getX() + getWidth() - 1;
    }
    
    public int getMaxY()
    {
        return getY() + getHeight() - 1;
    }
    
    /*
     * Absolute Position
     */
    public int getAbsX()
    {
        return getX() + (getParent() != null ? getParent().getAbsX() : 0);
    }
    
    public int getAbsY()
    {
        return getY() + (getParent() != null ? getParent().getAbsY() : 0);
    }
    
    /*
     * Absolute Bottom Right Point
     */
    public int getAbsMaxX()
    {
        return getAbsX() + getWidth() - 1;
    }
    
    public int getAbsMaxY()
    {
        return getAbsY() + getHeight() - 1;
    }
    
    /*
     * Border Thickness Property
     */
    protected int borderSize = 1;
    
    public int getBorderSize()
    {
        return this.borderSize;
    }
    
    public void setBorderSize(int borderSize)
    {
        if (this.borderSize != borderSize)
        {
            int prev = this.borderSize;
            this.borderSize = borderSize;
            updatedBorderSize(prev, this.borderSize);
        }
    }
    
    protected void updatedBorderSize(int prev, int borderSize)
    {
        updatedForegroundOriginX(getForegroundOriginX());
        updatedForegroundOriginY(getForegroundOriginY());
        updatedForegroundWidth(getForegroundWidth());
        updatedForegroundHeight(getForegroundHeight());
        redraw();
    }
    
    /*
     * Margin Thickness Property
     */
    protected int marginSize = 0;
    
    public int getMarginSize()
    {
        return this.marginSize;
    }
    
    public void setMarginSize(int marginSize)
    {
        if (this.marginSize != marginSize)
        {
            int prev = this.marginSize;
            this.marginSize = marginSize;
            updatedMarginSize(prev, this.marginSize);
        }
    }
    
    protected void updatedMarginSize(int prev, int marginSize)
    {
        updatedForegroundOriginX(getForegroundOriginX());
        updatedForegroundOriginY(getForegroundOriginY());
        updatedForegroundWidth(getForegroundWidth());
        updatedForegroundHeight(getForegroundHeight());
        redraw();
    }
    
    public int getForegroundOriginX()
    {
        return getBorderSize() + getMarginSize();
    }
    
    protected void updatedForegroundOriginX(int x)
    {
    
    }
    
    public int getForegroundOriginY()
    {
        return getBorderSize() + getMarginSize();
    }
    
    protected void updatedForegroundOriginY(int y)
    {
    
    }
    
    public int getForegroundWidth()
    {
        return getWidth() - (getBorderSize() + getMarginSize()) * 2;
    }
    
    public void setForegroundWidth(int width)
    {
        setWidth(width + (getBorderSize() + getMarginSize()) * 2);
    }
    
    protected void updatedForegroundWidth(int width)
    {
    
    }
    
    public int getForegroundHeight()
    {
        return getHeight() - (getBorderSize() + getMarginSize()) * 2;
    }
    
    public void setForegroundHeight(int height)
    {
        setHeight(height + (getBorderSize() + getMarginSize()) * 2);
    }
    
    protected void updatedForegroundHeight(int height)
    {
    
    }
    
    public void setForegroundSize(int width, int height)
    {
        setForegroundWidth(width);
        setForegroundHeight(height);
    }
    
    /*
     * Default Background Color Property
     */
    protected final Color defaultColor     = Color.WHITE.copy();
    private final   Color prevDefaultColor = Color.WHITE.copy();
    
    public Color getDefaultColor()
    {
        return this.defaultColor;
    }
    
    public void setDefaultColor(Color defaultColor)
    {
        if (!this.defaultColor.equals(defaultColor))
        {
            this.prevDefaultColor.set(this.defaultColor);
            this.defaultColor.set(defaultColor);
            updatedEnabledColor(this.prevDefaultColor, this.defaultColor);
        }
    }
    
    protected void updatedEnabledColor(Color prev, Color enabledColor)
    {
        redraw();
    }
    
    /*
     * Disabled Background Color Property
     */
    protected final Color disabledColor     = Color.GREY.copy();
    private final   Color prevDisabledColor = Color.GREY.copy();
    
    public Color getDisabledColor()
    {
        return this.disabledColor;
    }
    
    public void setDisabledColor(Color disabledColor)
    {
        if (!this.disabledColor.equals(disabledColor))
        {
            this.prevDisabledColor.set(this.disabledColor);
            this.disabledColor.set(disabledColor);
            updatedEnabledColor(this.prevDisabledColor, this.disabledColor);
        }
    }
    
    protected void updatedDisabledColor(Color prev, Color disabledColor)
    {
        redraw();
    }
    
    /*
     * Border Color Property
     */
    protected final Color borderColor     = Color.BLACK.copy();
    private final   Color prevBorderColor = Color.BLACK.copy();
    
    public Color getBorderColor()
    {
        return this.borderColor;
    }
    
    public void setBorderColor(Color borderColor)
    {
        if (!this.borderColor.equals(borderColor))
        {
            this.prevBorderColor.set(this.borderColor);
            this.borderColor.set(borderColor);
            updatedEnabledColor(this.prevBorderColor, this.borderColor);
        }
    }
    
    protected void updatedBorderColor(Color prev, Color borderColor)
    {
        redraw();
    }
    
    /*
     * Parent Coordinate Space Hit Test
     */
    public boolean hitTest(int x, int y)
    {
        return getX() <= x && x < getMaxX() && getY() <= y && y < getMaxY();
    }
    
    /*
     * Absolute Coordinate Space Hit Test
     */
    public boolean hitTestAbs(int x, int y)
    {
        return getAbsX() <= x && x < getAbsMaxX() && getAbsY() <= y && y < getAbsMaxY();
    }
    
    /*
     * Determines if this window overlaps another sibling, updating its layer to one above it. Layer order is biased by the order added to parent
     */
    protected void updateLayer()
    {
        if (getParent() != null)
        {
            this.layer = getParent().getLayer() + 1;
            for (Window s : getSiblings())
            {
                if (s == this) continue;
                if (Math.max(this.getX(), s.getX()) < Math.min(this.getMaxX(), s.getMaxX()) && Math.max(this.getY(), s.getY()) < Math.min(this.getMaxY(), s.getMaxY()))
                {
                    this.layer = s.getLayer() + 1;
                }
            }
        }
        else
        {
            this.layer = 0;
        }
    }
    
    /*
     * Events
     */
    
    private IFocused focused;
    
    public void onFocused(IFocused focused)
    {
        this.focused = focused;
    }
    
    protected void onFocused()
    {
        if (this.focused != null) this.focused.fire();
    }
    
    private IUnfocused unfocused;
    
    public void onUnfocused(IUnfocused unfocused)
    {
        this.unfocused = unfocused;
    }
    
    protected void onUnfocused()
    {
        if (this.unfocused != null) this.unfocused.fire();
    }
    
    private IMouseEntered mEntered;
    
    public void onMouseEntered(IMouseEntered mouseEntered)
    {
        this.mEntered = mouseEntered;
    }
    
    protected void onMouseEntered()
    {
        if (this.mEntered != null) this.mEntered.fire();
    }
    
    private IMouseExited mExited;
    
    public void onMouseExited(IMouseExited mouseExited)
    {
        this.mExited = mouseExited;
    }
    
    protected void onMouseExited()
    {
        if (this.mExited != null) this.mExited.fire();
    }
    
    private IMouseButtonDown mBDown;
    
    public void onMouseButtonDown(IMouseButtonDown mouseButtonDown)
    {
        this.mBDown = mouseButtonDown;
    }
    
    protected boolean onMouseButtonDown(Mouse.Button button, int widgetX, int widgetY)
    {
        return this.mBDown == null || this.mBDown.fire(button, widgetX, widgetY);
    }
    
    private IMouseButtonUp mBUp;
    
    public void onMouseButtonUp(IMouseButtonUp mouseButtonUp)
    {
        this.mBUp = mouseButtonUp;
    }
    
    protected boolean onMouseButtonUp(Mouse.Button button, int widgetX, int widgetY)
    {
        return this.mBUp == null || this.mBUp.fire(button, widgetX, widgetY);
    }
    
    private IMouseButtonHeld mBHeld;
    
    public void onMouseButtonHeld(IMouseButtonHeld mouseButtonHeld)
    {
        this.mBHeld = mouseButtonHeld;
    }
    
    protected boolean onMouseButtonHeld(Mouse.Button button, int widgetX, int widgetY)
    {
        return this.mBHeld == null || this.mBHeld.fire(button, widgetX, widgetY);
    }
    
    private IMouseButtonRepeated mBRepeated;
    
    public void onMouseButtonRepeated(IMouseButtonRepeated mouseButtonRepeated)
    {
        this.mBRepeated = mouseButtonRepeated;
    }
    
    protected boolean onMouseButtonRepeated(Mouse.Button button, int widgetX, int widgetY)
    {
        return this.mBRepeated == null || this.mBRepeated.fire(button, widgetX, widgetY);
    }
    
    private IMouseButtonClicked mBClicked;
    
    public void onMouseButtonClicked(IMouseButtonClicked mouseButtonClicked)
    {
        this.mBClicked = mouseButtonClicked;
    }
    
    protected boolean onMouseButtonClicked(Mouse.Button button, int widgetX, int widgetY, boolean doubleClicked)
    {
        return this.mBClicked == null || this.mBClicked.fire(button, widgetX, widgetY, doubleClicked);
    }
    
    private IMouseButtonDragged mBDragged;
    
    public void onMouseButtonDragged(IMouseButtonDragged mouseButtonDragged)
    {
        this.mBDragged = mouseButtonDragged;
    }
    
    protected boolean onMouseButtonDragged(Mouse.Button button, int widgetX, int widgetY, int dragX, int dragY, int relX, int relY)
    {
        return this.mBDragged == null || this.mBDragged.fire(button, widgetX, widgetY, dragX, dragY, relX, relY);
    }
    
    private IMouseScrolled mScrolled;
    
    public void onMouseScrolled(IMouseScrolled mouseScrolled)
    {
        this.mScrolled = mouseScrolled;
    }
    
    protected boolean onMouseScrolled(int scrollX, int scrollY)
    {
        return this.mScrolled == null || this.mScrolled.fire(scrollX, scrollY);
    }
    
    private IKeyboardKeyDown kKDown;
    
    public void onKeyboardKeyDown(IKeyboardKeyDown keyboardKeyDown)
    {
        this.kKDown = keyboardKeyDown;
    }
    
    protected boolean onKeyboardKeyDown(Keyboard.Key key)
    {
        return this.kKDown == null || this.kKDown.fire(key);
    }
    
    private IKeyboardKeyUp kKUp;
    
    public void onKeyboardKeyUp(IKeyboardKeyUp keyboardKeyUp)
    {
        this.kKUp = keyboardKeyUp;
    }
    
    protected boolean onKeyboardKeyUp(Keyboard.Key key)
    {
        return this.kKUp == null || this.kKUp.fire(key);
    }
    
    private IKeyboardKeyHeld kKHeld;
    
    public void onKeyboardKeyHeld(IKeyboardKeyHeld keyboardKeyHeld)
    {
        this.kKHeld = keyboardKeyHeld;
    }
    
    protected boolean onKeyboardKeyHeld(Keyboard.Key key)
    {
        return this.kKHeld == null || this.kKHeld.fire(key);
    }
    
    private IKeyboardKeyRepeated kKRepeated;
    
    public void onKeyboardKeyRepeated(IKeyboardKeyRepeated keyboardKeyRepeated)
    {
        this.kKRepeated = keyboardKeyRepeated;
    }
    
    protected boolean onKeyboardKeyRepeated(Keyboard.Key key)
    {
        return this.kKRepeated == null || this.kKRepeated.fire(key);
    }
    
    private IKeyboardKeyPressed kKPressed;
    
    public void onKeyboardKeyPressed(IKeyboardKeyPressed keyboardKeyPressed)
    {
        this.kKPressed = keyboardKeyPressed;
    }
    
    protected boolean onKeyboardKeyPressed(Keyboard.Key key, boolean doublePressed)
    {
        return this.kKPressed == null || this.kKPressed.fire(key, doublePressed);
    }
    
    private IKeyboardKeyTyped kKTyped;
    
    public void onKeyboardKeyTyped(IKeyboardKeyTyped keyboardKeyTyped)
    {
        this.kKTyped = keyboardKeyTyped;
    }
    
    protected boolean onKeyboardKeyTyped(char character)
    {
        return this.kKTyped == null || this.kKTyped.fire(character);
    }
    
    /*
     * Tooltip provider
     */
    
    private ITooltipText tooltipText;
    
    public void getTooltipText(ITooltipText tooltipText)
    {
        this.tooltipText = tooltipText;
    }
    
    public String getTooltipText()
    {
        return this.tooltipText != null ? this.tooltipText.getTooltip() : getParent() != null ? getParent().getTooltipText() : "";
    }
    
    /*
     * Updating Stuff
     */
    
    protected void update(double elapsedTime)
    {
        if (isVisible())
        {
            updateWindow(elapsedTime);
            
            for (Window child : getChildren()) child.update(elapsedTime);
        }
    }
    
    protected void updateWindow(double elapsedTime)
    {
    
    }
    
    /*
     * Drawing Stuff
     */
    
    protected Color getBackgroundColor()
    {
        return isEnabled() ? getDefaultColor() : getDisabledColor();
    }
    
    /*
     * Flag to Redraw Window. Causes parent chain to redraw
     */
    private boolean redraw = true;
    
    public void redraw()
    {
        this.redraw = true;
        if (getParent() != null) getParent().redraw();
    }
    
    /*
     * Flag to Re-Generate sprite. Causes parent chain to redraw
     */
    private boolean regen = true;
    
    public void regen()
    {
        this.redraw = this.regen = true;
        if (getParent() != null) getParent().redraw();
    }
    
    private Sprite sprite;
    
    public Sprite getSprite()
    {
        return this.sprite;
    }
    
    protected void draw(double elapsedTime)
    {
        if (isVisible())
        {
            if (this.redraw)
            {
                if (this.regen)
                {
                    LOGGER.debug("Regen: %s", this);
    
                    generateSprites(elapsedTime);
    
                    this.regen = false;
                }
    
                LOGGER.debug("Redraw: %s", this);
    
                PixelEngine.drawMode(DrawMode.NORMAL);
                PixelEngine.renderTarget(getSprite());
                drawWindow(elapsedTime);
    
                drawBorder(elapsedTime);
    
                drawChildren(elapsedTime);
    
                this.redraw = false;
            }
        }
    }
    
    protected void generateSprites(double elapsedTime)
    {
        if (getSprite() != null) getSprite().clear();
        
        this.sprite = new Sprite(getWidth(), getHeight());
    }
    
    protected void drawWindow(double elapsedTime)
    {
        fillRect(0, 0, getWidth(), getHeight(), getBackgroundColor());
    }
    
    protected void drawBorder(double elapsedTime)
    {
        for (int i = 0; i < getBorderSize(); i++) drawRect(i, i, getWidth() - i * 2, getHeight() - i * 2, getBorderColor());
    }
    
    protected void drawChildren(double elapsedTime)
    {
        for (Window child : getChildren())
        {
            child.draw(elapsedTime);
            if (child.isVisible())
            {
                PixelEngine.drawMode(DrawMode.NORMAL);
                PixelEngine.renderTarget(getSprite());
                drawSprite(child.getX(), child.getY(), child.getSprite(), 1);
            }
        }
    }
    
    /*
     * Utility Methods. These are used internally and should not be called
     */
    
    private boolean mouseOver = false;
    
    public boolean isMouseOver()
    {
        return this.mouseOver;
    }
    
    void mouseOver(int mouseX, int mouseY, boolean modal)
    {
        if (isVisible())
        {
            int widgetX = mouseX - getAbsX(), widgetY = mouseY - getAbsY();
            this.mouseOver = 0 <= widgetX && widgetX < getWidth() && 0 <= widgetY && widgetY < getHeight() && modal;
            
            getChildren().forEach(widget -> widget.mouseOver(mouseX, mouseY, modal));
        }
        else
        {
            this.mouseOver = false;
        }
    }
    
    Window getTop()
    {
        Window top = null;
        if (isVisible())
        {
            for (Window widget : getChildren())
            {
                Window topChild = widget.getTop();
                if (top == null || (topChild != null && topChild.getLayer() >= top.getLayer())) top = topChild;
            }
        }
        return top != null ? top : isMouseOver() ? this : null;
    }
}
